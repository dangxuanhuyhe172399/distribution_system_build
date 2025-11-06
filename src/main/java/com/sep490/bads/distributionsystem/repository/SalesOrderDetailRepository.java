package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.SalesOrderDetail;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderDetailRepository extends JpaRepository<SalesOrderDetail, Long>, JpaSpecificationExecutor<SalesOrderDetail> {

    //Lấy toàn bộ chi tiết đơn hàng theo ID đơn hàng
    @Query(value = "SELECT * FROM SalesOrderDetail WHERE order_id = :orderId", nativeQuery = true)
    List<SalesOrderDetail> findByOrderId(@Param("order_Id") Long orderId);

    //Cập nhật số lượng, đơn giá, chiết khấu
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE SalesOrderDetail
        SET quantity = :quantity,
            unit_price = :unitPrice,
            discount = :discount
        WHERE order_detail_id = :id
    """, nativeQuery = true)
    int updateItem(@Param("order_detail_id") Long id,
                   @Param("quantity") Long quantity,
                   @Param("unit_price") Long unitPrice,
                   @Param("discount") Long discount);

    //Xóa mềm (ẩn chi tiết đơn hàng)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE SalesOrderDetail SET discount = -1 WHERE order_detail_id = :id", nativeQuery = true)
    int softDelete(@Param("order_detail_id") Long id);

    @Query("""
    select d from SalesOrderDetail d
      join fetch d.product p
      left join fetch p.unit u
    where d.order.id = :orderId
  """)
    List<SalesOrderDetail> findAllWithProductByOrderId(@Param("order_Id") Long orderId);

}
