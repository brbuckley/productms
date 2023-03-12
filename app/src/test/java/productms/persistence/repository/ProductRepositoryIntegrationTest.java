package productms.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import productms.api.model.product.ProductCategory;
import productms.persistence.domain.ProductEntity;

@DataJpaTest
public class ProductRepositoryIntegrationTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private ProductRepository productRepository;

  @Test
  public void testFindByIdsPagination_whenValid_thenReturnProducts() {
    List<String> ids = new ArrayList<>();
    ids.add("PRD0000001");
    ids.add("PRD0000002");
    ids.add("PRD0000003");
    Pageable pageable = PageRequest.of(0, 50, Sort.by(new Sort.Order(Sort.Direction.ASC, "name")));

    List<ProductEntity> found =
        productRepository.findByIdsPagination(ids, ProductCategory.BEER, "%neken%", pageable);

    assertEquals("Heineken", found.get(0).getName());
  }

  @Test
  public void testFindByIdsPagination_whenNulls_thenReturnProducts() {
    Pageable pageable = PageRequest.of(0, 50, Sort.by(new Sort.Order(Sort.Direction.ASC, "name")));

    List<ProductEntity> found = productRepository.findByIdsPagination(null, null, null, pageable);

    assertEquals("Amstel", found.get(0).getName());
  }
}
