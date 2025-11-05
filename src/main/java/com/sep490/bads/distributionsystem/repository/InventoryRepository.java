package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByWarehouse_IdAndProduct_IdAndQrcode_Id(Long warehouseId, Long productId, Integer qrId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select i from Inventory i
    where i.warehouse.id=:warehouseId and i.product.id=:productId
      and ((:qrId is null and i.qrcode is null) or i.qrcode.id=:qrId)
      and ((:mfg is null and i.manufactureDate is null) or i.manufactureDate=:mfg)
      and ((:exp is null and i.expiryDate is null) or i.expiryDate=:exp)
  """)
    Optional<Inventory> lockLot(@Param("warehouseId") Long warehouseId,
                                @Param("productId") Long productId,
                                @Param("qrId") Integer qrId,
                                @Param("mfg") LocalDate mfg,
                                @Param("exp") LocalDate exp);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select i from Inventory i
    where i.warehouse.id=:warehouseId and i.product.id=:productId
    order by i.expiryDate asc nulls last, i.id asc
  """)
    List<Inventory> lockLotsForIssue(@Param("warehouseId") Long warehouseId,
                                     @Param("productId") Long productId);

    Page<Inventory> findAll(Specification<Inventory> inventorySpecification, Pageable pageable);
}

