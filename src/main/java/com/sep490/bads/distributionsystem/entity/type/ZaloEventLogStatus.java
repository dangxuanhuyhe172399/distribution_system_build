package com.sep490.bads.distributionsystem.entity.type;

public enum ZaloEventLogStatus {
    RECEIVED,   // đã nhận và log
    PROCESSED,  // xử lý xong
    FAILED,     // lỗi khi xử lý
    DUPLICATE   // trùng idempotency
}
