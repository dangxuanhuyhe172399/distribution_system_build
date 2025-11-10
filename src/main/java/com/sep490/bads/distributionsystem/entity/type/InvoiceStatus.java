package com.sep490.bads.distributionsystem.entity.type;

public enum InvoiceStatus {
    DRAFT,        // tạo nháp từ đơn hàng (chưa phát hành)
    ISSUED,       // đã phát hành (đã sinh file PDF/serial)
    SENT,         // đã gửi cho khách (email/zalo/sms…)
    CANCELLED, // hủy (sai thông tin, xuất lại hóa đơn mới)
    WAITING_WAREHOUSE,// chờ xuất kho
    DELIVERING,       // đang giao
    DELIVERED,        // đã giao
}
