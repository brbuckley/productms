package productms.persistence.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** The supplier where the product is purchased from. */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@EqualsAndHashCode
@Table(name = "supplier")
public class SupplierEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private int id;

  @Getter private String supplierId;
  @Getter @Setter private String name;

  @OneToMany(
      mappedBy = "supplier",
      cascade = CascadeType.REMOVE
      // , fetch = FetchType.LAZY
      )
  private List<ProductEntity> product;

  public void setSupplierId(int id) {
    this.supplierId = "SUP" + String.format("%07d", id);
  }

  public void setSupplierId(String id) {
    this.supplierId = id;
  }
}
