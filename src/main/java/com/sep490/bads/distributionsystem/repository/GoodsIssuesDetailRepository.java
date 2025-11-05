package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.GoodsIssuesDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsIssuesDetailRepository extends JpaRepository<GoodsIssuesDetail, Long> {
    List<GoodsIssuesDetail> findByIssueId(Long issueId);
}
