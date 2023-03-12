package productms.api.model.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductRequestTest {

  private Validator validator;
  private ProductRequest request;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testProductRequest_whenNameTooBig_thenThrow() {
    request = new ProductRequest();
    request.setName("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
    request.setPrice(new BigDecimal("1"));
    Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);
    assertEquals(
        "name size must be between 1 and 50",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }

  @Test
  public void testProductRequest_whenPriceNull_thenThrow() {
    request = new ProductRequest();
    Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);
    assertEquals(
        "price must not be null",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }

  @Test
  public void testProductRequest_whenPrice0_thenThrow() {
    request = new ProductRequest();
    request.setPrice(new BigDecimal("0"));
    Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);
    assertEquals(
        "price must be greater than 0",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }

  @Test
  public void testProductRequest_whenInvalidCategory_thenThrow() {
    request = new ProductRequest();
    request.setPrice(new BigDecimal("1"));
    request.setCategory("invalid");
    Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);
    assertEquals(
        "category must match \"(beer|wine)\"",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }

  @Test
  public void testProductRequest_whenMultipleInvalid_thenThrow() {
    request = new ProductRequest();
    request.setCategory("invalid");
    request.setName("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
    Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);
    // I'm expecting all the 3 violations together
    assertEquals(3, violations.size());
  }
}
