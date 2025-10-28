package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.dto.SalesOrderFilterDto;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>, JpaSpecificationExecutor<SalesOrder> {


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE Sales_Order SET status = :status WHERE sales_order_id = :id", nativeQuery = true)
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE Sales_Order SET status = 'DELETE' WHERE sales_order_id = :id", nativeQuery = true)
    int softDelete(@Param("id") Long id);


    static Specification<SalesOrder> specFrom(SalesOrderFilterDto f) {
        return (root, q, cb) -> {
            var ps = new java.util.ArrayList<Predicate>();

            if (f.getKeyword() != null && !f.getKeyword().isBlank()) {
                String like = "%" + f.getKeyword().trim().toLowerCase() + "%";
                ps.add(cb.like(cb.lower(root.get("customer").get("name")), like));
            }

            if (f.getStatus() != null && !f.getStatus().isBlank()) {
                ps.add(cb.equal(root.get("status"), f.getStatus()));
            }

            if (f.getPaymentMethod() != null && !f.getPaymentMethod().isBlank()) {
                ps.add(cb.equal(root.get("paymentMethod"), f.getPaymentMethod()));
            }

            if (f.getMinTotal() != null) {
                ps.add(cb.greaterThanOrEqualTo(root.get("grandTotal"), f.getMinTotal()));
            }

            if (f.getMaxTotal() != null) {
                ps.add(cb.lessThanOrEqualTo(root.get("grandTotal"), f.getMaxTotal()));
            }

            return cb.and(ps.toArray(Predicate[]::new));
        };
    }
}
