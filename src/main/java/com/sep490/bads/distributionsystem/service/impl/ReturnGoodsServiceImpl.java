package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.ReturnGoodsDto.ReturnCreateDto;
import com.sep490.bads.distributionsystem.dto.ReturnGoodsDto.ReturnInspectDto;
import com.sep490.bads.distributionsystem.entity.*;
import com.sep490.bads.distributionsystem.entity.type.InspectionResultStatus;
import com.sep490.bads.distributionsystem.entity.type.RequestStatus;
import com.sep490.bads.distributionsystem.entity.type.StockNoteStatus;
import com.sep490.bads.distributionsystem.exception.BadRequestException;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.repository.*;
import com.sep490.bads.distributionsystem.service.ReturnGoodsService;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReturnGoodsServiceImpl implements ReturnGoodsService {
    private final RequestRepository reqRepo;
    private final RequestDetailRepository rdRepo;
    private final SalesOrderDetailRepository orderDetailRepo;
    private final GoodsReceiptRepository grRepo;
    private final GoodsReceiptDetailRepository grdRepo;
    private final GoodsIssuesRepository giRepo;
    private final GoodsIssuesDetailRepository gidRepo;
    private final WarehouseRepository whRepo;
    private final InventoryRepository invRepo;
    private final UserRepository userRepo;
    @Override
    @Transactional
    public Request create(ReturnCreateDto dto, Long userId) {
        Request r = new Request();
        r.setRequestCode(null);
        r.setRequestType("RETURN");
        r.setRequestStatus(RequestStatus.NEW);
        r.setReason(dto.getReason());
        r.setReasonDetail(dto.getReasonDetail());
        r.setCreatedBy(userRepo.getReferenceById(userId));
        r = reqRepo.save(r);

        for (var i : dto.getItems()){
            var rd = new RequestDetail();
            rd.setRequest(r);
            rd.setOrderDetail(orderDetailRepo.getReferenceById(i.getOrderDetailId()));
            rd.setQuantity(i.getQuantity());
            rd.setReasonForItem(i.getReasonForItem());
            rdRepo.save(rd);
        }
        return r;
    }

    @Override
    @Transactional
    public Request inspect(Long requestId, ReturnInspectDto dto, Long userId){
        Request r = reqRepo.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));
        r.setRequestStatus(RequestStatus.INSPECTING);

        boolean anyPass = false;
        boolean allFail = true;
        for (var row : dto.getRows()){
            RequestDetail rd = rdRepo.findById(row.getRequestDetailId())
                    .orElseThrow(() -> new NotFoundException("RequestDetail not found"));
            rd.setInspectedQty(Optional.ofNullable(row.getInspectedQty()).orElse(0L));
            rd.setInspectionResult(row.getResult());
            rdRepo.save(rd);

            if (row.getResult() == InspectionResultStatus.PASS && rd.getInspectedQty() != null && rd.getInspectedQty() > 0){
                anyPass = true;
                allFail = false;
            }
            if (row.getResult() == InspectionResultStatus.PASS) allFail = false;
        }
        r.setRequestStatus(allFail ? RequestStatus.REJECTED : (anyPass ? RequestStatus.APPROVED : RequestStatus.REJECTED));
        return reqRepo.save(r);
    }

    @Override
    @Transactional
    public GoodsReceipt receipt(Long requestId, Long warehouseId, Long userId){
        Request r = reqRepo.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        if (r.getRequestStatus() != RequestStatus.APPROVED) {
            throw new BadRequestException("Request chưa APPROVED");
        }

        GoodsReceipt gr = new GoodsReceipt();
        gr.setWarehouse(whRepo.getReferenceById(warehouseId));
        gr.setCreatedBy(userRepo.getReferenceById(userId));
        gr.setStatus(StockNoteStatus.DRAFT);
        gr = grRepo.save(gr);

        for (RequestDetail rd : rdRepo.findAllByRequest_Id(requestId)){
            if (rd.getInspectionResult() != InspectionResultStatus.PASS) continue;
            long qty = Optional.ofNullable(rd.getInspectedQty()).orElse(0L);
            if (qty <= 0) continue;

            Product p = rd.getOrderDetail().getProduct(); // đảm bảo RD đã được load với product
            GoodsReceiptDetail g = GoodsReceiptDetail.builder()
                    .receipt(gr).product(p).quantity(qty).status("DRAFT").build();
            grdRepo.save(g);

            // Cộng tồn (post ngay)
            final Warehouse wh = whRepo.getReferenceById(warehouseId);
            Inventory lot = invRepo.lockLot(warehouseId, p.getId(), null, null, null)
                    .orElseGet(() -> Inventory.builder()
                            .warehouse(wh)
                            .product(p)
                            .quantity(0L)
                            .reservedQuantity(0L)
                            .build());
            lot.setQuantity(Optional.ofNullable(lot.getQuantity()).orElse(0L) + qty);
            lot.setLastInAt(LocalDateTime.now());
            invRepo.save(lot);
        }
        gr.setStatus(StockNoteStatus.POSTED);
        gr.setPostedAt(LocalDateTime.now());
        gr.setPostedBy(userRepo.getReferenceById(userId));
        gr = grRepo.save(gr);

        r.setRequestStatus(RequestStatus.RECEIPTED);
        reqRepo.save(r);
        return gr;
    }

    @Override @Transactional
    public GoodsIssues scrap(Long requestId, Long warehouseId, Long userId){
        Request r = reqRepo.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));
        GoodsIssues gi = new GoodsIssues();
        gi.setWarehouse(whRepo.getReferenceById(warehouseId));
        gi.setCreatedBy(userRepo.getReferenceById(userId));
        gi.setStatus(StockNoteStatus.DRAFT);
        gi = giRepo.save(gi);

        // nếu bạn muốn xuất hủy từ tồn (trường hợp đã nhập rồi mà kiểm định lại fail)
        for (RequestDetail rd : rdRepo.findAllByRequest_Id(requestId)){
            if (rd.getInspectionResult() != InspectionResultStatus.FAIL) continue;
            long qty = Optional.ofNullable(rd.getInspectedQty()).orElse(0L);
            if (qty <= 0) continue;

            Product p = rd.getOrderDetail().getProduct();
            gidRepo.save(GoodsIssuesDetail.builder().issue(gi).product(p).quantity(qty).status("DRAFT").build());

            // trừ trực tiếp không reserve
            long need = qty;
            for (Inventory lot : invRepo.lockLotsForIssue(warehouseId, p.getId())){
                if (need==0) break;
                long take = Math.min(need, Optional.ofNullable(lot.getQuantity()).orElse(0L));
                if (take>0){
                    lot.setQuantity(lot.getQuantity()-take);
                    lot.setLastOutAt(LocalDateTime.now());
                    invRepo.save(lot);
                    need -= take;
                }
            }
            if (need>0) throw new BadRequestException("Không đủ tồn để xuất hủy");
        }
        gi.setStatus(StockNoteStatus.COMPLETED);
        gi.setPostedAt(LocalDateTime.now());
        gi.setPostedBy(userRepo.getReferenceById(userId));
        giRepo.save(gi);

        r.setRequestStatus(RequestStatus.RETURN_TO_SUPPLIER); // hoặc trạng thái riêng cho scrap nội bộ
        reqRepo.save(r);
        return gi;
    }
}
