package guru.springframework.spring6restmvc.entities;

import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Beer {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
  private UUID id;

  @Version
  private Integer version;

  @NotNull
  @NotBlank
  @Size(max = 50)
  @Column(length = 50, nullable = false)
  private String beerName;
  @NotNull
  private BeerStyle beerStyle;

  @NotNull
  @NotBlank
  @Size(max = 255)
  @Column(length = 255, nullable = false)
  private String upc;
  private Integer quantityOnHand;
  @NotNull
  private BigDecimal price;
  private LocalDateTime createdDate;
  private LocalDateTime updateDate;

}
