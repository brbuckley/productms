package productms.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** ErrorResponse. */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {

  private String errorCode = null;
  private String description = null;
}
