package productms.persistence.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductEntityTest {

  ProductEntity product;

  @BeforeEach
  private void setup() {
    product = new ProductEntity();
  }

  @Test
  public void testSetProductId_whenInteger_thenSet() {
    product.setProductId(1);
    assertEquals("PRD0000001", product.getProductId());
  }
}
