package com.sep490.bads.distributionsystem.entity.type;

public enum ContractStatus {
        DRAFT,              // Nháp
        PENDING_APPROVAL,   // Chờ phê duyệt
        APPROVED,           // Đã phê duyệt
        REJECTED,           // Bị từ chối
        COMPLETED;          // Hoàn thành

        public String getLabel() {
            return switch (this) {
                case DRAFT -> "Nháp";
                case PENDING_APPROVAL -> "Chờ phê duyệt";
                case APPROVED -> "Đã phê duyệt";
                case REJECTED -> "Bị từ chối";
                case COMPLETED -> "Hoàn thành";
            };
        }
}
