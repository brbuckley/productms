package productms.persistence.domain.listener;

import javax.persistence.PostPersist;
import productms.persistence.domain.ProductEntity;

/** Listener for the Customer Entity. */
public class ProductListener {

  /**
   * Creates the productId based on the DB id. It works like a DB trigger.
   *
   * @param product Product persisted.
   */
  @PostPersist
  public void process(ProductEntity product) {
    product.setProductId(product.getId());
  }
}
