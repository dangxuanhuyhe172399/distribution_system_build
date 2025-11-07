package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.SalesOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
