package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

@Query("select r from Role r join User u on u.role = r where u.id = :userId")
Optional<Role> findRoleByUserId(@Param("userId") Long userId);
//
//    List<Role> findByRoleIdAndStatusNot(long l, RoleStatus roleStatus);
//
//    boolean existsByRoleIdAndCode(Long roleId, String code);
}
