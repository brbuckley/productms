package productms.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import productms.persistence.domain.SupplierEntity;

/** Spring JPA repository for Supplier entity. */
@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Integer> {

  SupplierEntity findBySupplierId(String supplierId);
}
