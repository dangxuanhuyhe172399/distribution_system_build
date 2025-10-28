package com.sep490.bads.distributionsystem.repository;

import com.sep490.bads.distributionsystem.dto.ProductFilterDto;
import com.sep490.bads.distributionsystem.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsBySku(String sku);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value =
            "UPDATE Product " +
                    "SET sku = :sku, " +
                    "    name = :name, " +
                    "    cost_price = :costPrice, " +
                    "    selling_price = :sellingPrice, " +
                    "    status = :status " +
                    "WHERE product_id = :id",
            nativeQuery = true)
    int updateBasic(@Param("id") Long id,
                    @Param("sku") String sku,
                    @Param("name") String name,
                    @Param("costPrice") Long costPrice,
                    @Param("sellingPrice") Long sellingPrice,
                    @Param("status") String status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value =
            "UPDATE Product " +
                    "SET category_id = :categoryId, " +
                    "    unit_id = :unitId " +
                    "WHERE product_id = :id",
            nativeQuery = true)
    int updateRelations(@Param("id") Long id,
                        @Param("categoryId") Long categoryId,
                        @Param("unitId") Long unitId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE Product SET status = 'DELETE' WHERE product_id = :id", nativeQuery = true)
    int softDelete(@Param("id") Long id);

    static Specification<Product> specFrom(ProductFilterDto f) {
        return (root, q, cb) -> {
            var ps = new java.util.ArrayList<Predicate>();
            if (f.getCategoryId() != null) ps.add(cb.equal(root.get("category").get("id"), f.getCategoryId()));
            if (f.getKeyword()!=null && !f.getKeyword().isBlank()) {
                String like = "%" + f.getKeyword().trim().toLowerCase() + "%";
                ps.add(cb.like(cb.lower(root.get("name")), like));
            }
            if (f.getStatus()!=null) ps.add(cb.equal(root.get("status"), f.getStatus()));
            if (f.getPriceFrom()!=null) ps.add(cb.greaterThanOrEqualTo(root.get("sellingPrice"), f.getPriceFrom()));
            if (f.getPriceTo()!=null)   ps.add(cb.lessThanOrEqualTo(root.get("sellingPrice"),  f.getPriceTo()));
            return cb.and(ps.toArray(Predicate[]::new));
        };
    }
}
