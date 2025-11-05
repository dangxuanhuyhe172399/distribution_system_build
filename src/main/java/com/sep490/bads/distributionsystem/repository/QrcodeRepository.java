package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Qrcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QrcodeRepository extends JpaRepository<Qrcode, Integer> {

}
