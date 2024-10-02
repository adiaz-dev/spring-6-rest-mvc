package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CustomerController.CUSTOMER_BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

  private final CustomerService customerService;
  public static final String CUSTOMER_BASE_URL = "/api/v1/customer";

  @PostMapping
  public ResponseEntity handlePost(@RequestBody Customer customer) {
    Customer saveCustomer = customerService.saveCustomer(customer);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", CUSTOMER_BASE_URL + "/" + saveCustomer.getId().toString());
    return new ResponseEntity(headers, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Customer> getAllCustomers() {
    log.info("Get all customers from the whole database");
    return customerService.listCustomers();
  }

  @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
  public Customer getCustomerById(@PathVariable("customerId") UUID customerId) {
    log.info("Get customer with id {}", customerId);
    return customerService.getCustomerById(customerId);
  }

}
