package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {
    List<RequestDetail> findAllByRequest_Id(Long requestId);
    @Query("""
    select rd from RequestDetail rd
      join fetch rd.orderDetail od
      join fetch od.product p
      left join fetch p.unit u
    where rd.request.id = :reqId
  """)
    List<RequestDetail> findAllViewByRequestId(@Param("reqId") Long reqId);
}
