package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findActiveByUsername(String username);

    Optional<User> findActiveByEmail(String email);

    Optional<User> findActiveById(Long id);

    List<User> findByIdIn(List<Long> ids);

    List<User> findByEmailIn(List<String> validEmails);

}

