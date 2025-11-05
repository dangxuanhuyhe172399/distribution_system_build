package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.GoodsIssues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsIssuesRepository extends JpaRepository<GoodsIssues, Long>, JpaSpecificationExecutor<GoodsIssues> {}
