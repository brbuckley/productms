package service.getProduct;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hamcrest.Matchers;
import service.util.SetupUtil;

public class GetProductStepDefinitions {

  private String api;
  private String productId;
  private static String token;

  @Before
  public void setup() {
    // This setup does not behave very well at BeforeAll for some reason.
    SetupUtil.setup();
  }

  @BeforeAll
  public static void auth() {
    token = SetupUtil.getToken();
  }

  @Given("I want to get a Product")
  public void iWantToGetAProduct() {
    api = "http://localhost:8080/{productId}";
  }

  @And("I ask the system for the product {word}")
  public void iAskTheSystemForTheProduct(String productId) {
    this.productId = productId;
  }

  @Then("The system gives me a product with id: {word}")
  public void theSystemGivesMeAProductWithId(String productId) {
    then().body("id", Matchers.equalTo(productId));
    then().assertThat().body(matchesJsonSchemaInClasspath("json/ProductResponseSchema.json"));
  }

  @Then("The system shows: {string}")
  public void theSystemShows(String status) {
    then().body("description", Matchers.equalTo(status));
  }

  @When("I call the system")
  public void iCallTheSystem() {
    rest().header("Content-Type", "application/json").auth().oauth2(token).get(api, productId);
  }
}
