package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.entity.*;
import com.sep490.bads.distributionsystem.mapper.GoodsReceiptMapper;
import com.sep490.bads.distributionsystem.repository.*;
import com.sep490.bads.distributionsystem.service.GoodsReceiptService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GoodsReceiptServiceImpl implements GoodsReceiptService {

    private final GoodsReceiptRepository receiptRepo;
    private final GoodsReceiptDetailRepository detailRepo;
//    private final WarehouseRepository warehouseRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
//    private final ContractRepository contractRepo;
    private final GoodsReceiptMapper mapper;

    private String generateCode() {
        return "GR-" + (100000 + new Random().nextInt(900000));
    }

    @Override
    public Page<GoodsReceiptDto> search(Pageable pageable, GoodsReceiptFilterDto f) {
        return receiptRepo.findAll(buildSpec(f), pageable).map(mapper::toDto);
    }

    @Override
    public GoodsReceiptDto findById(Long id) {
        return null;
    }

    @Override
    public GoodsReceiptDto create(GoodsReceiptCreateDto dto, Long userId) {
        return null;
    }

    @Override
    public void postReceipt(Long id, Long userId) {

    }

    private Specification<GoodsReceipt> buildSpec(GoodsReceiptFilterDto f) {
        return (root, q, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            if (f.getStatus() != null)
                ps.add(cb.equal(root.get("status"), f.getStatus()));
            if (f.getFrom() != null)
                ps.add(cb.greaterThanOrEqualTo(root.get("createdAt"), f.getFrom().atStartOfDay()));
            if (f.getTo() != null)
                ps.add(cb.lessThan(root.get("createdAt"), f.getTo().plusDays(1).atStartOfDay()));
            if (f.getQ() != null && !f.getQ().isBlank()) {
                String like = "%" + f.getQ().toLowerCase() + "%";
                ps.add(cb.like(cb.lower(root.get("receiptCode")), like));
            }
            return cb.and(ps.toArray(new Predicate[0]));
        };
    }

//    @Override
//    @Transactional
//    public GoodsReceiptDto create(GoodsReceiptCreateDto dto, Long userId) {
//        var user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
//        var warehouse = warehouseRepo.findById(dto.getWarehouseId()).orElseThrow(() -> new NotFoundException("Warehouse not found"));
//        var contract = dto.getContractId() != null ? contractRepo.findById(dto.getContractId()).orElse(null) : null;
//
//        GoodsReceipt receipt = GoodsReceipt.builder()
//                .receiptCode(generateCode())
//                .warehouse(warehouse)
//                .contract(contract)
//                .createdAt(LocalDateTime.now())
//                .createdBy(user)
//                .status(CommonStatus.DRAFT)
//                .note(dto.getNote())
//                .build();
//
//        receipt = receiptRepo.save(receipt);
//
//        if (dto.getDetails() != null) {
//            for (var d : dto.getDetails()) {
//                var product = productRepo.findById(d.getProductId()).orElseThrow(() -> new NotFoundException("Product not found"));
//                GoodsReceiptDetail detail = GoodsReceiptDetail.builder()
//                        .receipt(receipt)
//                        .product(product)
//                        .quantity(d.getQuantity())
//                        .manufactureDate(d.getManufactureDate())
//                        .expiryDate(d.getExpiryDate())
//                        .status(CommonStatus.ACTIVE)
//                        .note(d.getNote())
//                        .build();
//                detailRepo.save(detail);
//            }
//        }
//
//        return mapper.toDto(receipt);
//    }
//
//    @Override
//    @Transactional
//    public void postReceipt(Long id, Long userId) {
//        var receipt = receiptRepo.findById(id).orElseThrow(() -> new NotFoundException("Receipt not found"));
//        var user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
//        if (receipt.getStatus() == CommonStatus.POSTED)
//            throw new BadRequestException("Receipt already posted");
//
//        receipt.setStatus(CommonStatus.POSTED);
//        receipt.setPostedBy(user);
//        receipt.setPostedAt(LocalDateTime.now());
//        receiptRepo.save(receipt);
//    }
//
//    @Override
//    public GoodsReceiptDto findById(Long id) {
//        return mapper.toDto(receiptRepo.findById(id).orElseThrow(() -> new NotFoundException("Not found")));
//    }
}
