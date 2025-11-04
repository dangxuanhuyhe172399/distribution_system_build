package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long>, JpaSpecificationExecutor<GoodsReceipt> {
    boolean existsByReceiptCode(String receiptCode);
}
