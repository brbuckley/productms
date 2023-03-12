package service.postProduct;

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

public class PostProductStepDefinitions {

  private String api;
  private String supplierId;
  private Product product = new Product();
  private String name;
  private static String token;

  @Before
  public void setup() {
    SetupUtil.setup();
  }

  @BeforeAll
  public static void auth() {
    token = SetupUtil.getToken();
  }

  @Given("I want to create a new Product")
  public void iWantToCreateANewProduct() {
    api = "http://localhost:8080/{supplierId}";
  }

  @And(
      "The new Product should have name: {word}, price: {word}, category: {word}, supplier: {word}")
  public void iAskTheSystemForTheProduct(
      String name, String price, String category, String supplierId) {
    this.supplierId = supplierId;
    product.setPrice(new BigDecimal(price));
    product.setCategory(category);
    product.setName(name);
  }

  @Then("The system gives me a product with a new id, and name: {word}")
  public void theSystemGivesMeAProductWithNewIdAndName(String name) {
    then().body("name", Matchers.equalTo(name));
    then().assertThat().body(matchesJsonSchemaInClasspath("json/ProductResponseSchema.json"));
  }

  @Then("The system shows: {string}")
  public void theSystemShows(String status) {
    then().body("description", Matchers.equalTo(status));
  }

  @When("The new product is received by the system")
  public void theNewProductIsReceivedByTheSystem() {
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
        .post(api, supplierId);
  }

  @When("The invalid product is received by the system")
  public void theInvalidProductIsReceivedByTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body("{\n" + "  \"name\": \"" + product.getName() + "\"\n" + "}")
        .post(api, supplierId);
  }

  @And("The new Product should have name: {word}, supplier: {word}")
  public void theNewProductShouldHaveNameBob(String name, String supplierId) {
    this.name = name;
    this.supplierId = supplierId;
  }

  @And("The new Product should be:")
  public void theNewProductShouldBe(DataTable table) {
    product.setName(table.row(0).get(1));
    product.setPrice(new BigDecimal(table.row(1).get(1)));
    product.setCategory(table.row(2).get(1));
    supplierId = table.row(3).get(1);
  }
}
