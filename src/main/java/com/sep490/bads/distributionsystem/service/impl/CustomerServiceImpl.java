package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.customerDtos.*;
import com.sep490.bads.distributionsystem.entity.*;
import com.sep490.bads.distributionsystem.entity.type.CustomerStatus;
import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import com.sep490.bads.distributionsystem.exception.BadRequestException;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.repository.*;
import com.sep490.bads.distributionsystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepo;
    private final CustomerTypeRepository typeRepo;
    private final SalesOrderRepository orderRepo;
    private final UserRepository userRepo;

    // ---------- SEARCH ----------
    @Transactional(readOnly = true)
    @Override
    public Page<CustomersDto> search(Pageable pageable, CustomerFilterDto f) {
        return customerRepo.findAll(buildSpec(f), pageable)
                .map(this::toDto);
    }

    private Specification<Customer> buildSpec(CustomerFilterDto f) {
        return (root, q, cb) -> {
            var ps = new ArrayList<Predicate>();

            // exclude DELETED in list
            ps.add(cb.notEqual(root.get("status"), CustomerStatus.DELETED));

            if (f.getStatus() != null) {
                ps.add(cb.equal(root.get("status"), f.getStatus()));
            }
            var tp = root.join("type", jakarta.persistence.criteria.JoinType.LEFT);
            if (f.getCustomerTypeId() != null) ps.add(cb.equal(tp.get("id"), f.getCustomerTypeId()));
            if (StringUtils.hasText(f.getQ())) {
                String kw = "%" + f.getQ().trim().toLowerCase() + "%";
                ps.add(cb.or(
                        cb.like(cb.lower(root.get("code")), kw),
                        cb.like(cb.lower(root.get("name")), kw),
                        cb.like(cb.lower(root.get("phone")), kw),
                        cb.like(cb.lower(root.get("email")), kw),
                        cb.like(cb.lower(root.get("taxCode")), kw),
                        cb.like(cb.lower(root.get("address")), kw),
                        cb.like(cb.lower(root.get("district")), kw),
                        cb.like(cb.lower(root.get("province")), kw),
                        cb.like(cb.lower(tp.get("name")), kw)
                ));
            }
            if (f.getMinBalance() != null) {
                ps.add(cb.ge(root.get("currentBalance"),
                        BigDecimal.valueOf(f.getMinBalance())));
            }
            if (f.getMaxBalance() != null) {
                ps.add(cb.le(root.get("currentBalance"),
                        BigDecimal.valueOf(f.getMaxBalance())));
            }

            // sort
            q.orderBy("DESC".equalsIgnoreCase(f.getDirection())
                    ? cb.desc(root.get(f.getSortBy()))
                    : cb.asc(root.get(f.getSortBy())));

            return cb.and(ps.toArray(new Predicate[0]));
        };
    }

    // ---------- GET ----------
    @Transactional(readOnly = true)
    @Override
    public CustomersDto get(Long id) {
        var c = customerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        return toDto(c);
    }

    // ---------- CREATE ----------
    @Transactional
    @Override
    public CustomersDto create(CustomerCreateDto dto, Long createdById) {
        validateUnique(dto.getPhone(), dto.getEmail(), null);

        var c = new Customer();
        c.setCode(generateCustomerCode());
        c.setName(dto.getName());
        c.setPhone(dto.getPhone());
        c.setEmail(dto.getEmail());
        c.setTaxCode(dto.getTaxCode());
        c.setAddress(dto.getAddress());
        c.setDistrict(dto.getDistrict());
        c.setProvince(dto.getProvince());
        c.setNote(dto.getNote());

        if (dto.getCustomerTypeId() != null) {
            c.setType(typeRepo.findById(dto.getCustomerTypeId())
                    .orElseThrow(() -> new NotFoundException("CustomerType not found")));
        }

        c.setBalanceLimit(nvl(dto.getBalanceLimit(), BigDecimal.ZERO));
        c.setCurrentBalance(nvl(dto.getCurrentBalance(), BigDecimal.ZERO));
        c.setStatus(dto.getStatus() != null ? dto.getStatus() : CustomerStatus.ACTIVE);
        if (createdById != null) c.setCreatedBy(userRepo.getReferenceById(createdById));

        c = customerRepo.save(c);
        return toDto(c);
    }

    // ---------- UPDATE ----------
    @Transactional
    @Override
    public CustomersDto update(Long id, CustomerUpdateDto dto) {
        var c = customerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        validateUnique(dto.getPhone(), dto.getEmail(), c.getId());

        if (dto.getName() != null)      c.setName(dto.getName());
        if (dto.getPhone() != null)     c.setPhone(dto.getPhone());
        if (dto.getEmail() != null)     c.setEmail(dto.getEmail());
        if (dto.getTaxCode() != null)   c.setTaxCode(dto.getTaxCode());
        if (dto.getAddress() != null)   c.setAddress(dto.getAddress());
        if (dto.getDistrict() != null)  c.setDistrict(dto.getDistrict());
        if (dto.getProvince() != null)  c.setProvince(dto.getProvince());
        if (dto.getNote() != null)      c.setNote(dto.getNote());

        if (dto.getCustomerTypeId() != null) {
            c.setType(typeRepo.findById(dto.getCustomerTypeId())
                    .orElseThrow(() -> new NotFoundException("CustomerType not found")));
        }
        if (dto.getBalanceLimit() != null)  c.setBalanceLimit(dto.getBalanceLimit());
        if (dto.getCurrentBalance() != null) c.setCurrentBalance(dto.getCurrentBalance());
        if (dto.getStatus() != null)        c.setStatus(dto.getStatus());

        return toDto(c);
    }

    // ---------- SOFT DELETE ----------
    @Transactional
    @Override
    public void activate(Long id) {
        var c = customerRepo.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
        c.setStatus(CustomerStatus.ACTIVE);
    }

    @Transactional
    @Override
    public void deactivate(Long id) {
        var c = customerRepo.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
        c.setStatus(CustomerStatus.INACTIVE);
    }

    @Transactional
    @Override
    public void softDelete(Long id) {
        var c = customerRepo.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
        c.setStatus(CustomerStatus.DELETED);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerInsightDto getInsight(Long customerId, int limit) {
        var c = customerRepo.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        BigDecimal totalPurchased = orderRepo.sumTotalByCustomer(customerId);
        long orderCount = orderRepo.countActiveByCustomer(customerId);

        var recent = orderRepo.findByCustomer_IdOrderByCreatedAtDesc(
                customerId, PageRequest.of(0, Math.max(1, limit))
        ).getContent();

        var fmt = DateTimeFormatter.ISO_LOCAL_DATE;

        var rows = recent.stream().map(o ->
                CustomerOrderSummaryDto.builder()
                        .orderId(o.getId())
                        .orderCode(o.getSaleOrderCode())
                        .orderDate(o.getCreatedAt() != null ? o.getCreatedAt().toLocalDate().format(fmt) : null)
                        .totalAmount(o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
                        .paidAmount(resolvePaidAmount(o))
                        .status(o.getStatus())
                        .build()
        ).toList();

        return CustomerInsightDto.builder()
                .totalPurchased(totalPurchased != null ? totalPurchased : BigDecimal.ZERO)
                .orderCount(orderCount)
                .currentBalance(c.getCurrentBalance() != null ? c.getCurrentBalance() : BigDecimal.ZERO)
                .recentOrders(rows)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerSuggestionDto> getRecentCustomers(int limit) {
        int size = Math.max(1, limit);

        var page = customerRepo.findRecentCustomers(PageRequest.of(0, size));
        var fmt  = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        return page.getContent().stream()
                .map(row -> {
                    Customer c = (Customer) row[0];
                    LocalDateTime lastOrderAt = (LocalDateTime) row[1];

                    return CustomerSuggestionDto.builder()
                            .id(c.getId())
                            .code(c.getCode())
                            .name(c.getName())
                            .phone(c.getPhone())
                            .address(c.getAddress())
                            .district(c.getDistrict())
                            .province(c.getProvince())
                            .customerTypeName(
                                    c.getType() != null ? c.getType().getName() : null
                            )
                            .currentBalance(nvl(c.getCurrentBalance(), BigDecimal.ZERO))
                            .lastOrderAt(lastOrderAt != null ? lastOrderAt.format(fmt) : null)
                            .build();
                })
                .toList();
    }

    // ---------- helpers ----------
    private void validateUnique(String phone, String email, Long exceptId) {
        if (StringUtils.hasText(phone)) {
            boolean dup = exceptId == null
                    ? customerRepo.existsByPhone(phone)
                    : customerRepo.existsByPhoneAndIdNot(phone, exceptId);
            if (dup) throw new BadRequestException("Phone already exists");
        }
        if (StringUtils.hasText(email)) {
            boolean dup = exceptId == null
                    ? customerRepo.existsByEmail(email)
                    : customerRepo.existsByEmailAndIdNot(email, exceptId);
            if (dup) throw new BadRequestException("Email already exists");
        }
    }

    private BigDecimal resolvePaidAmount(SalesOrder o) {
        return (o.getInvoice() != null && o.getInvoice().getGrandTotal() != null)
                ? o.getInvoice().getGrandTotal()
                : BigDecimal.ZERO;
    }

    private static BigDecimal nvl(BigDecimal v, BigDecimal def){ return v==null?def:v; }

    private String generateCustomerCode() {
        var today = LocalDate.now();
        var start = today.atStartOfDay();
        var end   = today.plusDays(1).atStartOfDay();
        long seq = customerRepo.countByCreatedAtBetween(start, end) + 1;
        return "KH" + today.format(DateTimeFormatter.BASIC_ISO_DATE) + "-" + String.format("%04d", seq);
    }

    private CustomersDto toDto(Customer c){
        return CustomersDto.builder()
                .id(c.getId())
                .code(c.getCode())
                .name(c.getName())
                .phone(c.getPhone())
                .email(c.getEmail())
                .taxCode(c.getTaxCode())
                .address(c.getAddress())
                .district(c.getDistrict())
                .province(c.getProvince())
                .customerTypeId(c.getType()!=null? c.getType().getId(): null)
                .customerTypeName(c.getType()!=null? c.getType().getName(): null)
                .balanceLimit(nvl(c.getBalanceLimit(), BigDecimal.ZERO))
                .currentBalance(nvl(c.getCurrentBalance(), BigDecimal.ZERO))
                .status(c.getStatus())
                .note(c.getNote())
                .build();
    }
}