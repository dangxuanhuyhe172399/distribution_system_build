package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    long countByRequestType(String requestType);
}

