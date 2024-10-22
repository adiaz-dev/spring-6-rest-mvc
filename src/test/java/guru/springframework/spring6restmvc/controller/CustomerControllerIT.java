package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
class CustomerControllerIT {

  @Autowired
  CustomerController customerController;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  CustomerMapper customerMapper;


  @Test
  @Transactional
  @Rollback
  void deleteByIdFound() {
    Customer customer = customerRepository.findAll().get(0);

    //send the update request
    ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    //test that actually that ID is not present in the repository
    assertThat(customerRepository.findById(customer.getId()).isEmpty());
  }

  @Test
  void testDeleteNotFound() {
    assertThrows(NotFoundException.class, () -> {
      customerController.deleteCustomerById(UUID.randomUUID());
    });
  }


  @Test
  void testUpdateNotFound() {
    assertThrows(NotFoundException.class, () -> {
      customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
    });
  }


  @Test
  @Transactional
  @Rollback
  void testUpdateCustomerById() {
    Customer customer = customerRepository.findAll().get(0);
    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
    customerDTO.setId(null);
    customerDTO.setVersion(null);
    final String customerName = "UPDATED";
    customerDTO.setCustomerName(customerName);

    //send the update request
    ResponseEntity responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    //now check that the update happened
    Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
    assertEquals(customerDTO.getCustomerName(), updatedCustomer.getCustomerName());
  }

  @Test
  @Transactional
  @Rollback
  void testSaveCustomer() {
    CustomerDTO customerDTO = CustomerDTO.builder().customerName("Test Customer").build();

    ResponseEntity responseEntity = customerController.handlePost(customerDTO);

    //ensure response is 201
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
    String uuid = location[location.length - 1];
    //test uuid is not null
    assertThat(uuid).isNotNull();

    //now check if the customer is found in the db
    log.info("UUID: {}", uuid);
    Customer customer = customerRepository.findById(UUID.fromString(uuid)).get();
    assertThat(customer).isNotNull();
    assertThat(customer.getCustomerName()).isEqualTo(customerDTO.getCustomerName());
  }


  @Test
  @Transactional
  @Rollback
  void testPatchCustomerById() {
    Customer customer = customerRepository.findAll().get(0);
    CustomerDTO customerToUpdateDTO = customerController.getCustomerById(customer.getId());
    customerToUpdateDTO.setCustomerName("Patched customer name");

    //send the update request
    ResponseEntity responseEntity = customerController.patchBeerById(customerToUpdateDTO.getId(), customerToUpdateDTO);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    //now check that the update happened
    Customer updatedCustomer = customerRepository.findById(customerToUpdateDTO.getId()).get();
    assertEquals(customerToUpdateDTO.getCustomerName(), updatedCustomer.getCustomerName());
    assertEquals(customerToUpdateDTO.getId(), updatedCustomer.getId());

  }


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