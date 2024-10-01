package guru.springframework.spring6restmvc.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Customer {

  private UUID id;
  private String customerName;
  private Integer version;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;

  //if you add a getter even if lombok is used, then lombok is not going to generate a getter for you, instead your custom getter will be used.
  /*public String getCustomerName() {
    return "Custom getter " + customerName;
  }*/

}
