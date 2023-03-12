package service.getProducts;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import org.hamcrest.Matchers;
import service.util.SetupUtil;

public class GetProductsStepDefinitions {

  private String api;
  private HashMap<String, String> params;
  private static String token;

  @Before
  public void setup() {
    SetupUtil.setup();
    params = new HashMap<String, String>();
  }

  @BeforeAll
  public static void auth() {
    token = SetupUtil.getToken();
  }

  @Given("I want to get a Product")
  public void iWantToGetAProduct() {
    api = "http://localhost:8080/";
  }

  @And("I ask the system for the product {word}")
  public void iAskTheSystemForTheProduct(String id) {
    params.put("ids", id);
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
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .queryParams(params)
        .get(api);
  }

  @Then("The system gives me a list of products containing id: {word}")
  public void theSystemGivesMeAListOfProductsContainingId(String id) {
    then().assertThat().body(matchesJsonSchemaInClasspath("json/ProductListResponseSchema.json"));
    then().body("[0].id", Matchers.equalTo(id));
  }

  @Given("I want to get all Products")
  public void iWantToGetAllProducts() {
    api = "http://localhost:8080/";
  }

  @And("I ask the system for all products of category: {word}")
  public void iAskTheSystemForAllProductsOfCategory(String category) {
    params.put("category", category);
  }

  @Then("The system gives me a list of products")
  public void theSystemGivesMeAListOfProducts() {
    then().assertThat().body(matchesJsonSchemaInClasspath("json/ProductListResponseSchema.json"));
  }

  @Then("The system gives me a list of products of category: {word}")
  public void theSystemGivesMeAListOfProductsOfCategory(String category) {
    then().assertThat().body(matchesJsonSchemaInClasspath("json/ProductListResponseSchema.json"));
    // Since we only have 2 categories, when I call for 'beer'
    // I check if there is no 'wine' at the response, and vice-versa.
    if (category.equals("beer")) {
      category = "wine";
    } else {
      category = "beer";
    }
    then().assertThat().body("category", not(hasItem(category)));
  }

  @And("I ask the system for all products with name like: {word}")
  public void iAskTheSystemForAllProductsWithNameLike(String name) {
    params.put("name", name);
  }

  @Then("The system gives me a list of products containing name: {word}")
  public void theSystemGivesMeAListOfProductsContainingName(String name) {
    then().assertThat().body(matchesJsonSchemaInClasspath("json/ProductListResponseSchema.json"));
    then().assertThat().body("name", hasItem(name));
  }

  @And("I ask the system for a page of all products with limit: {int}")
  public void iAskTheSystemForAPageOfAllProductsWithLimit(int limit) {
    params.put("limit", Integer.toString(limit));
  }

  @Then("The system gives me a list of {int} products")
  public void theSystemGivesMeAListOfProducts(int limit) {
    then().body("size()", is(limit));
  }

  @And("I ask the system for the suppliers")
  public void iAskTheSystemForTheSuppliers() {
    params.put("fetch-suppliers", "true");
  }

  @Then("The system gives me a list of products and suppliers")
  public void theSystemGivesMeAListOfProductsAndSuppliers() {
    then()
        .assertThat()
        .body(matchesJsonSchemaInClasspath("json/ProductSuppliersResponseSchema.json"));
  }

  @And("I want the lowest price")
  public void iWantTheLowestPrice() {
    params.put("sort", "asc.price");
  }

  @Then("The system gives me a list of products that has the price: {word} at first position")
  public void theSystemGivesMeAListOfProductsThatHasThePriceAtFirstPosition(String price) {
    then().assertThat().body(matchesJsonSchemaInClasspath("json/ProductListResponseSchema.json"));
    then().assertThat().body("[0].price", equalTo(Float.parseFloat(price)));
  }

  @And("I want to sort by: asc.invalid")
  public void iWantToSortByAscInvalid() {
    params.put("sort", "asc.invalid");
  }
}
