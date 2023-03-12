package productms.mapper;

import org.springframework.stereotype.Component;
import productms.api.model.supplier.SupplierResponse;
import productms.persistence.domain.SupplierEntity;

/** Mapper for Supplier related objects. */
@Component
public class SupplierMapper {

  /**
   * Parses from SupplierEntity to SupplierResponse.
   *
   * @param entity Supplier Entity.
   * @return Supplier Response.
   */
  public SupplierResponse fromEntity(SupplierEntity entity) {
    return new SupplierResponse(entity.getSupplierId(), entity.getName());
  }
}
