package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.purchaseOrderDtos.*;
import com.sep490.bads.distributionsystem.entity.*;
import com.sep490.bads.distributionsystem.entity.type.ContractDetailStatus;
import com.sep490.bads.distributionsystem.entity.type.ContractStatus;
import com.sep490.bads.distributionsystem.exception.BadRequestException;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.repository.*;
import com.sep490.bads.distributionsystem.service.PurchaseOrderService;
import com.sep490.bads.distributionsystem.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
@Log4j2
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final ContractRepository contractRepository;
    private final ContractDetailRepository contractDetailRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public Page<PurchaseOrderListItemDto> searchPurchaseOrders(PurchaseOrderFilterDto filter, Pageable pageable) {
        Specification<Contract> spec = Specification.where(null);
        if (StringUtils.hasText(filter.getSearch())) {
            String like = "%" + filter.getSearch().trim() + "%";
            spec = spec.and((root, query, cb) -> {
                Join<Contract, Supplier> supplierJoin =
                        root.join("supplier", JoinType.LEFT);
                return cb.or(
                        cb.like(root.get("contractCode"), like),
                        cb.like(supplierJoin.get("name"), like)
                );
            });
        }

        if (filter.getSupplierId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("supplier").get("id"), filter.getSupplierId()));
        }

        if (filter.getCreatedById() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("createdBy").get("id"), filter.getCreatedById()));
        }

        if (!CollectionUtils.isEmpty(filter.getStatuses())) {
            spec = spec.and((root, query, cb) ->
                    root.get("status").in(filter.getStatuses()));
        }

        if (filter.getFromDate() != null) {
            LocalDateTime from = filter.getFromDate().atStartOfDay();
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), from));
        }

        if (filter.getToDate() != null) {
            LocalDateTime to = filter.getToDate().plusDays(1).atStartOfDay();
            spec = spec.and((root, query, cb) ->
                    cb.lessThan(root.get("createdAt"), to));
        }

        Page<Contract> page = contractRepository.findAll(spec, pageable);

        return page.map(this::mapToListItemDto);
    }

    @Override
    @Transactional
    public PurchaseOrderDetailDto createPurchaseOrder(CreatePurchaseOrderDto dto, Long currentUserId) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new NotFoundException(Constants.SUPPLIER_NOT_FOUND));

        User inCharge;
        if (dto.getInChargeId() != null) {
            inCharge = userRepository.findById(dto.getInChargeId())
                    .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
        } else {
            inCharge = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
        }
        ContractStatus status = dto.getStatus() != null
                ? dto.getStatus()
                : ContractStatus.DRAFT;
        Contract contract = Contract.builder()
                .supplier(supplier)
                .user(inCharge)
                .status(status)
                .note(dto.getNote())
                .createdBy(inCharge)
                .build();
        contract = contractRepository.save(contract);

        // mã PO-YYYY-XXXX
        String code = buildPoCode(contract.getId());
        contract.setContractCode(code);
        contract = contractRepository.save(contract);

        List<ContractDetail> details = buildDetailsFromDto(contract, dto.getItems());
        contractDetailRepository.saveAll(details);

        return mapToDetailDto(contract, details);
    }

    // ========= DETAIL =========

    @Override
    @Transactional
    public PurchaseOrderDetailDto getPurchaseOrderDetail(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.PURCHASE_ORDER_NOT_FOUND));

        List<ContractDetail> details =
                contractDetailRepository.findByContractId(id);

        return mapToDetailDto(contract, details);
    }

    @Override
    @Transactional
    public PurchaseOrderDetailDto updatePurchaseOrder(Long id, UpdatePurchaseOrderDto dto, Long currentUserId) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.PURCHASE_ORDER_NOT_FOUND));

        if (contract.getStatus() == ContractStatus.APPROVED
                || contract.getStatus() == ContractStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Không thể chỉnh sửa đơn ở trạng thái " + contract.getStatus());
        }

        contract.setNote(dto.getNote());
        if (dto.getStatus() != null) {
            contract.setStatus(dto.getStatus());
        }
        contractRepository.save(contract);

        // Xóa cũ, tạo lại chi tiết mới
        List<ContractDetail> oldDetails = contractDetailRepository.findByContractId(id);
        contractDetailRepository.deleteAll(oldDetails);

        List<ContractDetail> newDetails = buildDetailsFromDto(contract, dto.getItems());
        contractDetailRepository.saveAll(newDetails);
        return mapToDetailDto(contract, newDetails);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, ContractStatus status) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.PURCHASE_ORDER_NOT_FOUND));

        ContractStatus current = contract.getStatus();

        if (current == status) {
            return;
        }
        boolean allowed = switch (current) {
            case DRAFT -> status == ContractStatus.PENDING_APPROVAL;
            case PENDING_APPROVAL ->
                    status == ContractStatus.APPROVED
                            || status == ContractStatus.REJECTED;
            case APPROVED -> status == ContractStatus.COMPLETED;
            case REJECTED, COMPLETED -> false;   // không cho đổi nữa
        };

        if (!allowed) {
            throw new IllegalStateException(
                    "Không thể chuyển trạng thái từ " + current + " sang " + status);
        }
        contract.setStatus(status);
        contractRepository.save(contract);
    }

    @Override
    @Transactional
    public void archive(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.PURCHASE_ORDER_NOT_FOUND));

        if (contract.getStatus() != ContractStatus.COMPLETED) {
            throw new IllegalStateException("Chỉ được lưu trữ đơn Hoàn thành");
        }

        contract.setArchived(true);
        contractRepository.save(contract);
    }
    @Override
    @Transactional
    public PurchaseOrderDetailDto duplicate(Long id, Long currentUserId) {
        Contract source = contractRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.PURCHASE_ORDER_NOT_FOUND));

        if (source.getStatus() != ContractStatus.COMPLETED) {
            throw new IllegalStateException("Chỉ tạo bản sao từ đơn Hoàn thành");
        }

        User creator = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));

        Contract copy = Contract.builder()
                .supplier(source.getSupplier())
                .user(source.getUser())
                .status(ContractStatus.DRAFT)
                .note(source.getNote())
                .createdBy(creator)
                .archived(false)
                .build();

        copy = contractRepository.save(copy);

        copy.setContractCode(buildPoCode(copy.getId()));
        copy = contractRepository.save(copy);

        List<ContractDetail> sourceDetails =
                contractDetailRepository.findByContractId(id);
        List<ContractDetail> newDetails = new ArrayList<>();

        for (ContractDetail d : sourceDetails) {
            ContractDetail nd = ContractDetail.builder()
                    .contract(copy)
                    .product(d.getProduct())
                    .quantity(d.getQuantity())
                    .unitPrice(d.getUnitPrice())
                    .vatAmount(d.getVatAmount())
                    .estimatedDeliveryDate(d.getEstimatedDeliveryDate())
                    .status(ContractDetailStatus.DRAFT)
                    .build();
            newDetails.add(nd);
        }

        contractDetailRepository.saveAll(newDetails);

        return mapToDetailDto(copy, newDetails);
    }

    @Override
    public void sendToSupplier(Long id) {
        Contract contract = findApprovedContract(id); // check status == APPROVED
        // 1. Generate nội dung (PDF / HTML)
        // 2. Lấy email / zalo ID của NCC
        // 3. Gửi qua email service / Zalo OA
        // 4. Lưu lại cờ: đã gửi cho NCC
        Supplier supplier = contract.getSupplier();
        if (supplier == null) {
            throw new BadRequestException("Đơn mua hàng không có nhà cung cấp");
        }

        String email = supplier.getEmail();
        if (!StringUtils.hasText(email)) {
            throw new BadRequestException("Nhà cung cấp chưa có email, không thể gửi PO");
        }

        // TODO: ở đây bạn gọi EmailService/ZaloService thực tế
        // emailService.sendPurchaseOrder(email, contract, details, ...);

        log.info("Sending purchase order {} to supplier {} <{}>",
                contract.getContractCode(), supplier.getName(), email);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generatePdf(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.PURCHASE_ORDER_NOT_FOUND));

        List<ContractDetail> details =
                contractDetailRepository.findByContractId(id);

        StringBuilder sb = new StringBuilder();
        sb.append("PURCHASE ORDER ").append(contract.getContractCode()).append("\n\n");

        Supplier s = contract.getSupplier();
        if (s != null) {
            sb.append("Supplier: ").append(s.getName()).append("\n");
            sb.append("Address : ").append(s.getAddress()).append("\n");
            sb.append("Phone   : ").append(s.getPhone()).append("\n");
            sb.append("Tax code: ").append(s.getTaxCode()).append("\n\n");
        }

        sb.append("Items:\n");
        sb.append(String.format("%-5s %-30s %-8s %-12s %-12s\n",
                "No", "Product", "Qty", "UnitPrice", "LineTotal"));

        int index = 1;
        BigDecimal totalGoods = BigDecimal.ZERO;
        BigDecimal totalVat = BigDecimal.ZERO;

        for (ContractDetail d : details) {
            BigDecimal base = d.getUnitPrice()
                    .multiply(BigDecimal.valueOf(d.getQuantity()));
            BigDecimal vat = d.getVatAmount() != null ? d.getVatAmount() : BigDecimal.ZERO;
            BigDecimal lineTotal = base.add(vat);

            totalGoods = totalGoods.add(base);
            totalVat = totalVat.add(vat);

            sb.append(String.format("%-5d %-30s %-8d %-12s %-12s\n",
                    index++,
                    d.getProduct().getName(),
                    d.getQuantity(),
                    d.getUnitPrice(),
                    lineTotal));
        }

        sb.append("\nSubtotal: ").append(totalGoods).append("\n");
        sb.append("VAT     : ").append(totalVat).append("\n");
        sb.append("Grand   : ").append(totalGoods.add(totalVat)).append("\n");

        return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    private Contract findApprovedContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.PURCHASE_ORDER_NOT_FOUND));

        if (contract.getStatus() != ContractStatus.APPROVED) {
            throw new BadRequestException("Chỉ gửi cho NCC khi đơn ở trạng thái Đã phê duyệt");
        }
        return contract;
    }

    private String buildPoCode(Long id) {
        int year = LocalDate.now().getYear();
        return String.format("PO-%d-%04d", year, id);
    }

    private List<ContractDetail> buildDetailsFromDto(Contract contract, List<PurchaseOrderItemDto> items) {
        List<ContractDetail> details = new ArrayList<>();
        if (items == null) return details;

        for (PurchaseOrderItemDto item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException(Constants.PRODUCT_NOT_FOUND));

            ContractDetail detail = ContractDetail.builder()
                    .contract(contract)
                    .product(product)
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .vatAmount(item.getVatAmount())
                    .estimatedDeliveryDate(item.getEstimatedDeliveryDate())
                    .status(ContractDetailStatus.DRAFT)
                    .build();

            details.add(detail);
        }
        return details;
    }

    private PurchaseOrderListItemDto mapToListItemDto(Contract contract) {
        BigDecimal total = contractDetailRepository.calculateTotalAmount(contract.getId());

        String createdAtStr = null;
        if (contract.getCreatedAt() != null) {
            createdAtStr = contract.getCreatedAt().toLocalDate().format(DATE_FORMATTER);
        }

        String contactPhone = null;
        if (contract.getSupplier() != null) {
            contactPhone = contract.getSupplier().getPhone();
        }

        return PurchaseOrderListItemDto.builder()
                .id(contract.getId())
                .code(contract.getContractCode())
                .supplierName(contract.getSupplier() != null ? contract.getSupplier().getName() : null)
                .contactPhone(contactPhone)
                .inChargeName(contract.getUser() != null ? contract.getUser().getFullName() : null)
                .createdAt(createdAtStr)
                .totalAmount(total)
                .status(contract.getStatus())
                .archived(Boolean.TRUE.equals(contract.getArchived()))
                .build();
    }


    private PurchaseOrderDetailDto mapToDetailDto(Contract contract, List<ContractDetail> details) {
        List<PurchaseOrderItemDto> itemDtos = new ArrayList<>();
        BigDecimal totalGoods = BigDecimal.ZERO;
        BigDecimal totalVat   = BigDecimal.ZERO;

        if (details != null) {
            for (ContractDetail d : details) {
                BigDecimal base = d.getUnitPrice()
                        .multiply(BigDecimal.valueOf(d.getQuantity()));
                BigDecimal vat  = d.getVatAmount() != null ? d.getVatAmount() : BigDecimal.ZERO;
                BigDecimal lineTotal = base.add(vat);

                totalGoods = totalGoods.add(base);
                totalVat   = totalVat.add(vat);

                PurchaseOrderItemDto itemDto = new PurchaseOrderItemDto(
                        d.getProduct().getId(),
                        d.getProduct().getName(),
                        d.getQuantity(),
                        d.getUnitPrice(),
                        d.getVatAmount(),
                        lineTotal,
                        d.getEstimatedDeliveryDate()
                );
                itemDtos.add(itemDto);
            }
        }

        String createdAtStr = null;
        if (contract.getCreatedAt() != null) {
            createdAtStr = contract.getCreatedAt().toLocalDate().format(DATE_FORMATTER);
        }

        Supplier s = contract.getSupplier();

        return PurchaseOrderDetailDto.builder()
                .id(contract.getId())
                .code(contract.getContractCode())
                .createdAt(createdAtStr)
                .supplierId(s != null ? s.getId() : null)
                .supplierName(s != null ? s.getName() : null)
                .supplierAddress(s != null ? s.getAddress() : null)
                .supplierContactName(s != null ? s.getContactName() : null)
                .supplierPhone(s != null ? s.getPhone() : null)
                .supplierTaxCode(s != null ? s.getTaxCode() : null)
                .inChargeId(contract.getUser() != null ? contract.getUser().getId() : null)
                .inChargeName(contract.getUser() != null ? contract.getUser().getFullName() : null)
                .note(contract.getNote())
                .status(contract.getStatus())
                .archived(Boolean.TRUE.equals(contract.getArchived()))
                .items(itemDtos)
                .totalGoodsAmount(totalGoods)
                .totalVatAmount(totalVat)
                .grandTotal(totalGoods.add(totalVat))
                .build();
    }

}
