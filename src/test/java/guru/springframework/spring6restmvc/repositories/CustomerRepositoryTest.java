package guru.springframework.spring6restmvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CustomerRepositoryTest {

  @Autowired
  CustomerRepository customerRepository;

  @Test
  void testSaveBeer() {
    Customer savedCustomer = customerRepository.save(Customer.builder().customerName("New customer").build());

    assertNotNull(savedCustomer);
    assertNotNull(savedCustomer.getId());
    assertThat(savedCustomer.getId()).isNotNull();
  }

}