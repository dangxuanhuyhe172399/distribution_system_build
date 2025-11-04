package com.sep490.bads.distributionsystem.repository.zalo;

import com.sep490.bads.distributionsystem.entity.zalo.ZaloEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ZaloEventLogRepository extends JpaRepository<ZaloEventLog, UUID>, JpaSpecificationExecutor<ZaloEventLog> {
    Optional<ZaloEventLog> findByEventId(String eventId);
    // ZaloCustomerLinkRepository
  //  Optional<ZaloCustomerLink> findById(String zaloUserId);

    // ZaloProductLinkRepository
//    Optional<ZaloProductLink> findByZaloProductId(String zaloProductId);
//    Optional<ZaloProductLink> findByProductId(Integer productId);
//
//    // ZaloOrderLinkRepository
//    Optional<ZaloOrderLink> findByZaloOrderId(String zaloOrderId);

}
