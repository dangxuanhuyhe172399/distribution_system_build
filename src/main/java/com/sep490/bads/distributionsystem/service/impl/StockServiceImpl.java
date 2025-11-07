package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.stockDto.CreateIssueDto;
import com.sep490.bads.distributionsystem.dto.stockDto.CreateReceiptDto;
import com.sep490.bads.distributionsystem.entity.*;
import com.sep490.bads.distributionsystem.entity.type.StockNoteStatus;
import com.sep490.bads.distributionsystem.exception.BadRequestException;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.repository.*;
import com.sep490.bads.distributionsystem.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class StockServiceImpl implements StockService {
    private final InventoryRepository invRepo;
    private final GoodsReceiptRepository grRepo;
    private final GoodsReceiptDetailRepository grdRepo;
    private final GoodsIssuesRepository giRepo;
    private final GoodsIssuesDetailRepository gidRepo;
    private final WarehouseRepository whRepo;
    private final ProductRepository productRepo;
    private final QrcodeRepository qrRepo;
    private final UserRepository userRepo;

    // ======== NHẬP KHO ========
    @Override
    @Transactional
    public GoodsReceipt createAndPost(CreateReceiptDto dto, Long userId) {
        Warehouse w = whRepo.findById(dto.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("Kho không tồn tại"));
        User creator = userRepo.getReferenceById(userId);

        GoodsReceipt gr = new GoodsReceipt();
        gr.setWarehouse(w);
        gr.setStatus(StockNoteStatus.DRAFT);
        gr.setCreatedBy(creator);
        gr.setNote(dto.getNote());
        gr = grRepo.save(gr);

        for (var l : dto.getLines()) {
            Product p = productRepo.findById(l.getProductId())
                    .orElseThrow(() -> new NotFoundException("SP không tồn tại"));

            Qrcode qr = (l.getQrId() == null) ? null
                    : qrRepo.findById(l.getQrId()).orElseThrow(() -> new NotFoundException("QR không tồn tại"));

            GoodsReceiptDetail d = new GoodsReceiptDetail();
            d.setReceipt(gr);
            d.setProduct(p);
            d.setQuantity(l.getQuantity());
            d.setManufactureDate(l.getManufactureDate());
            d.setExpiryDate(l.getExpiryDate());
            d.setStatus("DRAFT");
            d.setNote(l.getNote());
            grdRepo.save(d);

            // upsert lô vào Inventory
            Inventory lot = invRepo.lockLot(w.getId(), p.getId(), l.getQrId(),
                            l.getManufactureDate(), l.getExpiryDate())
                    .orElseGet(() -> Inventory.builder()
                            .warehouse(w).product(p).qrcode(qr)
                            .quantity(0L).reservedQuantity(0L)
                            .manufactureDate(l.getManufactureDate())
                            .expiryDate(l.getExpiryDate())
                            .build());

            lot.setQuantity(lot.getQuantity() + l.getQuantity());
            lot.setLastInAt(LocalDateTime.now());
            invRepo.save(lot);
        }

        gr.setStatus(StockNoteStatus.POSTED);
        gr.setPostedAt(LocalDateTime.now());
        gr.setPostedBy(creator);
        return grRepo.save(gr);
    }

    @Override
    @Transactional
    public GoodsIssues createIssueDraft(CreateIssueDto dto, Long userId) {
        Warehouse w = whRepo.findById(dto.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("Kho không tồn tại"));
        User u = userRepo.getReferenceById(userId);

        GoodsIssues gi = new GoodsIssues();
        gi.setWarehouse(w);
        gi.setOrder(null);
        gi.setCreatedBy(u);
        gi.setStatus(StockNoteStatus.DRAFT);
        gi = giRepo.save(gi);

        for (var l : dto.getLines()) {
            Product p = productRepo.findById(l.getProductId())
                    .orElseThrow(() -> new NotFoundException("SP không tồn tại"));
            GoodsIssuesDetail d = GoodsIssuesDetail.builder()
                    .issue(gi).product(p).quantity(l.getQuantity()).status("DRAFT").build();
            gidRepo.save(d);
        }
        return gi;
    }

    @Override
    @Transactional
    public GoodsIssues confirmPick(Long issueId, Long userId) {
        GoodsIssues gi = giRepo.findById(issueId)
                .orElseThrow(() -> new NotFoundException("Phiếu xuất không tồn tại"));
        if (gi.getStatus() != StockNoteStatus.DRAFT) return gi;

        Warehouse w = gi.getWarehouse();
        for (GoodsIssuesDetail d : gidRepo.findByIssueId(issueId)) {
            long need = d.getQuantity();
            for (Inventory lot : invRepo.lockLotsForIssue(w.getId(), d.getProduct().getId())) {
                if (need == 0) break;
                long qty = Optional.ofNullable(lot.getQuantity()).orElse(0L);
                long rsv = Optional.ofNullable(lot.getReservedQuantity()).orElse(0L);
                long free = Math.max(0L, qty - rsv);
                long take = Math.min(free, need);
                if (take > 0) {
                    lot.setReservedQuantity(rsv + take);
                    invRepo.save(lot);
                    need -= take;
                }
            }
            if (need > 0) throw new BadRequestException("Không đủ tồn khả dụng để reserve");
            d.setStatus("PROCESSING");
            gidRepo.save(d);
        }
        gi.setStatus(StockNoteStatus.CONFIRMED);
        return giRepo.save(gi);
    }

    @Override
    @Transactional
    public GoodsReceipt postReceipt(Long receiptId, Long userId) {
        GoodsReceipt gr = grRepo.findById(receiptId)
                .orElseThrow(() -> new NotFoundException("Phiếu nhập không tồn tại"));
        if (gr.getStatus() == StockNoteStatus.POSTED) return gr;

        User poster = userRepo.getReferenceById(userId);
        var details = grdRepo.findAllByReceiptId(gr.getId());

        for (GoodsReceiptDetail d : details) {
            Product p = d.getProduct();
            Warehouse w = gr.getWarehouse();
            Integer qrId = (d.getReceipt()!=null && d.getReceipt().getId()!=null
                    && d.getProduct()!=null) ? (d.getProduct().getId().intValue()) : null;

            Inventory lot = invRepo.lockLot(w.getId(), p.getId(), null,
                            d.getManufactureDate(), d.getExpiryDate())
                    .orElseGet(() -> Inventory.builder()
                            .warehouse(w).product(p)
                            .quantity(0L).reservedQuantity(0L)
                            .manufactureDate(d.getManufactureDate())
                            .expiryDate(d.getExpiryDate()).build());

            lot.setQuantity(lot.getQuantity() + d.getQuantity());
            lot.setLastInAt(LocalDateTime.now());
            invRepo.save(lot);
            d.setStatus("POSTED");
            grdRepo.save(d);
        }
        gr.setStatus(StockNoteStatus.POSTED);
        gr.setPostedAt(LocalDateTime.now());
        gr.setPostedBy(poster);
        return grRepo.save(gr);
    }

    // ======== XUẤT KHO ========
    @Override
    @Transactional
    public GoodsIssues createAndPost(CreateIssueDto dto, Long userId) {
        Warehouse w = whRepo.findById(dto.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("Kho không tồn tại"));
        User creator = userRepo.getReferenceById(userId);

        GoodsIssues gi = new GoodsIssues();
        gi.setWarehouse(w);
        gi.setStatus(StockNoteStatus.DRAFT);
        gi.setCreatedBy(creator);
        gi.setNote(dto.getNote());
        gi = giRepo.save(gi);

        for (var l : dto.getLines()) {
            Product p = productRepo.findById(l.getProductId())
                    .orElseThrow(() -> new NotFoundException("SP không tồn tại"));

            long need = l.getQuantity();
            var lots = invRepo.lockLotsForIssue(w.getId(), p.getId()); // FEFO
            for (Inventory lot : lots) {
                if (need == 0) break;

                long available = Math.max(
                        0L,
                        Optional.ofNullable(lot.getQuantity()).orElse(0L)
                                - Optional.ofNullable(lot.getReservedQuantity()).orElse(0L)
                );
                long take = Math.min(available, need);
                if (take > 0) {
                    lot.setQuantity(lot.getQuantity() - take);
                    lot.setLastOutAt(LocalDateTime.now());
                    invRepo.save(lot);

                    GoodsIssuesDetail gd = new GoodsIssuesDetail();
                    gd.setIssue(gi);                      // sửa: set entity
                    gd.setProduct(p);
                    gd.setQuantity(take);
                    gd.setStatus("POSTED");
                    gidRepo.save(gd);

                    need -= take;
                }
            }
            if (need > 0) throw new BadRequestException("Tồn kho không đủ để xuất");
        }

        gi.setStatus(StockNoteStatus.POSTED);
        gi.setPostedAt(LocalDateTime.now());
        gi.setPostedBy(creator);
        return giRepo.save(gi);
    }

    @Override
    @Transactional
    public GoodsIssues postIssue(Long issueId, Long userId) {
        GoodsIssues gi = giRepo.findById(issueId)
                .orElseThrow(() -> new NotFoundException("Phiếu xuất không tồn tại"));
        if (gi.getStatus() == StockNoteStatus.COMPLETED || gi.getStatus() == StockNoteStatus.POSTED) return gi;

        Warehouse w   = gi.getWarehouse();
        User poster   = userRepo.getReferenceById(userId);
        var details   = gidRepo.findByIssueId(issueId);

        for (GoodsIssuesDetail d : details) {
            if ("POSTED".equalsIgnoreCase(d.getStatus())) continue;

            Product p = d.getProduct();
            long need = Optional.ofNullable(d.getQuantity()).orElse(0L);
            if (need <= 0) continue;

            // FEFO: lô gần hết hạn trước
            var lots = invRepo.lockLotsForIssue(w.getId(), p.getId());
            for (Inventory lot : lots) {
                if (need == 0) break;

                long onHand   = Optional.ofNullable(lot.getQuantity()).orElse(0L);
                long reserved = Optional.ofNullable(lot.getReservedQuantity()).orElse(0L);
                long available = Math.max(0L, onHand - reserved);

                long take = Math.min(available, need);
                if (take > 0) {
                    lot.setQuantity(onHand - take);
                    lot.setLastOutAt(LocalDateTime.now());
                    invRepo.save(lot);
                    need -= take;
                }
            }

            if (need > 0) {
                throw new BadRequestException("Tồn kho không đủ cho sản phẩm ID=" + p.getId());
            }
            d.setStatus("POSTED");
            gidRepo.save(d);
        }
        gi.setStatus(StockNoteStatus.COMPLETED);
        gi.setPostedAt(LocalDateTime.now());
        gi.setPostedBy(poster);
        return giRepo.save(gi);
    }
}
