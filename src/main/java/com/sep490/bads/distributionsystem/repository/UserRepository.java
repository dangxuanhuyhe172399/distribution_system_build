package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.User;
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

    @Query("""
        select u from User u
        left join fetch u.role r
        where u.username = :username
    """)
    Optional<User> findByUsernameWithRole(@Param("username") String username);

//    @Query("""
//        select u from User u
//        where u.email = :email
//          and u.status = com.sep490.bads.distributionsystem.entity.type.UserStatus.ACTIVE
//    """)
//    Optional<User> findActiveByEmail(@Param("email") String email);
}

