package productms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import productms.ProductMsResponseUtil;
import productms.api.model.product.ProductCategory;
import productms.api.model.product.ProductRequest;
import productms.api.model.product.ProductResponse;
import productms.exception.NotExistException;
import productms.mapper.ProductMapper;
import productms.mapper.SupplierMapper;
import productms.persistence.domain.ProductEntity;
import productms.persistence.repository.ProductRepository;
import productms.persistence.repository.SupplierRepository;

public class ProductServiceTest {

  ProductService service;

  @Test
  public void testPostProduct_whenValid_thenReturnProductAndSupplier()
      throws NotExistException, JsonProcessingException {
    ProductEntity entity = new ProductEntity();
    entity.setSupplier(ProductMsResponseUtil.supplierEntity());
    // Mocks
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    when(productRepo.save(entity)).thenReturn(ProductMsResponseUtil.defaultProductEntity());

    SupplierRepository supplierRepo = Mockito.mock(SupplierRepository.class);
    when(supplierRepo.findBySupplierId("SUP0000001"))
        .thenReturn(ProductMsResponseUtil.supplierEntity());

    service =
        new ProductService(
            productRepo, supplierRepo, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    ProductResponse product = service.postProduct(new ProductRequest(), "SUP0000001");

    assertEquals("Heineken", product.getName());
    Assertions.assertEquals("Supplier A", product.getSupplier().getName());
  }

  @Test
  public void testPostProduct_whenSupplierDoesNotExist_thenThrow() {
    // Mocks
    SupplierRepository supplierRepo = Mockito.mock(SupplierRepository.class);
    when(supplierRepo.findBySupplierId("SUP0000001")).thenReturn(null);

    service =
        new ProductService(
            null, supplierRepo, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    assertThrows(
        NotExistException.class, () -> service.postProduct(new ProductRequest(), "SUP0000001"));
  }

  @Test
  public void testGetProduct_whenFetch_thenReturnProductAndSupplier() throws NotExistException {
    // Mocks
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    when(productRepo.findByProductId("PRD0000001"))
        .thenReturn(ProductMsResponseUtil.defaultProductEntity());

    service =
        new ProductService(
            productRepo, null, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    ProductResponse product = service.getProduct("PRD0000001", true);
    assertEquals("Heineken", product.getName());
    Assertions.assertEquals("Supplier A", product.getSupplier().getName());
  }

  @Test
  public void testGetProduct_whenNoFetch_thenOnlyReturnProduct() throws NotExistException {
    // Mocks
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    when(productRepo.findByProductId("PRD0000001"))
        .thenReturn(ProductMsResponseUtil.defaultProductEntity());

    service =
        new ProductService(
            productRepo, null, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    ProductResponse product = service.getProduct("PRD0000001", false);
    assertEquals("Heineken", product.getName());
    assertNull(product.getSupplier());
  }

  @Test
  public void testGetProduct_whenNotExists_thenThrow() {
    // Mocks
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    when(productRepo.findByProductId("SUP0000001")).thenReturn(null);

    service =
        new ProductService(
            productRepo, null, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    assertThrows(NotExistException.class, () -> service.getProduct("PRD0000001", false));
  }

  @Test
  public void testPutProduct_whenNotExists_thenThrow() {
    // Mocks
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    when(productRepo.findByProductId("PRD0000001")).thenReturn(null);

    service =
        new ProductService(
            productRepo, null, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    assertThrows(
        NotExistException.class, () -> service.putProduct(new ProductRequest(), "PRD0000001"));
  }

  @Test
  public void testPutProduct_whenValid_thenReturnProduct()
      throws NotExistException, JsonProcessingException {
    // Mocks
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    when(productRepo.findByProductId("PRD0000001"))
        .thenReturn(ProductMsResponseUtil.defaultProductEntity());
    when(productRepo.save(ProductMsResponseUtil.defaultProductEntity()))
        .thenReturn(ProductMsResponseUtil.defaultProductEntity());

    service =
        new ProductService(
            productRepo, null, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    ProductResponse product =
        service.putProduct(ProductMsResponseUtil.defaultProductRequest(), "PRD0000001");
    assertEquals("Heineken", product.getName());
  }

  @Test
  public void testGetProducts_whenSortAscIdsNameFetch_thenReturnList() throws NotExistException {
    Pageable pageable = PageRequest.of(10, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "name")));
    List<String> ids = new ArrayList<>();
    ids.add("PRD0000001");
    ids.add("PRD0000002");
    // Mocks
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    when(productRepo.findByIdsPagination(ids, ProductCategory.BEER, "%name%", pageable))
        .thenReturn(ProductMsResponseUtil.productEntityList());

    service =
        new ProductService(
            productRepo, null, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    List<ProductResponse> products =
        service.getProducts("PRD0000001,PRD0000002", "", "name", "asc.name", 10, 10, true);
    assertEquals("Heineken", products.get(0).getName());
    Assertions.assertEquals("Supplier A", products.get(0).getSupplier().getName());
  }

  @Test
  public void testGetProducts_whenSortDescNoFetch_thenReturnList() throws NotExistException {
    Pageable pageable =
        PageRequest.of(10, 10, Sort.by(new Sort.Order(Sort.Direction.DESC, "name")));
    // Mocks
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    when(productRepo.findByIdsPagination(null, ProductCategory.BEER, null, pageable))
        .thenReturn(ProductMsResponseUtil.productEntityList());

    service =
        new ProductService(
            productRepo, null, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    List<ProductResponse> products =
        service.getProducts(null, "", null, "desc.name", 10, 10, false);
    assertEquals("Heineken", products.get(0).getName());
    assertNull(products.get(0).getSupplier());
  }

  @Test
  public void testGetProducts_whenNotFound_thenThrow() {
    Pageable pageable =
        PageRequest.of(10, 10, Sort.by(new Sort.Order(Sort.Direction.DESC, "name")));
    // Mocks
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    when(productRepo.findByIdsPagination(null, ProductCategory.BEER, "%%", pageable))
        .thenReturn(new ArrayList<>());

    service =
        new ProductService(
            productRepo, null, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    assertThrows(
        NotExistException.class,
        () -> service.getProducts(null, "", "", "desc.name", 10, 10, false));
  }

  @Test
  public void testPutProduct_whenNoCategory_thenReturnProduct()
      throws NotExistException, JsonProcessingException {
    ProductEntity entity = new ProductEntity();
    entity.setProductId(1);
    entity.setSupplier(ProductMsResponseUtil.supplierEntity());
    // Mocks
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    when(productRepo.findByProductId("PRD0000001"))
        .thenReturn(ProductMsResponseUtil.defaultProductEntity());
    when(productRepo.save(entity)).thenReturn(ProductMsResponseUtil.minimalProductEntity());

    service =
        new ProductService(
            productRepo, null, new ObjectMapper(), new ProductMapper(new SupplierMapper()));
    ProductResponse product = service.putProduct(new ProductRequest(), "PRD0000001");
    assertNull(product.getCategory());
    assertEquals("PRD0000001", product.getId());
  }
}
