package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.dto.SupplierFilterDto;
import com.sep490.bads.distributionsystem.entity.Supplier;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {

    @Override
    @EntityGraph(attributePaths = {"category"})
    Page<Supplier> findAll(Specification<Supplier> spec, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"category"})
    Optional<Supplier> findById(Long id);

    @EntityGraph(attributePaths = {"category"})
    Optional<Supplier> findByName(String name);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    @Query("""
        SELECT s FROM Supplier s
        LEFT JOIN FETCH s.category
        WHERE s.status = com.sep490.bads.distributionsystem.entity.type.CommonStatus.ACTIVE
    """)
    List<Supplier> findAllActive();

    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.status <> com.sep490.bads.distributionsystem.entity.type.CommonStatus.INACTIVE")
    long countExcludeDeleted();

    long countByStatus(CommonStatus status);

    @Query("""
        SELECT s FROM Supplier s
        LEFT JOIN FETCH s.category
        WHERE s.id = :id
    """)
    Optional<Supplier> findByIdFetchCategory(@Param("id") Long id);

    static Specification<Supplier> specFrom(SupplierFilterDto f) {
        return (root, query, cb) -> {
            var ps = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
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
            return cb.and(ps.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }
}
