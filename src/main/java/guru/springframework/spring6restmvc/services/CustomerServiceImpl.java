package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

  private Map<UUID, Customer> customerMap;

  public CustomerServiceImpl() {
    this.customerMap = new HashMap<>();

    Customer customer1 = Customer.builder().id(UUID.randomUUID()).version(1)
        .customerName("Augusto Diaz").createdDate(LocalDateTime.now())
        .lastModifiedDate(LocalDateTime.now()).build();
    Customer customer2 = Customer.builder().id(UUID.randomUUID()).version(1)
        .customerName("Nashiely Campos").createdDate(LocalDateTime.now())
        .lastModifiedDate(LocalDateTime.now()).build();
    Customer customer3 = Customer.builder().id(UUID.randomUUID()).version(1)
        .customerName("Valentina Diaz").createdDate(LocalDateTime.now())
        .lastModifiedDate(LocalDateTime.now()).build();
    Customer customer4 = Customer.builder().id(UUID.randomUUID()).version(1)
        .customerName("Guadalupe Noriega").createdDate(LocalDateTime.now())
        .lastModifiedDate(LocalDateTime.now()).build();

    customerMap.put(customer1.getId(), customer1);
    customerMap.put(customer2.getId(), customer2);
    customerMap.put(customer3.getId(), customer3);
    customerMap.put(customer4.getId(), customer4);
  }

  @Override
  public List<Customer> listCustomers() {
    return new ArrayList<>(customerMap.values());
  }

  @Override
  public Customer getCustomerById(UUID id) {
    log.debug("Get Customer by Id - in service. Id: " + id.toString());
    return customerMap.get(id);
  }

  @Override
  public Customer saveCustomer(Customer customer) {
    Customer newCustomer = Customer.builder().id(UUID.randomUUID()).version(1)
        .customerName(customer.getCustomerName()).createdDate(LocalDateTime.now())
        .lastModifiedDate(LocalDateTime.now()).build();
    customerMap.put(newCustomer.getId(), newCustomer);

    return newCustomer;
  }

  @Override
  public void updateCustomer(UUID customerId, Customer customer) {
    Customer existingCustomer = getCustomerById(customerId);
    if (existingCustomer != null) {
      existingCustomer.setCustomerName(customer.getCustomerName());
      existingCustomer.setVersion(existingCustomer.getVersion() + 1);
      existingCustomer.setLastModifiedDate(LocalDateTime.now());
    }
  }

  @Override
  public void deleteCustomerById(UUID customerId) {
    customerMap.remove(customerId);
  }

  @Override
  public void patchCustomerById(UUID customerId, Customer customer) {
    Customer existingCustomer = getCustomerById(customerId);
    boolean actuallyModified = false;

    if (StringUtils.hasText(customer.getCustomerName())) {
      existingCustomer.setCustomerName(customer.getCustomerName());
      actuallyModified = true;
    }

    //log the update event
    if (actuallyModified) {
      existingCustomer.setVersion(existingCustomer.getVersion() + 1);
      existingCustomer.setLastModifiedDate(LocalDateTime.now());
    }

  }
}
