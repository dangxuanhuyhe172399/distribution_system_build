package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {
    List<RequestDetail> findAllByRequest_Id(Long requestId);
}
