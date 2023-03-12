package productms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import productms.api.model.product.ProductCategory;
import productms.api.model.product.ProductRequest;
import productms.api.model.product.ProductResponse;
import productms.api.model.supplier.SupplierResponse;
import productms.persistence.domain.ProductEntity;
import productms.persistence.domain.SupplierEntity;

public class ProductMsResponseUtil {

  public static ProductResponse defaultProductResponse() {
    ProductResponse product = new ProductResponse();
    product.setId("PRD0000001");
    product.setName("Heineken");
    product.setPrice(new BigDecimal("10"));
    product.setCategory("beer");
    product.setSupplier(new SupplierResponse("SUP0000001", "Supplier A"));
    return product;
  }

  public static ProductResponse noSupplierResponse() {
    ProductResponse product = defaultProductResponse();
    product.setSupplier(null);
    return product;
  }

  public static SupplierEntity supplierEntity() {
    return new SupplierEntity(1, "SUP0000001", "Supplier A", new ArrayList<>());
  }

  public static List<ProductResponse> productResponseList() {
    List<ProductResponse> products = new ArrayList<>();
    products.add(defaultProductResponse());
    ProductResponse response = defaultProductResponse();
    response.setId("PRD0000002");
    response.setName("Amstel");
    products.add(response);
    return products;
  }

  public static List<ProductEntity> productEntityList() {
    List<ProductEntity> products = new ArrayList<>();
    products.add(defaultProductEntity());
    products.add(defaultProductEntity());
    return products;
  }

  public static ProductEntity defaultProductEntity() {
    ProductEntity product = new ProductEntity();
    product.setProductId("PRD0000001");
    product.setName("Heineken");
    product.setPrice(new BigDecimal("10"));
    product.setCategory(ProductCategory.BEER);
    product.setSupplier(new SupplierEntity(1, "SUP0000001", "Supplier A", new ArrayList<>()));
    return product;
  }

  public static ProductEntity minimalProductEntity() {
    ProductEntity product = new ProductEntity();
    product.setProductId("PRD0000001");
    product.setPrice(new BigDecimal("10"));
    product.setSupplier(new SupplierEntity(1, "SUP0000001", null, new ArrayList<>()));
    return product;
  }

  public static ProductRequest defaultProductRequest() {
    ProductRequest productRequest = new ProductRequest();
    productRequest.setCategory("beer");
    productRequest.setPrice(new BigDecimal("10"));
    productRequest.setName("Heineken");
    return productRequest;
  }

  public static ProductRequest updateProductRequest() {
    ProductRequest productRequest = new ProductRequest();
    productRequest.setCategory("beer");
    productRequest.setPrice(new BigDecimal("12"));
    productRequest.setName("Heineken Light");
    return productRequest;
  }

  public static ProductResponse updateProductResponse() {
    ProductResponse product = new ProductResponse();
    product.setId("PRD0000001");
    product.setName("Heineken Light");
    product.setPrice(new BigDecimal("12"));
    product.setCategory("beer");
    product.setSupplier(new SupplierResponse("SUP0000001", "Supplier A"));
    return product;
  }
}
