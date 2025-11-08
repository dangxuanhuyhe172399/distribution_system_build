package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.PickGoodsDto.PickGoodsDetailDto;
import com.sep490.bads.distributionsystem.dto.PickGoodsDto.PickGoodsHeaderDto;
import com.sep490.bads.distributionsystem.dto.PickGoodsDto.PickGoodsLineDto;
import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseCreateDto;
import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseDto;
import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseFilterDto;
import com.sep490.bads.distributionsystem.dto.warehouseDto.WarehouseUpdateDto;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.Warehouse;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import com.sep490.bads.distributionsystem.entity.type.StockNoteStatus;
import com.sep490.bads.distributionsystem.exception.BadRequestException;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.mapper.WarehouseMapper;
import com.sep490.bads.distributionsystem.repository.SalesOrderDetailRepository;
import com.sep490.bads.distributionsystem.repository.SalesOrderRepository;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.repository.WarehouseRepository;
import com.sep490.bads.distributionsystem.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository repo;
    private final UserRepository userRepo;
    private final WarehouseMapper mapper;
    private final SalesOrderDetailRepository sodRepo;
    private final SalesOrderRepository orderRepo;

    @Override
    public Page<WarehouseDto> search(Pageable pageable, WarehouseFilterDto f) {
        return repo.findAll(buildSpec(f), pageable).map(mapper::toDto);
    }

    private Specification<Warehouse> buildSpec(WarehouseFilterDto f) {
        return (root, q, cb) -> {
            var ps = new ArrayList<Predicate>();
            if (f.getStatus()!=null)   ps.add(cb.equal(root.get("status"), f.getStatus()));
            if (f.getIsActive()!=null) ps.add(cb.equal(root.get("isActive"), f.getIsActive()));
            if (f.getManagerId()!=null)ps.add(cb.equal(root.join("manager").get("id"), f.getManagerId()));
            if (StringUtils.hasText(f.getQ())) {
                String kw = "%" + f.getQ().trim().toLowerCase() + "%";
                ps.add(cb.or(
                        cb.like(cb.lower(root.get("code")), kw),
                        cb.like(cb.lower(root.get("name")), kw),
                        cb.like(cb.lower(root.get("address")), kw)
                ));
            }
            return cb.and(ps.toArray(new Predicate[0]));
        };
    }

    @Override
    public WarehouseDto get(Long id) {
        return mapper.toDto(find(id));
    }

    private Warehouse find(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Warehouse not found"));
    }

    @Override
    @Transactional
    public WarehouseDto create(WarehouseCreateDto dto) {
        if (repo.existsByCode(dto.getCode()))
            throw new BadRequestException("Code existed");
        Warehouse w = Warehouse.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .isActive(Objects.requireNonNullElse(dto.getIsActive(), true))
                .status(dto.getStatus()==null? CommonStatus.ACTIVE : dto.getStatus())
                .build();
        if (dto.getManagerId()!=null) {
            User m = userRepo.findById(dto.getManagerId())
                    .orElseThrow(() -> new NotFoundException("Manager not found"));
            w.setManager(m);
        }
        return mapper.toDto(repo.save(w));
    }

    @Override
    @Transactional
    public void update(Long id, WarehouseUpdateDto dto) {
        Warehouse w = find(id);
        if (StringUtils.hasText(dto.getCode()) && !dto.getCode().equals(w.getCode())
                && repo.existsByCodeAndIdNot(dto.getCode(), id))
            throw new BadRequestException("Code existed");

        if (dto.getCode()!=null)     w.setCode(dto.getCode());
        if (dto.getName()!=null)     w.setName(dto.getName());
        if (dto.getAddress()!=null)  w.setAddress(dto.getAddress());
        if (dto.getPhone()!=null)    w.setPhone(dto.getPhone());
        if (dto.getEmail()!=null)    w.setEmail(dto.getEmail());
        if (dto.getIsActive()!=null) w.setIsActive(dto.getIsActive());
        if (dto.getStatus()!=null)   w.setStatus(dto.getStatus());

        if (dto.getManagerId()!=null) {
            User m = userRepo.findById(dto.getManagerId())
                    .orElseThrow(() -> new NotFoundException("Manager not found"));
            w.setManager(m);
        }
        repo.save(w);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Warehouse w = find(id);
        w.setIsActive(false);
        w.setStatus(CommonStatus.INACTIVE);
        repo.save(w);
    }

    @Override
    @Transactional public void activate(Long id)   { setActive(id, true); }

    @Override
    @Transactional public void deactivate(Long id) { setActive(id, false); }

    private void setActive(Long id, boolean active) {
        Warehouse w = find(id);
        w.setIsActive(active);
        w.setStatus(active ? CommonStatus.ACTIVE : CommonStatus.INACTIVE);
        repo.save(w);
    }

    @Override
    @Transactional(readOnly = true)
    public PickGoodsDetailDto getPickDetailByOrder(Long orderId){
        var o = orderRepo.findById(orderId).orElseThrow();

        var lines = sodRepo.findAllWithProductByOrderId(orderId).stream().map(d ->
                PickGoodsLineDto.builder()
                        .sku(d.getProduct().getSku())
                        .productName(d.getProduct().getName())
                        .unitName(d.getProduct().getUnit()!=null ? d.getProduct().getUnit().getName() : null)
                        .quantity(Optional.ofNullable(d.getQuantity()).orElse(0L))
                        .unitPrice(d.getUnitPrice())
                        .lineTotal(d.getUnitPrice()!=null
                                ? d.getUnitPrice().multiply(BigDecimal.valueOf(Optional.ofNullable(d.getQuantity()).orElse(0L)))
                                : BigDecimal.ZERO)
                        .build()
        ).toList();

        var header = PickGoodsHeaderDto.builder()
                .orderCode(o.getSaleOrderCode())
                .customerName(o.getCustomer()!=null ? o.getCustomer().getName() : null)
                .email(o.getCustomer()!=null ? o.getCustomer().getEmail() : null)
                .phone(o.getCustomer()!=null ? o.getCustomer().getPhone() : null)
                .taxCode(o.getCustomer()!=null ? o.getCustomer().getTaxCode() : null)
                .address(o.getCustomer()!=null ? o.getCustomer().getAddress() : null)
                .paymentMethod(String.valueOf(o.getPaymentMethod()))
                .sellerName(o.getUser()!=null ? o.getUser().getFullName() : null)
                .orderDate(o.getCreatedAt()!=null ? o.getCreatedAt().toLocalDate() : null)
                .status(mapToStockNoteStatus(o.getStatus()))
                .build();

        return PickGoodsDetailDto.builder()
                .header(header)
                .lines(lines)
                .build();
    }
    private StockNoteStatus mapToStockNoteStatus(SaleOderStatus salesStatus) {
        return switch (salesStatus) {
            case NEW, PENDING -> StockNoteStatus.WAITING;
            case CONFIRMED    -> StockNoteStatus.CONFIRMED;
            case DELIVERED, COMPLETED, POSTED -> StockNoteStatus.POSTED;
            default -> StockNoteStatus.WAITING;
        };
    }
}
