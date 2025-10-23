package com.sep490.bads.distributionsystem.repository;

//import com.tal.recruitment.system.entity.Role;
import com.sep490.bads.distributionsystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);
}
