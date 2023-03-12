package productms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import productms.api.model.product.ProductCategory;
import productms.api.model.product.ProductRequest;
import productms.api.model.product.ProductResponse;
import productms.exception.NotExistException;
import productms.mapper.ProductMapper;
import productms.persistence.domain.ProductEntity;
import productms.persistence.domain.SupplierEntity;
import productms.persistence.repository.ProductRepository;
import productms.persistence.repository.SupplierRepository;

/** Product services. */
@Service
@Slf4j
public class ProductService {

  private ObjectMapper mapper;
  private ProductRepository productRepository;
  private SupplierRepository supplierRepository;
  private ProductMapper productMapper;

  /**
   * Custom constructor. Initializes the repositories and mapper.
   *
   * @param productRepository Product repository.
   * @param supplierRepository Supplier repository.
   * @param mapper Object mapper.
   * @param productMapper Product mapper.
   */
  public ProductService(
      ProductRepository productRepository,
      SupplierRepository supplierRepository,
      ObjectMapper mapper,
      ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.supplierRepository = supplierRepository;
    this.mapper = mapper;
    this.productMapper = productMapper;
  }

  /**
   * Persists a new product to the DataBase.
   *
   * @param product Product Minimal Object.
   * @param supplierId Supplier Id.
   * @return Product created.
   * @throws NotExistException Not Exist Exception.
   * @throws JsonProcessingException Json Processing Exception.
   */
  public ProductResponse postProduct(ProductRequest product, String supplierId)
      throws NotExistException, JsonProcessingException {
    log.info(
        "POST request received for supplier: {} with body: {}",
        supplierId,
        mapper.writeValueAsString(product));
    SupplierEntity supplier = supplierRepository.findBySupplierId(supplierId);
    if (supplier == null) {
      throw new NotExistException("Supplier");
    }
    ProductEntity productEntity = new ProductEntity();
    productMapper.toEntity(productEntity, product);
    productEntity.setSupplier(supplier);
    productEntity = productRepository.save(productEntity);
    log.info(
        "Added new Product: {} for supplier {}",
        productEntity.getProductId(),
        productEntity.getSupplier().getSupplierId());
    return productMapper.fromEntity(productEntity);
  }

  /**
   * Gets a product from the DataBase.
   *
   * @param productId ProductId.
   * @param fetchSuppliers Boolean to decide if suppliers are fetched.
   * @return Product.
   * @throws NotExistException Not Exist Exception.
   */
  public ProductEntity getProductEntity(String productId, boolean fetchSuppliers)
      throws NotExistException {
    ProductEntity product = productRepository.findByProductId(productId);
    if (product == null) {
      log.info("Product Not Found {}", productId);
      throw new NotExistException("Product");
    } else {
      log.info("Found Product: {}", productId);
      if (!fetchSuppliers) {
        product.setSupplier(null);
      }
    }
    return product;
  }

  /**
   * Gets a product from the DataBase.
   *
   * @param productId ProductId.
   * @param fetchSuppliers Boolean to decide if suppliers are fetched.
   * @return Product.
   * @throws NotExistException Not Exist Exception.
   */
  public ProductResponse getProduct(String productId, boolean fetchSuppliers)
      throws NotExistException {
    log.info(
        "GET request received for product: {} with fetch-suppliers: {}",
        productId,
        String.valueOf(fetchSuppliers));
    ProductEntity productEntity = getProductEntity(productId, fetchSuppliers);
    return productMapper.fromEntity(productEntity);
  }

  /**
   * Gets all products using given filters.
   *
   * @param ids Ids to filter.
   * @param category Category.
   * @param name A name like the product's.
   * @param sort Sort by asc/desc . name/price
   * @param limit Limit paging results.
   * @param offset Page to start.
   * @param fetchSuppliers Fetch suppliers flag.
   * @return List of Products.
   * @throws NotExistException Not Exist Exception.
   */
  public List<ProductResponse> getProducts(
      String ids,
      String category,
      String name,
      String sort,
      int limit,
      int offset,
      boolean fetchSuppliers)
      throws NotExistException {
    log.info(
        "GET request received with ids: {} | category: {} | name: {} | sort: {} "
            + "| limit: {} | offset: {} | fetch-suppliers: {}",
        ids,
        category,
        name,
        sort,
        String.valueOf(limit),
        String.valueOf(offset),
        String.valueOf(fetchSuppliers));
    List<String> idsList = new ArrayList<>();
    String[] sortArray = sort.split("\\.");
    Sort.Direction direction =
        sortArray[0].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable =
        PageRequest.of(offset, limit, Sort.by(new Sort.Order(direction, sortArray[1])));
    if (ids != null) {
      idsList = Arrays.asList(ids.split(","));
    }
    if (name != null) {
      name = "%" + name + "%";
    }
    ProductCategory productCategory = category == null ? null : ProductCategory.fromValue(category);
    List<ProductEntity> products =
        productRepository.findByIdsPagination(idsList, productCategory, name, pageable);
    if (products.isEmpty()) {
      log.info("Products Not Found");
      throw new NotExistException("Product");
    } else {
      log.info("Found Products");
      if (!fetchSuppliers) {
        for (int i = 0; i < products.size(); i++) {
          products.get(i).setSupplier(null);
        }
      }
    }
    List<ProductResponse> response = new ArrayList<>();
    for (ProductEntity product : products) {
      response.add(productMapper.fromEntity(product));
    }
    return response;
  }

  /**
   * Update product.
   *
   * @param product Product with new info.
   * @param productId Product Id.
   * @return Updated product.
   * @throws NotExistException Not Exist Exception.
   * @throws JsonProcessingException Json Processing Exception.
   */
  public ProductResponse putProduct(ProductRequest product, String productId)
      throws NotExistException, JsonProcessingException {
    log.info(
        "PUT request received for product: {} with body: {}",
        productId,
        mapper.writeValueAsString(product));
    ProductEntity productEntity = productRepository.findByProductId(productId);
    if (productEntity == null) {
      log.info("Product Not Found {}", productId);
      throw new NotExistException("Product");
    } else {
      log.info("Found Product: {}", productId);
      productEntity.setPrice(product.getPrice());
      productEntity.setCategory(
          product.getCategory() == null ? null : ProductCategory.fromValue(product.getCategory()));
      productEntity.setName(product.getName());
      productEntity = productRepository.save(productEntity);
    }
    return productMapper.fromEntity(productEntity);
  }
}
