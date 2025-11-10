package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.inventoryDtos.*;
import com.sep490.bads.distributionsystem.entity.Inventory;
import com.sep490.bads.distributionsystem.entity.Product;
import com.sep490.bads.distributionsystem.entity.Qrcode;
import com.sep490.bads.distributionsystem.entity.Warehouse;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import com.sep490.bads.distributionsystem.entity.type.ProductStatus;
import com.sep490.bads.distributionsystem.exception.BadRequestException;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.repository.*;
import com.sep490.bads.distributionsystem.service.InventoryService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final GoodsReceiptDetailRepository goodsReceiptDetailRepo;
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;
    private final QrcodeRepository qrcodeRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryDto> listInventory(InventoryFilterDto f, Pageable pageable) {
        Page<Inventory> page = inventoryRepo.findAll(buildSpec(f), pageable);
        int nearDays = f.getExpireWithinDays() == null ? 14 : f.getExpireWithinDays();

        List<InventoryDto> content = page.getContent().stream()
                .map(this::toDto)
                .filter(dto -> matchStatus(dto, f.getStatus(), nearDays))
                .toList();

        return new PageImpl<>(content, pageable, page.getTotalElements());

    }

    @Override
    @Transactional(readOnly = true)
    public InventoryDto getById(Long id) {
        var inv = inventoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));
        return toDto(inv);
    }

    private Specification<Inventory> buildSpec(InventoryFilterDto f) {
        return (root, q, cb) -> {
            var ps = new ArrayList<Predicate>();
            var product = root.join("product");
            var warehouse = root.join("warehouse");

            if (f.getWarehouseId() != null) {
                ps.add(cb.equal(warehouse.get("id"), f.getWarehouseId()));
            }

            if (StringUtils.hasText(f.getQ())) {
                String kw = "%" + f.getQ().trim().toLowerCase() + "%";
                ps.add(cb.or(
                        cb.like(cb.lower(product.get("sku")), kw),
                        cb.like(cb.lower(product.get("name")), kw)
                ));
            }

            if (f.getExpireWithinDays() != null && f.getExpireWithinDays() > 0) {
                LocalDate now = LocalDate.now();
                LocalDate to = now.plusDays(f.getExpireWithinDays());
                ps.add(cb.and(
                        cb.isNotNull(root.get("expiryDate")),
                        cb.between(root.get("expiryDate"), now, to)
                ));
            }

            return cb.and(ps.toArray(new Predicate[0]));
        };
    }

    private boolean matchStatus(InventoryDto dto, String status, int nearDays) {
        if (!StringUtils.hasText(status)) return true;

        LocalDate now = LocalDate.now();
        boolean near = dto.getNearestExpiry() != null
                && dto.getAvailableQty() > 0
                && !dto.getNearestExpiry().isAfter(now.plusDays(nearDays));

        return switch (status.toUpperCase()) {
            case "IN_STOCK" -> "Còn hàng".equals(dto.getBadge());
            case "LOW_STOCK" -> "Sắp hết hàng".equals(dto.getBadge());
            case "OUT_OF_STOCK" -> "Hết hàng".equals(dto.getBadge());
            case "DISCONTINUED" -> "Ngừng kinh doanh".equals(dto.getBadge());
            case "NEAR_EXPIRY" -> near;
            default -> true;
        };
    }

    private InventoryDto toDto(Inventory inv) {
        InventoryDto dto = new InventoryDto();
        dto.setProductId(inv.getProduct().getId());
        dto.setSku(inv.getProduct().getSku());
        dto.setProductName(inv.getProduct().getName());
        dto.setWarehouseId(inv.getWarehouse().getId());
        dto.setWarehouseName(inv.getWarehouse().getName());
        dto.setUnitName(inv.getProduct().getUnit() != null ? inv.getProduct().getUnit().getName() : null);

        long qty = Optional.ofNullable(inv.getQuantity()).orElse(0L)
                - Optional.ofNullable(inv.getReservedQuantity()).orElse(0L);
        if (qty < 0) qty = 0;
        dto.setAvailableQty(qty);

        dto.setNearestExpiry(inv.getExpiryDate());

        boolean discontinued =
                inv.getProduct().getStatus() != ProductStatus.ACTIVE
                        || inv.getWarehouse().getStatus() != CommonStatus.ACTIVE
                        || Boolean.FALSE.equals(inv.getWarehouse().getIsActive());

        String badge;
        if (discontinued)                     badge = "Ngừng kinh doanh";
        else if (qty == 0)                    badge = "Hết hàng";
        else if (qty <= Optional.ofNullable(inv.getSafetyStock()).orElse(0L))
            badge = "Sắp hết hàng";
        else                                   badge = "Còn hàng";
        dto.setBadge(badge);

        dto.setProductStatus(inv.getProduct().getStatus().name());
        dto.setWarehouseStatus(inv.getWarehouse().getStatus().name());
        return dto;
    }

    // -------- GET ----------
    @Transactional(readOnly = true)
    @Override
    public InventoryDto get(Long id) {
        var inv = inventoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));
        return toDto(inv);
    }

    // -------- CREATE ----------
    @Override
    @Transactional(readOnly = true)
    public InventoryDto create(InventoryCreateDto dto) {
        Warehouse w = warehouseRepo.findById(dto.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("Warehouse not found"));
        Product p = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Qrcode qr = null;
        if (dto.getQrId()!=null) {
            qr = qrcodeRepo.findById(dto.getQrId())
                    .orElseThrow(() -> new NotFoundException("QR not found"));
        }

        // ràng buộc 1 dòng / (warehouse, product, qr)
        if (inventoryRepo.findByWarehouse_IdAndProduct_IdAndQrcode_Id(w.getId(), p.getId(),
                qr!=null?qr.getId():null).isPresent()) {
            throw new BadRequestException("Inventory row existed. Use PUT to update.");
        }

        if (dto.getReservedQuantity()!=null && dto.getReservedQuantity() > dto.getQuantity())
            throw new BadRequestException("reservedQuantity must be <= quantity");

        Inventory inv = Inventory.builder()
                .warehouse(w)
                .product(p)
                .qrcode(qr)
                .quantity(dto.getQuantity())
                .reservedQuantity(Optional.ofNullable(dto.getReservedQuantity()).orElse(0L))
                .safetyStock(dto.getSafetyStock())
                .manufactureDate(dto.getManufactureDate())
                .expiryDate(dto.getExpiryDate())
                .lastInAt(LocalDateTime.now())
                .build();

        return toDto(inventoryRepo.save(inv));
    }

    // -------- UPDATE ----------
    @Override
    @Transactional
    public void update(Long id, InventoryUpdateDto dto) {
        Inventory inv = inventoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        if (dto.getQrId()!=null) {
            Qrcode qr = qrcodeRepo.findById(dto.getQrId())
                    .orElseThrow(() -> new NotFoundException("QR not found"));
            // check duplicate nếu đổi qr
            inventoryRepo.findByWarehouse_IdAndProduct_IdAndQrcode_Id(
                            inv.getWarehouse().getId(), inv.getProduct().getId(), qr.getId()
                    ).filter(other -> !Objects.equals(other.getId(), inv.getId()))
                    .ifPresent(x -> { throw new BadRequestException("Duplicate (warehouse, product, qr)"); });
            inv.setQrcode(qr);
        }

        if (dto.getQuantity()!=null) {
            if (dto.getReservedQuantity()!=null && dto.getReservedQuantity() > dto.getQuantity())
                throw new BadRequestException("reservedQuantity must be <= quantity");
            // nếu tăng số lượng coi như nhập
            if (inv.getQuantity()==null || dto.getQuantity() > inv.getQuantity()) inv.setLastInAt(LocalDateTime.now());
            // nếu giảm coi như xuất
            if (inv.getQuantity()!=null && dto.getQuantity() < inv.getQuantity()) inv.setLastOutAt(LocalDateTime.now());
            inv.setQuantity(dto.getQuantity());
        }
        if (dto.getReservedQuantity()!=null) {
            long q = Optional.ofNullable(inv.getQuantity()).orElse(0L);
            if (dto.getReservedQuantity() > q)
                throw new BadRequestException("reservedQuantity must be <= quantity");
            inv.setReservedQuantity(dto.getReservedQuantity());
        }
        if (dto.getSafetyStock()!=null) inv.setSafetyStock(dto.getSafetyStock());
        if (dto.getManufactureDate()!=null) inv.setManufactureDate(dto.getManufactureDate());
        if (dto.getExpiryDate()!=null) inv.setExpiryDate(dto.getExpiryDate());

        inventoryRepo.save(inv);
    }

    // -------- DELETE ----------
    @Override
    @Transactional
    public void delete(Long id) {
        Inventory inv = inventoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));
        if (Optional.ofNullable(inv.getReservedQuantity()).orElse(0L) > 0)
            throw new BadRequestException("Cannot delete: reserved quantity > 0");
        inventoryRepo.delete(inv);
    }


    // -------- DETAIL ----------
    @Override
    @Transactional(readOnly = true)
    public InventoryDetailDto getDetail(Long wid, Long pid) {
        Product p = productRepo.findById(pid).orElseThrow(() -> new NotFoundException("Product not found"));
        Warehouse w = warehouseRepo.findById(wid).orElseThrow(() -> new NotFoundException("Warehouse not found"));

        long available = Optional.ofNullable(inventoryRepo.sumAvailable(wid, pid)).orElse(0L);

        var lots = inventoryRepo.findLotsForDetail(wid, pid);
        LocalDate mfg  = lots.isEmpty() ? null : lots.get(0).getManufactureDate();
        LocalDate exp  = lots.isEmpty() ? null : lots.get(0).getExpiryDate();
        Long daysToExp = (exp == null) ? null : java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), exp);

        String supplier = null;
        var names = goodsReceiptDetailRepo.findLastSupplierName(wid, pid);
        if (!names.isEmpty()) supplier = names.get(0);

        return InventoryDetailDto.builder()
                .productId(pid).sku(p.getSku()).productName(p.getName())
                .warehouseId(wid).warehouseName(w.getName())
                .unitName(p.getUnit()!=null ? p.getUnit().getName() : null)
                .availableQty(available)
                .manufactureDate(mfg).expiryDate(exp).daysToExpire(daysToExp)
                .sellingPrice(p.getSellingPrice())
                .supplierName(supplier)
                .build();
    }

    // -------- HISTORY + EXPORT (stub) ----------
    @Override
    public Object history(Long productId, Long warehouseId) {
        return Map.of("productId", productId, "warehouseId", warehouseId, "items", List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayResource exportCsv(InventoryFilterDto f) {
        var page = listInventory(f, PageRequest.of(0, Integer.MAX_VALUE));
        StringBuilder sb = new StringBuilder("SKU,Product,Qty,Unit,Warehouse,Badge,NearestExpiry\n");
        for (var r : page.getContent()) {
            sb.append(csv(r.getSku())).append(',')
                    .append(csv(r.getProductName())).append(',')
                    .append(r.getAvailableQty()).append(',')
                    .append(csv(r.getUnitName())).append(',')
                    .append(csv(r.getWarehouseName())).append(',')
                    .append(csv(r.getBadge())).append(',')
                    .append(r.getNearestExpiry() == null ? "" : r.getNearestExpiry())
                    .append('\n');
        }
        return new ByteArrayResource(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String csv(String v){ return v==null? "": "\""+v.replace("\"","\"\"")+"\""; }
}
