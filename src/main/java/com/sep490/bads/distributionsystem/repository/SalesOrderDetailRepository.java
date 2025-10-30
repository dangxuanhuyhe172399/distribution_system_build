package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.SalesOrderDetail;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderDetailRepository extends JpaRepository<SalesOrderDetail, Long>, JpaSpecificationExecutor<SalesOrderDetail> {

    //Lấy toàn bộ chi tiết đơn hàng theo ID đơn hàng

    @Query(value = "SELECT * FROM SalesOrderDetail WHERE order_id = :orderId", nativeQuery = true)
    List<SalesOrderDetail> findByOrderId(@Param("orderId") Long orderId);

    //Cập nhật số lượng, đơn giá, chiết khấu
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE SalesOrderDetail
        SET quantity = :quantity,
            unit_price = :unitPrice,
            discount = :discount
        WHERE order_detail_id = :id
    """, nativeQuery = true)
    int updateItem(@Param("id") Long id,
                   @Param("quantity") Long quantity,
                   @Param("unitPrice") Long unitPrice,
                   @Param("discount") Long discount);

    //Xóa mềm (ẩn chi tiết đơn hàng)

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE SalesOrderDetail SET discount = -1 WHERE order_detail_id = :id", nativeQuery = true)
    int softDelete(@Param("id") Long id);

    //Tạo specification linh hoạt từ DTO (nếu cần lọc theo product, order, giá, v.v.)

    static Specification<SalesOrderDetail> specFrom(Long orderId, Long productId) {
        return (root, q, cb) -> {
            var ps = new java.util.ArrayList<Predicate>();

            if (orderId != null)
                ps.add(cb.equal(root.get("order").get("id"), orderId));

            if (productId != null)
                ps.add(cb.equal(root.get("product").get("id"), productId));

            return cb.and(ps.toArray(Predicate[]::new));
        };
    }
}
