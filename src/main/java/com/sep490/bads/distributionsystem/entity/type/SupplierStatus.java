package com.sep490.bads.distributionsystem.entity.type;

public enum SupplierStatus {
    ACTIVE,   // Đang hoạt động
    INACTIVE;  // Tạm ngưng

    public String getLabel() {
        return switch (this) {
            case ACTIVE -> "Đang hoạt động";
            case INACTIVE -> "Tạm ngưng";
        };
    }
}
