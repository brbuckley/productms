package productms.mapper;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import productms.api.model.product.ProductCategory;
import productms.api.model.product.ProductRequest;
import productms.api.model.product.ProductResponse;
import productms.persistence.domain.ProductEntity;

/** Mapper for Product related objects. */
@NoArgsConstructor
@Component
public class ProductMapper {

  private SupplierMapper supplierMapper;

  @Autowired
  public ProductMapper(SupplierMapper supplierMapper) {
    this.supplierMapper = supplierMapper;
  }

  /**
   * Parses from ProductRequest to ProductEntity.
   *
   * @param entity Product Entity.
   * @param request Product Request.
   */
  public void toEntity(ProductEntity entity, ProductRequest request) {
    entity.setName(request.getName());
    entity.setPrice(request.getPrice());
    entity.setCategory(
        request.getCategory() == null ? null : ProductCategory.fromValue(request.getCategory()));
  }

  /**
   * Parses from ProductEntity to ProductResponse.
   *
   * @param entity Product Entity.
   * @return Product Response.
   */
  public ProductResponse fromEntity(ProductEntity entity) {
    ProductResponse response =
        ProductResponse.builder()
            .name(entity.getName())
            .price(entity.getPrice())
            .id(entity.getProductId())
            .build();
    response.setCategory(entity.getCategory() == null ? null : entity.getCategory().getValue());
    response.setSupplier(
        entity.getSupplier() == null ? null : supplierMapper.fromEntity(entity.getSupplier()));
    return response;
  }
}
