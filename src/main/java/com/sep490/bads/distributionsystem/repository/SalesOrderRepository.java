package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>, JpaSpecificationExecutor<SalesOrder> {
    boolean existsBySaleOrderCode(String saleOrderCode);

    // lấy 1 đơn + details + product + customer + user
    @Query("""
        select distinct o from SalesOrder o
          left join fetch o.orderDetails d
          left join fetch d.product p
          left join fetch o.customer c
          left join fetch o.user u
        where o.id = :id
    """)
    Optional<SalesOrder> findByIdDeep(@Param("id") Long id);

    long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    // Tổng tiền đã mua (loại đơn hủy)
    @Query("""
        select coalesce(sum(o.totalAmount), 0)
        from SalesOrder o
        where o.customer.id = :customerId
          and o.status <> com.sep490.bads.distributionsystem.entity.type.SaleOderStatus.CANCELLED
    """)
    BigDecimal sumTotalByCustomer(@Param("customerId") Long customerId);

    @Query("""
        select count(o)
        from SalesOrder o
        where o.customer.id = :customerId
          and o.status <> com.sep490.bads.distributionsystem.entity.type.SaleOderStatus.CANCELLED
    """)
    long countActiveByCustomer(@Param("customerId") Long customerId);

    // Lấy N đơn gần nhất + kèm invoice (để tính paidAmount)
    @EntityGraph(attributePaths = {"invoice"})
    Page<SalesOrder> findByCustomer_IdOrderByCreatedAtDesc(Long customerId, Pageable pageable);

//    //Đã thanh toán
//    @Query("select coalesce(sum(p.amount),0) from Payment p where p.order.id=:orderId and p.status='CONFIRMED'")
//    BigDecimal sumPaidByOrder(@Param("orderId") Long orderId);
}
