package productms.configuration;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * Request filter. Triggers when receiving a WebRequest (spring) and adds info from a ServletRequest
 * (javax) to get easier access to some extra info like the request body. This filter is used by
 * CustomControllerAdvice when logging the requests.
 */
@Component
public class RequestFilter implements Filter {
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    ContentCachingRequestWrapper contentCachingRequestWrapper =
        new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);

    filterChain.doFilter(contentCachingRequestWrapper, servletResponse);
  }
}
