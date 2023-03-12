package productms.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/** Creates and customizes an ObjectMapper that is used by the services. */
public class CustomObjectMapper {

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    return new ObjectMapper().configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
  }
}
