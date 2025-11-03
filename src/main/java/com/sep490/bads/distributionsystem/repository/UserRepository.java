package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findActiveByUsername(String username);

    Optional<User> findActiveByEmail(String email);

    Optional<User> findActiveById(Long id);

    List<User> findByIdIn(List<Long> ids);

    List<User> findByEmailIn(List<String> validEmails);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("select u from User u left join fetch u.role where u.id = :id")
    Optional<User> findByIdFetchRole(@Param("id") Long id);

    @Query("""
        select u from User u
        left join fetch u.role r
        where u.username = :username
    """)
    Optional<User> findByUsernameWithRole(@Param("username") String username);

    @Query("""
  select u from User u
  left join fetch u.role
  where u.username = :username and u.status = com.sep490.bads.distributionsystem.entity.type.UserStatus.ACTIVE
""")
    Optional<User> findActiveByUsernameWithRole(@Param("username") String username);

    @Query("select count(u) from User u where u.status <> com.sep490.bads.distributionsystem.entity.type.UserStatus.DELETED")
    long countExcludeDeleted();
    long countByStatus(UserStatus status);
}

