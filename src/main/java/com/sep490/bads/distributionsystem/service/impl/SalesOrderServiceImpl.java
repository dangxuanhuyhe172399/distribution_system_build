//package com.sep490.bads.distributionsystem.service.impl;
//
//import com.sep490.bads.distributionsystem.dto.*;
//import com.sep490.bads.distributionsystem.entity.*;
//import com.sep490.bads.distributionsystem.mapper.SalesOrderMapper;
//import com.sep490.bads.distributionsystem.repository.*;
//import com.sep490.bads.distributionsystem.service.SalesOrderService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.data.domain.*;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//@Log4j2
//public class SalesOrderServiceImpl implements SalesOrderService {
//
//    private final SalesOrderRepository salesOrderRepo;
//    private final SalesOrderDetailRepository detailRepo;
//    private final CustomerRepository customerRepo;
//    private final ProductRepository productRepo;
//    private final SalesOrderMapper orderMapper;
//
//    @Override
//    public SalesOrderDto createOrder(SalesOrderCreateDto dto) {
//        // Kiểm tra khách hàng
//        Customer customer = customerRepo.findById(dto.getCustomerId())
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng ID=" + dto.getCustomerId()));
//
//        // Tạo đơn hàng mới
//        SalesOrder order = new SalesOrder();
//        order.setCustomer(customer);
//        order.setUserId(dto.getUserId());
//        order.setPaymentMethod(dto.getPaymentMethod());
//        order.setNote(dto.getNote());
//        order.setStatus("PENDING");
//        order.setCreatedAt(new Date());
//        order.setCreatedBy(dto.getUserId());
//
//        // Lưu tạm đơn để có ID
//        SalesOrder saved = salesOrderRepo.save(order);
//
//        // Lưu danh sách chi tiết đơn
//        List<SalesOrderDetail> details = dto.getItems().stream().map(item -> {
//            Product product = productRepo.findById(item.getProductId())
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID=" + item.getProductId()));
//
//            SalesOrderDetail d = new SalesOrderDetail();
//            d.setOrder(saved);
//            d.setProduct(product);
//            d.setQuantity(item.getQuantity().intValue());
//            d.setUnitPrice(item.getUnitPrice().doubleValue());
//            d.setDiscount(item.getDiscount() == null ? 0.0 : item.getDiscount().doubleValue());
//            return d;
//        }).collect(Collectors.toList());
//
//        detailRepo.saveAll(details);
//
//        log.info("Đã tạo đơn hàng ID={} cho khách hàng {}", saved.getId(), customer.getName());
//        return orderMapper.toDto(saved, details);
//    }
//
//    @Override
//    public SalesOrderDto getOrderById(Long id) {
//        SalesOrder order = salesOrderRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID=" + id));
//        List<SalesOrderDetail> details = detailRepo.findByOrderId(id);
//        return orderMapper.toDto(order, details);
//    }
//
//    @Override
//    public SalesOrderDto updateStatus(Long id, SalesOrderStatusUpdateDto dto) {
//        SalesOrder order = salesOrderRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID=" + id));
//
//        order.setStatus(dto.getStatus().toUpperCase());
//        order.setUpdatedAt(new Date());
//        SalesOrder updated = salesOrderRepo.save(order);
//
//        log.info("Đã cập nhật trạng thái đơn hàng ID={} -> {}", id, dto.getStatus());
//        List<SalesOrderDetail> details = detailRepo.findByOrderId(id);
//        return orderMapper.toDto(updated, details);
//    }
//
//    @Override
//    public void softDeleteOrder(Long id) {
//        SalesOrder order = salesOrderRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID=" + id));
//
//        order.setStatus("INACTIVE");
//        salesOrderRepo.save(order);
//
//        log.info("Đã ẩn (soft delete) đơn hàng ID={}", id);
//    }
//
//    @Override
//    public Page<SalesOrderDto> filterOrders(SalesOrderFilterDto f) {
//        Sort.Direction dir = "DESC".equalsIgnoreCase(f.getDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Pageable pageable = PageRequest.of(f.getPage(), f.getSize(), Sort.by(dir, f.getSortBy()));
//
//        return salesOrderRepo.findAll(SalesOrderRepository.specFrom(f), pageable)
//                .map(order -> {
//                    List<SalesOrderDetail> details = detailRepo.findByOrderId(order.getId());
//                    return orderMapper.toDto(order, details);
//                });
//    }
//
//
//    @Override
//    public List<SalesOrderDto> getAllOrders() {
//        return salesOrderRepo.findAll().stream()
//                .map(order -> {
//                    List<SalesOrderDetail> details = detailRepo.findByOrderId(order.getId());
//                    return orderMapper.toDto(order, details);
//                })
//                .collect(Collectors.toList());
//    }
//}
