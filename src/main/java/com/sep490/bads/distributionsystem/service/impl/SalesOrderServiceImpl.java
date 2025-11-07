package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.salesOrderDto.*;
import com.sep490.bads.distributionsystem.entity.*;
import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import com.sep490.bads.distributionsystem.entity.type.SaleOrderDetailStatus;
import com.sep490.bads.distributionsystem.exception.BadRequestException;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.mapper.SalesOrderMapper;
import com.sep490.bads.distributionsystem.repository.*;
import com.sep490.bads.distributionsystem.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Log4j2
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository orderRepo;
    private final SalesOrderDetailRepository detailRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    // ===================== SEARCH =====================
    @Transactional(readOnly = true)
    @Override
    public Page<SalesOrderDto> search(Pageable pageable, SalesOrderFilterDto f) {
        return orderRepo.findAll(buildSpec(f), pageable).map(this::toDtoShallow);
    }

    private Specification<SalesOrder> buildSpec(SalesOrderFilterDto f) {
        return (root, q, cb) -> {
            var ps = new ArrayList<Predicate>();

            if (f.getStatus() != null)    ps.add(cb.equal(root.get("status"), f.getStatus()));
            if (StringUtils.hasText(f.getPaymentMethod()))
                ps.add(cb.equal(root.get("paymentMethod"), f.getPaymentMethod()));

            if (StringUtils.hasText(f.getKeyword())) {
                String kw = "%" + f.getKeyword().trim().toLowerCase() + "%";
                var customerJoin = root.join("customer", jakarta.persistence.criteria.JoinType.LEFT);
                ps.add(cb.or(
                        cb.like(cb.lower(root.get("saleOrderCode")), kw),
                        cb.like(cb.lower(customerJoin.get("name")), kw)
                ));
            }

            if (f.getMinTotal() != null)
                ps.add(cb.ge(root.get("totalAmount"), f.getMinTotal()));
            if (f.getMaxTotal() != null)
                ps.add(cb.le(root.get("totalAmount"), f.getMaxTotal()));

            q.orderBy("DESC".equalsIgnoreCase(f.getDirection())
                    ? cb.desc(root.get(f.getSortBy()))
                    : cb.asc(root.get(f.getSortBy()))
            );
            return cb.and(ps.toArray(new Predicate[0]));
        };
    }

    // ===================== GET =====================
    @Transactional(readOnly = true)
    @Override
    public SalesOrderDto get(Long id) {
        SalesOrder o = orderRepo.findByIdDeep(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return toDtoDeep(o);
    }

    // ===================== CREATE DRAFT =====================
    @Transactional
    @Override
    public SalesOrderDto createDraft(SalesOrderCreateDto dto, Long createdById) {
        var o = new SalesOrder();
        o.setSaleOrderCode(null);
        o.setCustomer(customerRepo.getReferenceById(dto.getCustomerId()));
        o.setUser(userRepo.getReferenceById(dto.getUserId()));
        o.setPaymentMethod(dto.getPaymentMethod());
        o.setNote(dto.getNote());
        o.setStatus(SaleOderStatus.NEW);
        o.setCreatedBy(userRepo.getReferenceById(createdById));
        o.setTotalAmount(BigDecimal.ZERO);
        o = orderRepo.save(o);

        BigDecimal total = BigDecimal.ZERO;
        for (var i : dto.getItems()) {
            var d = new SalesOrderDetail();
            d.setOrder(o);
            d.setProduct(productRepo.getReferenceById(i.getProductId()));
            d.setQuantity(nvl(i.getQuantity(), 0L));
            d.setUnitPrice(nvl(i.getUnitPrice(), BigDecimal.ZERO));
            d.setDiscount(nvl(i.getDiscount(), BigDecimal.ZERO));
            d.setVatAmount(nvl(i.getVatAmount(), BigDecimal.ZERO));
            d.setTotalPrice(calcLineTotal(d.getQuantity(), d.getUnitPrice(), d.getDiscount(), d.getVatAmount()));
            d.setStatus(SaleOrderDetailStatus.DRAFT);
            detailRepo.save(d);
            total = total.add(nvl(d.getTotalPrice(), BigDecimal.ZERO));
        }
        o.setTotalAmount(total);
        return toDtoDeep(orderRepo.findByIdDeep(o.getId()).orElse(o));
    }

    // ===================== UPDATE DRAFT =====================
    @Transactional
    @Override
    public SalesOrderDto updateDraft(Long orderId, SalesOrderUpdateDto dto) {
        var o = orderRepo.findByIdDeep(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        assertEditable(o.getStatus());

        if (dto.getCustomerId() != null) o.setCustomer(customerRepo.getReferenceById(dto.getCustomerId()));
        if (dto.getUserId() != null)     o.setUser(userRepo.getReferenceById(dto.getUserId()));
        if (dto.getPaymentMethod() != null) o.setPaymentMethod(dto.getPaymentMethod());
        if (dto.getNote() != null)          o.setNote(dto.getNote());

        // full-replace items cho đơn giản
        detailRepo.deleteAll(detailRepo.findByOrderId(orderId));

        BigDecimal total = BigDecimal.ZERO;
        if (dto.getItems() != null) {
            for (var i : dto.getItems()) {
                var d = new SalesOrderDetail();
                d.setOrder(o);
                d.setProduct(productRepo.getReferenceById(i.getProductId()));
                d.setQuantity(nvl(i.getQuantity(), 0L));
                d.setUnitPrice(nvl(i.getUnitPrice(), BigDecimal.ZERO));
                d.setDiscount(nvl(i.getDiscount(), BigDecimal.ZERO));
                d.setVatAmount(nvl(i.getVatAmount(), BigDecimal.ZERO));
                d.setTotalPrice(calcLineTotal(d.getQuantity(), d.getUnitPrice(), d.getDiscount(), d.getVatAmount()));
                d.setStatus(o.getStatus() == SaleOderStatus.PENDING ? SaleOrderDetailStatus.PENDING : SaleOrderDetailStatus.DRAFT);
                detailRepo.save(d);
                total = total.add(nvl(d.getTotalPrice(), BigDecimal.ZERO));
            }
        }
        o.setTotalAmount(total);
        return toDtoDeep(o);
    }

    // ===================== SUBMIT (-> PENDING) =====================
    @Transactional
    @Override
    public SalesOrderDto submit(Long orderId) {
        var o = orderRepo.findByIdDeep(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        assertEditable(o.getStatus());
        o.setStatus(SaleOderStatus.PENDING);
        if (o.getOrderDetails() != null) {
            o.getOrderDetails().forEach(d -> d.setStatus(SaleOrderDetailStatus.PENDING));
        }
        return toDtoDeep(o);
    }

    // ===================== CONFIRM (-> CONFIRMED) =====================
    @Transactional
    @Override
    public SalesOrderDto confirm(Long orderId) {
        var o = orderRepo.findByIdDeep(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        if (o.getStatus() != SaleOderStatus.PENDING && o.getStatus() != SaleOderStatus.NEW)
            throw new BadRequestException("Chỉ xác nhận khi NEW hoặc PENDING");

        if (o.getSaleOrderCode() == null) {
            o.setSaleOrderCode(generateOrderCode());
        }
        o.setStatus(SaleOderStatus.CONFIRMED);
        if (o.getOrderDetails() != null) {
            o.getOrderDetails().forEach(d -> d.setStatus(SaleOrderDetailStatus.CONFIRM));
        }
        return toDtoDeep(o);
    }

    // ===================== CANCEL =====================
    @Transactional
    @Override
    public void cancel(Long orderId, String reason) {
        var o = orderRepo.findByIdDeep(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        if (o.getStatus() == SaleOderStatus.DELIVERED || o.getStatus() == SaleOderStatus.COMPLETED)
            throw new BadRequestException("Không thể hủy đơn đã giao/hoàn tất");
        o.setStatus(SaleOderStatus.CANCELLED);
        if (o.getOrderDetails() != null) {
            o.getOrderDetails().forEach(d -> d.setStatus(SaleOrderDetailStatus.CANCELED));
        }
        if (reason != null && !reason.isBlank())
            o.setNote(o.getNote() == null ? ("Cancel: " + reason) : (o.getNote() + " | Cancel: " + reason));
    }

    // ===================== helpers =====================
    private void assertEditable(SaleOderStatus st) {
        if (st != SaleOderStatus.NEW && st != SaleOderStatus.PENDING)
            throw new BadRequestException("Đơn chỉ được sửa khi NEW/PENDING");
    }

    private BigDecimal calcLineTotal(Long qty, BigDecimal unit, BigDecimal discount, BigDecimal vat) {
        var q = BigDecimal.valueOf(qty == null ? 0 : qty);
        return nvl(unit, BigDecimal.ZERO).multiply(q)
                .subtract(nvl(discount, BigDecimal.ZERO))
                .add(nvl(vat, BigDecimal.ZERO));
    }
    private static BigDecimal nvl(BigDecimal v, BigDecimal def){ return v==null?def:v; }
    private static <T> T nvl(T v, T def){ return v==null?def:v; }
    private static BigDecimal toBd(Long v){ return v==null? null : BigDecimal.valueOf(v); }

    private String generateOrderCode() {
        var now = LocalDate.now();
        var from = now.atStartOfDay();
        var to = now.plusDays(1).atStartOfDay();
        long seq = orderRepo.countByCreatedAtBetween(from, to) + 1;
        return "SO" + now.format(DateTimeFormatter.BASIC_ISO_DATE) + "-" + String.format("%04d", seq);
    }

    // =========== mapping ===========
    private SalesOrderDto toDtoShallow(SalesOrder o){
        return SalesOrderDto.builder()
                .id(o.getId())
                .saleOrderCode(o.getSaleOrderCode())
                .customerId(o.getCustomer()!=null? o.getCustomer().getId(): null)
                .customerName(o.getCustomer()!=null? o.getCustomer().getName(): null)
                .userId(o.getUser()!=null? o.getUser().getId(): null)
                .userName(o.getUser()!=null? o.getUser().getFullName(): null)
                .orderDate(o.getCreatedAt()!=null? o.getCreatedAt().toLocalDate().toString() : null)
                .status(o.getStatus())
                .paymentMethod(o.getPaymentMethod())
                .note(o.getNote())
                .subTotal(BigDecimal.valueOf(o.getTotalAmount()!=null? o.getTotalAmount().longValue(): 0L))
                .discountTotal(BigDecimal.valueOf(0L))
                .grandTotal(BigDecimal.valueOf(o.getTotalAmount()!=null? o.getTotalAmount().longValue(): 0L))
                .items(null) // list items không fetch trong search
                .build();
    }

    private SalesOrderDto toDtoDeep(SalesOrder o) {
        var sub = BigDecimal.ZERO;
        var disc = BigDecimal.ZERO;
        var grand = BigDecimal.ZERO;

        var items = new ArrayList<SalesOrderItemDto>();
        if (o.getOrderDetails() != null) {
            for (var d : o.getOrderDetails()) {
                var lineSub = nvl(d.getUnitPrice(), BigDecimal.ZERO)
                        .multiply(BigDecimal.valueOf(nvl(d.getQuantity(), 0L)));
                var lineDisc = nvl(d.getDiscount(), BigDecimal.ZERO);
                var lineGrand = nvl(d.getTotalPrice(), lineSub.subtract(lineDisc)
                        .add(nvl(d.getVatAmount(), BigDecimal.ZERO)));

                sub = sub.add(lineSub);
                disc = disc.add(lineDisc);
                grand = grand.add(lineGrand);

                items.add(SalesOrderItemDto.builder()
                        .orderDetailId(d.getId())
                        .productId(d.getProduct() != null ? d.getProduct().getId() : null)
                        .productName(d.getProduct() != null ? d.getProduct().getName() : null)
                        .quantity(d.getQuantity())
                        .unitPrice(nvl(d.getUnitPrice(), BigDecimal.ZERO))
                        .discount(nvl(d.getDiscount(), BigDecimal.ZERO))
                        .vatAmount(nvl(d.getVatAmount(), BigDecimal.ZERO))
                        .totalPrice(nvl(d.getTotalPrice(), BigDecimal.ZERO))
                        .build());
            }
        }

        return SalesOrderDto.builder()
                .id(o.getId())
                .saleOrderCode(o.getSaleOrderCode())
                .customerId(o.getCustomer() != null ? o.getCustomer().getId() : null)
                .customerName(o.getCustomer() != null ? o.getCustomer().getName() : null)
                .userId(o.getUser() != null ? o.getUser().getId() : null)
                .userName(o.getUser() != null ? o.getUser().getFullName() : null)
                .orderDate(o.getCreatedAt() != null ? o.getCreatedAt().toLocalDate().toString() : null)
                .status(o.getStatus())
                .paymentMethod(o.getPaymentMethod())
                .note(o.getNote())
                .subTotal(sub)
                .discountTotal(disc)
                .grandTotal(grand)
                .items(items)
                .build();
    }
}
