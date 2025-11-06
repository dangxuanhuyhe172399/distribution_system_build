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
    Optional<Inventory> lockLot(@Param("warehouse_id") Long warehouseId,
                                @Param("product_id") Long productId,
                                @Param("qr_id") Integer qrId,
                                @Param("manufacture_date") LocalDate mfg,
                                @Param("expiry_date") LocalDate exp);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select i from Inventory i
    where i.warehouse.id=:warehouseId and i.product.id=:productId
    order by i.expiryDate asc nulls last, i.id asc
  """)
    List<Inventory> lockLotsForIssue(@Param("warehouse_id") Long warehouseId,
                                     @Param("product_id") Long productId);

    @Query("""
 select coalesce(sum(coalesce(i.quantity,0) - coalesce(i.reservedQuantity,0)),0)
 from Inventory i
 where i.warehouse.id=:wid and i.product.id=:pid
""")
    Long sumAvailable(@Param("warehouse_id") Long wid, @Param("product_id") Long pid);

    @Query("""
 select i from Inventory i
 where i.warehouse.id=:wid and i.product.id=:pid
   and (coalesce(i.quantity,0) - coalesce(i.reservedQuantity,0)) > 0
 order by i.expiryDate asc nulls last, i.id asc
""")
    List<Inventory> findLotsForDetail(@Param("warehouse_id") Long wid, @Param("product_id") Long pid);
    Page<Inventory> findAll(Specification<Inventory> inventorySpecification, Pageable pageable);
}

