package com.sep490.bads.distributionsystem.repository.zalo;

import com.sep490.bads.distributionsystem.entity.zalo.ZaloEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ZaloEventLogRepository extends JpaRepository<ZaloEventLog, UUID> {
    Optional<ZaloEventLog> findByEventId(String eventId);
}
