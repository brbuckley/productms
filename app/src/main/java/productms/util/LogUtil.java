package productms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

/** Utility class for request logs. */
@Slf4j
@NoArgsConstructor
public class LogUtil {

  /**
   * Logs the URL, query params and body of the request.
   *
   * @param request WebRequest.
   */
  public static void logUtil(WebRequest request) {
    HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
    ContentCachingRequestWrapper nativeRequest =
        (ContentCachingRequestWrapper) ((ServletWebRequest) request).getNativeRequest();
    String requestEntityAsString =
        new String(nativeRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode oneLineBody = objectMapper.readValue(requestEntityAsString, JsonNode.class);
      log.info(
          "Request received for URL: {} with params: {} and body: {}",
          httpRequest.getRequestURI(),
          httpRequest.getQueryString(),
          oneLineBody);
    } catch (JsonProcessingException e) {
      log.info(
          "Request received for URL: {} with params: {} and body: {}",
          httpRequest.getRequestURI(),
          httpRequest.getQueryString(),
          requestEntityAsString);
    }
  }
}
