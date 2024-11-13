package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.entities.BeerOrderShipment;
import guru.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest //bring the full context of spring to have all the data loaded
class BeerOrderRepositoryTest {

  @Autowired
  BeerOrderRepository beerOrderRepository;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  BeerRepository beerRepository;

  Customer testCustomer;
  Beer testBeer;

  @BeforeEach
  void setUp() {
    testCustomer = customerRepository.findAll().get(0);
    testBeer = beerRepository.findAll().get(0);
  }

  @Transactional//without an explicit transaction, the repository will not run in transactional mode
  @Test
  void testBeerOrders(){
    BeerOrder beerOrder = BeerOrder.builder()
        .customerRef("Test Order")
        .customer(testCustomer)
        .beerOrderShipment(BeerOrderShipment.builder()
            .trackingNumber("12345r")
            .build())
        .build();

    // instead of continued using beerOrder after being persistent into the DB, it is better to
    // retrieve it explicitly, otherwise it is not guarantee you can point correctly to the instance
    BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
    System.out.println(savedBeerOrder.getCustomerRef());

  }


}