package productms.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import productms.ProductMsResponseUtil;
import productms.api.model.product.ProductRequest;
import productms.api.model.product.ProductResponse;
import productms.persistence.domain.ProductEntity;

public class ProductMapperTest {

  ProductMapper mapper;

  @Test
  public void testFromProductEntity_whenValid_thenReturnProductResponse() {
    mapper = new ProductMapper(new SupplierMapper());
    ProductEntity entity = ProductMsResponseUtil.defaultProductEntity();
    ProductResponse response = mapper.fromEntity(entity);
    assertEquals("Heineken", response.getName());
  }

  @Test
  public void testToProductEntity_whenValid_thenUpdateEntity() {
    mapper = new ProductMapper(new SupplierMapper());
    ProductEntity entity = new ProductEntity();
    ProductRequest request = new ProductRequest("Heineken", new BigDecimal("10"), "beer");
    mapper.toEntity(entity, request);
    assertEquals("Heineken", entity.getName());
  }

  @Test
  public void testToProductEntity_whenCategoryNull_thenUpdateEntity() {
    mapper = new ProductMapper(new SupplierMapper());
    ProductEntity entity = new ProductEntity();
    ProductRequest request = new ProductRequest("Heineken", new BigDecimal("10"), null);
    mapper.toEntity(entity, request);
    assertNull(entity.getCategory());
  }
}
