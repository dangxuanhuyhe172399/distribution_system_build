package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Customer;
import com.sep490.bads.distributionsystem.entity.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByPhone(String phone);
    Optional<CustomerType> findByName(String name);
    @Query("SELECT c FROM Customer c WHERE c.status = 'ACTIVE'")
    Page<Customer> findAllActive(Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);
}
