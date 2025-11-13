package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.dto.SupplierFilterDto;
import com.sep490.bads.distributionsystem.entity.Supplier;
import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.lang.Nullable;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @EntityGraph(attributePaths = {"category"})
    Page<Supplier> findAll(@Nullable Specification<Supplier> spec, Pageable pageable);


    @Query("""
        SELECT s FROM Supplier s
        LEFT JOIN FETCH s.category
        WHERE s.id = :id
    """)
    Optional<Supplier> findById( @Param("id") Long id);

    static Specification<Supplier> specFrom(SupplierFilterDto f) {
        return (root, query, cb) -> {
            var ps = new ArrayList<Predicate>();
            if (f.getKeyword() != null && !f.getKeyword().isBlank()) {
                String like = "%" + f.getKeyword().toLowerCase().trim() + "%";
                ps.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("email")), like),
                        cb.like(cb.lower(root.get("phone")), like)
                ));
            }
            if (f.getCategoryId() != null)
                ps.add(cb.equal(root.get("category").get("id"), f.getCategoryId()));
            if (f.getStatus() != null)
                ps.add(cb.equal(root.get("status"), f.getStatus()));
            return cb.and(ps.toArray(Predicate[]::new));
        };
    }
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhoneAndIdNot(String phone, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("""
        SELECT s FROM Supplier s
        LEFT JOIN FETCH s.category
        WHERE s.status = com.sep490.bads.distributionsystem.entity.type.SupplierStatus.ACTIVE
    """)
    List<Supplier> findAllActive();

    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.status <> com.sep490.bads.distributionsystem.entity.type.SupplierStatus.INACTIVE")
    long countExcludeDeleted();

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
