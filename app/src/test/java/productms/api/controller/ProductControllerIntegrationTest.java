package productms.api.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import productms.ProductMsResponseUtil;
import productms.persistence.repository.ProductRepository;
import productms.persistence.repository.SupplierRepository;
import productms.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ProductService customerService;
  // For some reason I need to mock ALL the repos for the EM to work.
  @MockBean private SupplierRepository customerRepository;
  @MockBean private ProductRepository productRepository;

  MediaType MEDIA_TYPE_JSON_UTF8 =
      new MediaType("application", "json", java.nio.charset.Charset.forName("UTF-8"));

  @Test
  public void testGetProduct_whenValid_then200() throws Exception {
    given(customerService.getProduct("PRD0000001", false))
        .willReturn(ProductMsResponseUtil.noSupplierResponse());
    mockMvc
        .perform(get("/PRD0000001").with(jwt()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    "{\"name\":\"Heineken\",\"price\":10,\"category\":\"beer\",\"id\":\"PRD0000001\"}"));
  }

  @Test
  public void testGetProduct_whenInvalidProductId_then400() throws Exception {
    mockMvc.perform(get("/invalid").with(jwt())).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void testPostProduct_whenValid_then201() throws Exception {
    given(customerService.postProduct(ProductMsResponseUtil.defaultProductRequest(), "SUP0000001"))
        .willReturn(ProductMsResponseUtil.defaultProductResponse());
    MockHttpServletRequestBuilder request = post("/SUP0000001");
    request.content("{\"name\":\"Heineken\",\"price\":10,\"category\":\"beer\"}");
    request.contentType(MEDIA_TYPE_JSON_UTF8);
    mockMvc
        .perform(request.with(jwt()))
        .andExpect(status().is(201))
        .andExpect(
            content()
                .string(
                    "{\"name\":\"Heineken\",\"price\":10,\"category\":\"beer\",\"id\":\"PRD0000001\",\"supplier\":{\"id\":\"SUP0000001\",\"name\":\"Supplier A\"}}"));
  }

  @Test
  public void testPostProduct_whenInvalidGender_then400() throws Exception {
    MockHttpServletRequestBuilder request = post("/SUP0000001");
    request.content("{\"name\":\"Heineken\",\"category\":\"beer\"}");
    request.contentType(MEDIA_TYPE_JSON_UTF8);
    mockMvc.perform(request.with(jwt())).andExpect(status().isBadRequest());
  }

  @Test
  public void testPutProduct_whenValid_then200() throws Exception {
    given(customerService.putProduct(ProductMsResponseUtil.updateProductRequest(), "PRD0000001"))
        .willReturn(ProductMsResponseUtil.updateProductResponse());
    MockHttpServletRequestBuilder request = put("/PRD0000001");
    request.content("{\"name\":\"Heineken Light\",\"price\":12,\"category\":\"beer\"}");
    request.contentType(MEDIA_TYPE_JSON_UTF8);
    mockMvc
        .perform(request.with(jwt()))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    "{\"name\":\"Heineken Light\",\"price\":12,\"category\":\"beer\",\"id\":\"PRD0000001\",\"supplier\":{\"id\":\"SUP0000001\",\"name\":\"Supplier A\"}}"));
  }

  @Test
  public void testPutProduct_whenInvalidProductId_then400() throws Exception {
    MockHttpServletRequestBuilder request = put("/invalid");
    request.content("{\"name\":\"Heineken Light\",\"category\":\"beer\"}");
    request.contentType(MEDIA_TYPE_JSON_UTF8);
    mockMvc.perform(request.with(jwt())).andExpect(status().isBadRequest());
  }

  @Test
  public void testGetProducts_whenValid_then200() throws Exception {
    given(customerService.getProducts(null, null, null, "asc.name", 50, 0, false))
        .willReturn(ProductMsResponseUtil.productResponseList());
    mockMvc
        .perform(get("/").with(jwt()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    "[{\"name\":\"Heineken\",\"price\":10,\"category\":\"beer\",\"id\":\"PRD0000001\",\"supplier\":{\"id\":\"SUP0000001\",\"name\":\"Supplier A\"}},{\"name\":\"Amstel\",\"price\":10,\"category\":\"beer\",\"id\":\"PRD0000002\",\"supplier\":{\"id\":\"SUP0000001\",\"name\":\"Supplier A\"}}]"));
  }

  @Test
  public void testGetProducts_whenInvalidIds_then400() throws Exception {
    mockMvc
        .perform(get("/?ids=invalid").with(jwt()))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}
