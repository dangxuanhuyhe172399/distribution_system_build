package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.GoodsReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsReceiptDetailRepository extends JpaRepository<GoodsReceiptDetail, Long> {
    List<GoodsReceiptDetail> findAllByReceiptId(Long receiptId);

    @Query("""
 select s.name
 from GoodsReceiptDetail d
 join d.receipt r
 left join r.contract c
 left join c.supplier s
 where r.warehouse.id=:wid and d.product.id=:pid and r.status='POSTED'
 order by r.postedAt desc
""")
    List<String> findLastSupplierName(@Param("wid") Long wid, @Param("pid") Long pid);
}
