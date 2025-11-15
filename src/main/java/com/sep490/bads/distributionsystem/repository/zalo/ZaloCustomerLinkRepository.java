package com.sep490.bads.distributionsystem.repository.zalo;

import com.sep490.bads.distributionsystem.entity.zalo.ZaloCustomerLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZaloCustomerLinkRepository extends JpaRepository<ZaloCustomerLink, String>, JpaSpecificationExecutor<ZaloCustomerLink> {
    Optional<ZaloCustomerLink> findFirstByCustomer_Id(Long customerId);
}
