package guru.springframework.spring6restmvc.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  private UUID id;

  @Version
  private Integer version;

  private String customerName;

  @Column(length = 255)
  private String email;

  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;

  @OneToMany(mappedBy = "customer")
  private Set<BeerOrder> beerOrders = new HashSet<>();
}
