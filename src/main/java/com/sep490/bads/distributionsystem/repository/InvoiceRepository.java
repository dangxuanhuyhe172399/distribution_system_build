package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("""
    select coalesce(sum(i.grandTotal), 0)
    from Invoice i
    where i.order.customer.id = :customerId
      and i.status <> com.sep490.bads.distributionsystem.entity.type.InvoiceStatus.CANCELLED
  """)
    BigDecimal sumPaidByCustomer(@Param("customerId") Long customerId);
}
