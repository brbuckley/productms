package productms.api.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import productms.api.model.supplier.SupplierResponse;

/** Product Response. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

  private String name;

  private BigDecimal price;

  private String category;

  private String id;

  private SupplierResponse supplier;
}
