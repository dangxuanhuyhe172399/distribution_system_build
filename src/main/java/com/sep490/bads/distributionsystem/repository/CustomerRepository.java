package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    @Query("SELECT c FROM Customer c WHERE c.status = 'ACTIVE'")
    Page<Customer> findAllActive(Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);
}
