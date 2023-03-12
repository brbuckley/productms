package productms.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import productms.persistence.domain.SupplierEntity;

@DataJpaTest
public class SupplierRepositoryIntegrationTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private SupplierRepository supplierRepository;

  @Test
  public void testFindBySupplierId_whenValidId_thenFind() {
    // Insert Test Supplier
    SupplierEntity entity = new SupplierEntity();
    entity.setName("Test Supplier");
    entity.setSupplierId("SUP1000000");
    entityManager.persist(entity);

    SupplierEntity found = supplierRepository.findBySupplierId("SUP1000000");

    assertEquals("Test Supplier", found.getName());
  }
}
