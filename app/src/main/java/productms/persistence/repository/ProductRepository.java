package productms.persistence.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import productms.api.model.product.ProductCategory;
import productms.persistence.domain.ProductEntity;

/** Spring JPA repository for Product entity. */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

  ProductEntity findByProductId(String productId);

  // This query works like an "if/else", each AND statement has 2 options (OR), and for every case
  // one is selected.
  // But most of them does not affect the results, so either it filters by the option, or does
  // nothing when null.
  @Query(
      "select p from ProductEntity p where "
          + "( (coalesce(:ids) is null) or (coalesce(:ids) is not null and p.productId in :ids) ) "
          + "and ( (:category is null) or (:category is not null and p.category = :category) ) "
          + "and ( (coalesce(:name) is null) or (:name is not null and p.name like :name ) ) ")
  List<ProductEntity> findByIdsPagination(
      @Param("ids") List<String> productIds,
      @Param("category") ProductCategory category,
      @Param("name") String name,
      Pageable pageRequest);
}
