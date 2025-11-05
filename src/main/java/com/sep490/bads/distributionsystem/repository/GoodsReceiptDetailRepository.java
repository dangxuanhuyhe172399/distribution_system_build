package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.GoodsReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsReceiptDetailRepository extends JpaRepository<GoodsReceiptDetail, Long> {
    List<GoodsReceiptDetail> findAllByReceiptId(Long receiptId);
}
