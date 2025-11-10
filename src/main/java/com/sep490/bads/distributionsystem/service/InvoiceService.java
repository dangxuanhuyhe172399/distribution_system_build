package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.invoiceDtos.InvoiceDto;
import com.sep490.bads.distributionsystem.dto.invoiceDtos.InvoiceFilterDto;
import org.springframework.data.domain.Page;

public interface InvoiceService {
    InvoiceDto createFromOrder(Long orderId, Long supplierId, String paymentMethod);
    InvoiceDto issuePdfInvoice(Long invoiceId);         // phát hành (tạo PDF + chuyển ISSUED)
    InvoiceDto resend(Long invoiceId);        // gửi lại cho KH (tăng sendCount, cập nhật sentAt)
    InvoiceDto getOne(Long id);
    Page<InvoiceDto> filter(InvoiceFilterDto f);
}
