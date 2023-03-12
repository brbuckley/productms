package productms.persistence.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SupplierEntityTest {

  SupplierEntity supplier;

  @BeforeEach
  private void setup() {
    supplier = new SupplierEntity();
  }

  @Test
  public void testSetSupplierId_whenInteger_thenSet() {
    supplier.setSupplierId(1);
    assertEquals("SUP0000001", supplier.getSupplierId());
  }

  @Test
  public void testSetSupplierId_whenString_thenSet() {
    supplier.setSupplierId("SUP0000001");
    assertEquals("SUP0000001", supplier.getSupplierId());
  }
}
