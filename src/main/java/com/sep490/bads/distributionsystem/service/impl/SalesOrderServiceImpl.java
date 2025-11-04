package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.SalesOrderDto;
import com.sep490.bads.distributionsystem.dto.SalesOrderCreateDto;
import com.sep490.bads.distributionsystem.dto.SalesOrderFilterDto;
import com.sep490.bads.distributionsystem.dto.SalesOrderUpdateDto;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import com.sep490.bads.distributionsystem.mapper.SalesOrderMapper;
import com.sep490.bads.distributionsystem.repository.SalesOrderRepository;
import com.sep490.bads.distributionsystem.service.SalesOrderService;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class SalesOrderServiceImpl implements SalesOrderService {

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private SalesOrderMapper salesOrderMapper;

    @Override
    public List<SalesOrderDto> getAllOrders() {
        List<SalesOrder> entities = salesOrderRepository.findAll();
        return salesOrderMapper.toDto(entities);
    }


    @Override
    public SalesOrder findById(Long id) {
        return salesOrderRepository.findOne(buildOrderSpecification(id)).orElse(null);
    }

    @Override
    public SalesOrderDto createOrder(SalesOrderCreateDto dto) {
        SalesOrder entity = new SalesOrder();
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setNote(dto.getNote());
        entity.setStatus(SaleOderStatus.NEW);
        SalesOrder saved = salesOrderRepository.save(entity);
        return salesOrderMapper.toDto(saved);
    }

    @Override
    public void updateOrder(Long id, SalesOrderUpdateDto dto) {
        SalesOrder existing = findById(id);
        if (existing == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng ID=" + id);
        }

        // cập nhật dữ liệu cơ bản
        existing.setStatus(SaleOderStatus.valueOf(dto.getStatus()));
        existing.setPaymentMethod(dto.getPaymentMethod());
        existing.setNote(dto.getNote());

        salesOrderRepository.save(existing);
        log.info("Cập nhật đơn hàng ID={}", id);
    }

    @Override
    public void softDeleteOrder(Long id) {
        SalesOrder existing = findById(id);
        if (existing == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng ID=" + id);
        }

        existing.setStatus(SaleOderStatus.valueOf(SaleOderStatus.NEW.name()));
        salesOrderRepository.save(existing);
        log.info("Đã ẩn (soft delete) đơn hàng ID={}", id);
    }

    // Specification để tìm theo ID (hoặc lọc trạng thái khác Deleted)
    public static Specification<SalesOrder> buildOrderSpecification(Long id) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            } else {
                predicates.add(cb.notEqual(root.get("status"), "INACTIVE"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    @Override
    public Page<SalesOrderDto> filterOrders(SalesOrderFilterDto filter, Pageable pageable) {
        return salesOrderRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
                String like = "%" + filter.getKeyword().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("customer").get("name")), like));
            }
            if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getPaymentMethod() != null && !filter.getPaymentMethod().isBlank()) {
                predicates.add(cb.equal(root.get("paymentMethod"), filter.getPaymentMethod()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable).map(salesOrderMapper::toDto);
    }

}
