package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.dto.productDtos.ProductFilterDto;
import com.sep490.bads.distributionsystem.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsBySku(String sku);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE Product
           SET p_category_id = :categoryId,
               p_unit_id     = :unitId
         WHERE product_id    = :id
        """, nativeQuery = true)
    void updateRelations(@Param("id") Long id,
                         @Param("categoryId") Long categoryId,
                         @Param("unitId") Long unitId);

    static Specification<Product> specFrom(ProductFilterDto f) {
        return (root, q, cb) -> {
            var ps = new ArrayList<Predicate>();
            if (f.getCategoryId()!=null)
                ps.add(cb.equal(root.get("category").get("id"), f.getCategoryId()));

            if (f.getKeyword()!=null && !f.getKeyword().isBlank()) {
                String like = "%" + f.getKeyword().trim().toLowerCase() + "%";
                ps.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("sku")), like),
                        cb.like(cb.lower(root.get("barcode")), like)
                ));
            }

            if (f.getStatus()!=null)
                ps.add(cb.equal(root.get("status"), f.getStatus())); // ProductStatus

            if (f.getPriceFrom()!=null)
                ps.add(cb.greaterThanOrEqualTo(root.get("sellingPrice"), f.getPriceFrom()));
            if (f.getPriceTo()!=null)
                ps.add(cb.lessThanOrEqualTo(root.get("sellingPrice"), f.getPriceTo()));

            return cb.and(ps.toArray(Predicate[]::new));
        };
    }
}
