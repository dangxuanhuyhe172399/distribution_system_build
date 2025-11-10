package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.invoiceDtos.InvoiceDto;
import com.sep490.bads.distributionsystem.dto.invoiceDtos.InvoiceFilterDto;
import com.sep490.bads.distributionsystem.entity.Invoice;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
import com.sep490.bads.distributionsystem.entity.Supplier;
import com.sep490.bads.distributionsystem.entity.type.DeliveryStatus;
import com.sep490.bads.distributionsystem.entity.type.InvoiceStatus;
import com.sep490.bads.distributionsystem.mapper.InvoiceMapper;
import com.sep490.bads.distributionsystem.repository.InvoiceRepository;
import com.sep490.bads.distributionsystem.repository.SalesOrderRepository;
import com.sep490.bads.distributionsystem.repository.SupplierRepository;
import com.sep490.bads.distributionsystem.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepo;
    private final SupplierRepository supplierRepo;
    private final SalesOrderRepository orderRepo;

    @Override
    @Transactional
    public InvoiceDto createFromOrder(Long orderId, Long supplierId, String paymentMethod) {
//        SalesOrder order = orderRepo.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng " + orderId));
//        Supplier supplier = supplierRepo.findById(supplierId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp " + supplierId));
//
//        String code = nextInvoiceCode(); // VD: HD-00001
//
//        Invoice inv = Invoice.builder()
//                .invoiceCode(code)
//                .order(order)
//                .supplier(supplier)
//                .status(InvoiceStatus.DRAFT)
//                .deliveryStatus(DeliveryStatus.CHO_XUAT_KHO)
//                .paymentMethod(paymentMethod)
//                .sendCount(0)
//                .build();
//
//        // build items từ order items
//        var items = new ArrayList<InvoiceItem>();
//        BigDecimal sum = BigDecimal.ZERO;
//        BigDecimal sumVat = BigDecimal.ZERO;
//
//        for (SalesOrderItem oi : order.getItems()) {
//            BigDecimal amount = oi.getUnitPrice().multiply(BigDecimal.valueOf(oi.getQuantity()));
//            BigDecimal vatAmt = amount.multiply(BigDecimal.valueOf(oi.getVatRate())).divide(BigDecimal.valueOf(100));
//
//            InvoiceItem it = InvoiceItem.builder()
//                    .invoice(inv)
//                    .productName(oi.getProduct().getName())
//                    .unitName(oi.getUnit().getName())
//                    .quantity(oi.getQuantity())
//                    .unitPrice(oi.getUnitPrice())
//                    .amount(amount)
//                    .vatRate(oi.getVatRate())
//                    .vatAmount(vatAmt)
//                    .build();
//
//            items.add(it);
//            sum = sum.add(amount);
//            sumVat = sumVat.add(vatAmt);
//        }
//        inv.setItems(items);
//        inv.setVatAmount(sumVat);
//        inv.setGrandTotal(sum.add(sumVat));
//
//        invoiceRepo.save(inv);
//        return mapper.toDto(inv);
        return null;
    }

    @Override
    @Transactional
    public InvoiceDto issuePdfInvoice(Long invoiceId) {
//        Invoice inv = invoiceRepo.findById(invoiceId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn " + invoiceId));
//        if (inv.getStatus() != InvoiceStatus.DRAFT)
//            throw new RuntimeException("Chỉ phát hành hóa đơn ở trạng thái DRAFT");
//
//        inv.setStatus(InvoiceStatus.ISSUED);
//        inv.setIssuedAt(Instant.now());
//
//        // generate PDF (stub) => lưu file
//        InvoiceFile file = InvoiceFile.builder()
//                .invoice(inv)
//                .version((inv.getFiles()==null?0:inv.getFiles().size()) + 1)
//                .fileName(inv.getInvoiceCode() + ".pdf")
//                .filePath("/files/invoices/" + inv.getInvoiceCode() + ".pdf") // TODO: thay bằng đường dẫn thật
//                .build();
//        inv.getFiles().add(file);
//
//        invoiceRepo.save(inv);
//        return mapper.toDto(inv);
        return null;
    }

    @Override
    @Transactional
    public InvoiceDto resend(Long invoiceId) {
//        Invoice inv = invoiceRepo.findById(invoiceId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn " + invoiceId));
//        if (inv.getStatus() == InvoiceStatus.DRAFT)
//            throw new RuntimeException("Hóa đơn chưa phát hành, không thể gửi lại");
//
//        // thực hiện send email/sms link PDF
//        inv.setSentAt(Instant.now());
//        inv.setSendCount((inv.getSendCount() == null ? 0 : inv.getSendCount()) + 1);
//        if (inv.getStatus() == InvoiceStatus.ISSUED) inv.setStatus(InvoiceStatus.SENT);
//
//        return mapper.toDto(inv);
        return null;
    }

    @Override
    public InvoiceDto getOne(Long id) {
//        return mapper.toDto(invoiceRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn " + id)));
        return null;
    }

    @Override
    public Page<InvoiceDto> filter(InvoiceFilterDto f) {
//        Sort.Direction dir = "DESC".equalsIgnoreCase(f.getDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Pageable pageable = PageRequest.of(f.getPage(), f.getSize(), Sort.by(dir, f.getSortBy()));
//        return invoiceRepo.findAll(
//                InvoiceRepository.spec(f.getKeyword(), f.getStatus(), f.getDeliveryStatus(), f.getFrom(), f.getTo()),
//                pageable
//        ).map(mapper::toDto);
        return null;
    }

    private String nextInvoiceCode() {
//        long next = invoiceRepo.findLast().map(Invoice::getId).orElse(0L) + 1;
//        return "HD-" + String.format("%05d", next);
        return "";
    }
}
