package productms.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import productms.api.model.product.ProductRequest;
import productms.exception.NotExistException;
import productms.service.ProductService;
import productms.util.HeadersUtil;

/** Controller for the product services. */
@RestController
@Validated
public class ProductController {

  private ProductService productService;

  /**
   * Custom constructor to inject the needed services.
   *
   * @param productService Product service.
   */
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  /**
   * Post endpoint to add a new product to the system.
   *
   * @param correlationId Optional correlation id header.
   * @param product Product Minimal Object.
   * @param supplierId Supplier Id.
   * @return Product.
   * @throws NotExistException Not Exist Exception.
   * @throws JsonProcessingException Json Processing Exception.
   */
  @PostMapping(value = "/{supplierId}", produces = "application/json")
  public ResponseEntity postProduct(
      @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
      @Pattern(regexp = "^SUP[0-9]{7}$") @PathVariable("supplierId") String supplierId,
      @Valid @RequestBody ProductRequest product)
      throws NotExistException, JsonProcessingException {
    return ResponseEntity.status(201)
        .headers(HeadersUtil.defaultHeaders(correlationId))
        .body(productService.postProduct(product, supplierId));
  }

  /**
   * Get endpoint to find a product saved at the system.
   *
   * @param correlationId Optional correlation id header.
   * @param productId ProductId.
   * @param fetchSuppliers Fetch Suppliers flag.
   * @return Product.
   * @throws NotExistException Not Exist Exception.
   */
  @GetMapping(value = "/{productId}", produces = "application/json")
  public ResponseEntity getProduct(
      @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
      @RequestParam(name = "fetch-suppliers", defaultValue = "false", required = false)
          boolean fetchSuppliers,
      @Pattern(regexp = "^PRD[0-9]{7}$") @PathVariable("productId") String productId)
      throws NotExistException {
    return ResponseEntity.ok()
        .headers(HeadersUtil.defaultHeaders(correlationId))
        .body(productService.getProduct(productId, fetchSuppliers));
  }

  /**
   * Get all products with filters.
   *
   * @param correlationId Correlation Id header.
   * @param ids Ids filter.
   * @param category Category filter.
   * @param name Name filter.
   * @param sort Sort by.
   * @param limit Limit results.
   * @param offset Offset pagination.
   * @param fetchSuppliers Fetch Suppliers flag.
   * @return List of Products.
   * @throws NotExistException Not Exist Exception.
   */
  @GetMapping(value = "/", produces = "application/json")
  public ResponseEntity getProducts(
      @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
      @Pattern(regexp = "^(PRD[0-9]{7}(|,))*$") @RequestParam(name = "ids", required = false)
          String ids,
      @Pattern(regexp = "^(beer|wine)$") @RequestParam(name = "category", required = false)
          String category,
      @Pattern(regexp = "^.{1,50}$") @RequestParam(name = "name", required = false) String name,
      @Pattern(regexp = "^(asc|desc)\\.(name|price)$")
          @RequestParam(name = "sort", defaultValue = "asc.name", required = false)
          String sort,
      @RequestParam(name = "limit", defaultValue = "50", required = false) int limit,
      @RequestParam(name = "offset", defaultValue = "0", required = false) int offset,
      @RequestParam(name = "fetch-suppliers", defaultValue = "false", required = false)
          boolean fetchSuppliers)
      throws NotExistException {
    return ResponseEntity.ok()
        .headers(HeadersUtil.defaultHeaders(correlationId))
        .body(productService.getProducts(ids, category, name, sort, limit, offset, fetchSuppliers));
  }

  /**
   * Update Product.
   *
   * @param correlationId Correlation Id header.
   * @param productId Product Id.
   * @param product Product.
   * @return Updated product.
   * @throws NotExistException Not Exist Exception.
   * @throws JsonProcessingException Json Processing Exception.
   */
  @PutMapping(value = "/{productId}", produces = "application/json")
  public ResponseEntity putProduct(
      @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
      @Pattern(regexp = "^PRD[0-9]{7}$") @PathVariable("productId") String productId,
      @Valid @RequestBody ProductRequest product)
      throws NotExistException, JsonProcessingException {
    return ResponseEntity.status(200)
        .headers(HeadersUtil.defaultHeaders(correlationId))
        .body(productService.putProduct(product, productId));
  }
}
