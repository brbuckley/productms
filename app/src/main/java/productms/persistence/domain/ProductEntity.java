package productms.persistence.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import productms.api.model.product.ProductCategory;
import productms.persistence.domain.listener.ProductListener;

/** The object containing the minimal required information of a product. */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@EntityListeners(ProductListener.class)
@EqualsAndHashCode
@Table(name = "product")
public class ProductEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private int id;

  @Getter private String productId;

  @Getter @Setter private String name;

  @Getter @Setter private BigDecimal price;

  @Getter @Setter private ProductCategory category;

  @ManyToOne @Getter @Setter private SupplierEntity supplier;

  // This is more of a utility setter
  public void setProductId(int id) {
    this.productId = "PRD" + String.format("%07d", id);
  }

  public void setProductId(String id) {
    this.productId = id;
  }
}
