package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Supplier;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    @Query("SELECT s FROM Supplier s WHERE s.status = com.sep490.bads.distributionsystem.entity.type.CommonStatus.ACTIVE")
    Page<Supplier> findAllActive(Pageable pageable);
}
