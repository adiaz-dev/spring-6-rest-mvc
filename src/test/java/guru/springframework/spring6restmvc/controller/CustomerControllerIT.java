package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CustomerControllerIT {

  @Autowired
  CustomerController customerController;

  @Autowired
  CustomerRepository customerRepository;

  @Test
  void testListAll() {
    List<CustomerDTO> customers = customerController.getAllCustomers();
    assertThat(customers).size().isEqualTo(4);
  }

  @Rollback
  @Transactional
  @Test
  void testListAllEmptyList() {
    customerRepository.deleteAll();
    List<CustomerDTO> customers = customerController.getAllCustomers();
    assertThat(customers).size().isEqualTo(0);
  }

  @Test
  void testGetById() {
    Customer customer = customerRepository.findAll().get(0);
    CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());
    assertThat(customerDTO).isNotNull();
  }

  @Test
  void testGetByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      customerController.getCustomerById(UUID.randomUUID());
    });
  }

}