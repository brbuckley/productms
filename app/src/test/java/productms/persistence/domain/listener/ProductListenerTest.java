package productms.persistence.domain.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import org.junit.jupiter.api.Test;
import productms.ProductMsResponseUtil;
import productms.persistence.domain.ProductEntity;

public class ProductListenerTest {

  @Test
  public void testProcess_whenValid_thenSetProductId() throws ParseException {
    // In this scenario the product initially has id = 0 and productId = PRD0000001
    // After processing, the productId should be based of the id number, which means PRD0000000
    ProductEntity entity = ProductMsResponseUtil.defaultProductEntity();
    assertEquals("PRD0000001", entity.getProductId());
    ProductListener listener = new ProductListener();
    listener.process(entity);
    assertEquals("PRD0000000", entity.getProductId());
  }
}
