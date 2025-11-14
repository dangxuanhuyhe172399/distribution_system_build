package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.ContractDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ContractDetailRepository extends JpaRepository<ContractDetail, Long> {
    List<ContractDetail> findByContractId(Long contractId);

    @Query("""
           select coalesce(sum(cd.unitPrice * cd.quantity + coalesce(cd.vatAmount, 0)), 0)
           from ContractDetail cd
           where cd.contract.id = :contractId
           """)
    BigDecimal calculateTotalAmount(@Param("contractId") Long contractId);
}
