package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

  private final CustomerService customerService;
  public static final String CUSTOMER_PATH = "/api/v1/customer";
  public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

  @PostMapping(CUSTOMER_PATH)
  public ResponseEntity handlePost(@RequestBody CustomerDTO customer) {

    //verify customer has at least a name
    if (StringUtils.isEmpty(customer.getCustomerName())) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    CustomerDTO saveCustomer = customerService.saveCustomer(customer);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", CUSTOMER_PATH + "/" + saveCustomer.getId().toString());
    return new ResponseEntity(headers, HttpStatus.CREATED);
  }

  @GetMapping(CUSTOMER_PATH)
  public List<CustomerDTO> getAllCustomers() {
    log.info("Get all customers from the whole database");
    return customerService.listCustomers();
  }

  @GetMapping(CUSTOMER_PATH_ID)
  public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId) {
    log.info("Get customer with id {}", customerId);
    return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
  }

  @PutMapping(CUSTOMER_PATH_ID)
  public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
    customerService.updateCustomer(customerId, customer);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(CUSTOMER_PATH_ID)
  public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID customerId){
    log.debug("Delete Customer by Id");
    customerService.deleteCustomerById(customerId);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PatchMapping(CUSTOMER_PATH_ID)
  public ResponseEntity patchBeerById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
    customerService.patchCustomerById(customerId, customer);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}
