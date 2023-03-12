package service.putProduct;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import org.hamcrest.Matchers;
import service.model.Product;
import service.util.SetupUtil;

public class PutProductStepDefinitions {

  private String api;
  private String productId;
  private Product product = new Product();
  private static String token;

  @Before
  public void setup() {
    SetupUtil.setup();
  }

  @BeforeAll
  public static void auth() {
    token = SetupUtil.getToken();
  }

  @Given("I want to update a Product")
  public void iWantToUpdateAProduct() {
    api = "http://localhost:8080/{productId}";
  }

  @And("The Product {word} should have name: {word}, price: {word}, category: {word}")
  public void theProductShouldHaveNamePriceCategory(
      String productId, String name, String price, String category) {
    this.productId = productId;
    product.setPrice(new BigDecimal(price));
    product.setCategory(category);
    product.setName(name);
  }

  @When("The product is received by the system")
  public void theProductIsReceivedByTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body(
            "{\n"
                + "  \"name\": \""
                + product.getName()
                + "\",\n"
                + "  \"price\": "
                + product.getPrice()
                + ",\n"
                + "  \"category\": \""
                + product.getCategory()
                + "\"\n"
                + "}")
        .put(api, productId);
  }

  @Then("The system gives me a product with id {word} and name: {word}")
  public void theSystemGivesMeAProductWithIdAndName(String productId, String name) {
    then().body("id", Matchers.equalTo(productId));
    then().body("name", Matchers.equalTo(name));
    then().assertThat().body(matchesJsonSchemaInClasspath("json/ProductResponseSchema.json"));
  }

  @And("The Product should be:")
  public void theProductShouldBe(DataTable table) {
    productId = table.row(0).get(1);
    product.setName(table.row(1).get(1));
    product.setPrice(new BigDecimal(table.row(2).get(1)));
    product.setCategory(table.row(3).get(1));
  }

  @And("The Product {word} should have name: {word}")
  public void theProductShouldHaveName(String productId, String name) {
    this.productId = productId;
    product.setName(name);
  }

  @When("The incomplete product is received by the system")
  public void theIncompleteProductIsReceivedByTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body("{\n" + "  \"name\": \"" + product.getName() + "\"\n" + "}")
        .put(api, productId);
  }

  @Then("The system shows: {string}")
  public void theSystemShows(String status) {
    then().body("description", Matchers.equalTo(status));
  }
}
