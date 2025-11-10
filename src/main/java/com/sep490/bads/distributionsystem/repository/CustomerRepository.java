package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByPhone(String phone);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhoneAndIdNot(String phone, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
