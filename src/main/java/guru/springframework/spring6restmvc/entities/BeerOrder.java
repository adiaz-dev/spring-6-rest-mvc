package guru.springframework.spring6restmvc.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
public class BeerOrder {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  private UUID id;

  @Version
  private Long version;

  private String customerRef;

  @ManyToOne
  private Customer customer;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @UpdateTimestamp
  private LocalDateTime lastModifiedDate;

  public BeerOrder(UUID id, Long version, String customerRef, Customer customer,
      LocalDateTime createdDate, LocalDateTime lastModifiedDate,
      Set<BeerOrderLine> beerOrderLines, BeerOrderShipment beerOrderShipment) {
    this.id = id;
    this.version = version;
    this.customerRef = customerRef;
    setCustomer(customer);//additionally it sets the relationship
    this.createdDate = createdDate;
    this.lastModifiedDate = lastModifiedDate;
    this.beerOrderLines = beerOrderLines;
    setBeerOrderShipment(beerOrderShipment);
  }

  public boolean isNew(){
    return this.id == null;
  }

  //Sets the relationship of this beer order with the current customer
  public void setCustomer(Customer customer) {
    this.customer = customer;
    customer.getBeerOrders().add(this);
  }

  @OneToMany(mappedBy = "beerOrder")
  private Set<BeerOrderLine> beerOrderLines;

  @OneToOne(cascade = CascadeType.PERSIST)
  private BeerOrderShipment beerOrderShipment;

  public void setBeerOrderShipment(BeerOrderShipment beerOrderShipment) {
    this.beerOrderShipment = beerOrderShipment;
    beerOrderShipment.setBeerOrder(this);
  }
}
