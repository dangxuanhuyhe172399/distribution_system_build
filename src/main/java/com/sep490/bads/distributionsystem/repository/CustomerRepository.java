package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findFirstByPhone(String phone);
    Optional<Customer> findFirstByEmail(String email);
    Optional<Customer> findFirstByPhoneOrEmail(String phone, String email);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhoneAndIdNot(String phone, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // top khách hàng mua gần nhất (loại đơn hủy & khách đã xóa) ====
    @Query("""
        select c, max(o.createdAt)
        from SalesOrder o
          join o.customer c
        where o.status <> com.sep490.bads.distributionsystem.entity.type.SaleOderStatus.CANCELLED
          and c.status <> com.sep490.bads.distributionsystem.entity.type.CustomerStatus.DELETED
        group by c
        order by max(o.createdAt) desc
    """)
    Page<Object[]> findRecentCustomers(Pageable pageable);
}
