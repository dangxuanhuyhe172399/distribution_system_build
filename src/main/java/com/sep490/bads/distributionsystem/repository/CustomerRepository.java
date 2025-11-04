package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Customer;
import com.sep490.bads.distributionsystem.entity.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByPhone(String phone);
    Optional<CustomerType> findByName(String name);
}
