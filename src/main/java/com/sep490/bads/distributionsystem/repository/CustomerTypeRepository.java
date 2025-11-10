package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {
    Optional<CustomerType> findByName(String name);
}
