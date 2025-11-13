package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.SupplierCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierCategoryRepository extends JpaRepository<SupplierCategory, Long> {
}
