package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;
import java.util.List;
import java.util.UUID;

/**
 * Customer service
 * */
public interface CustomerService {

  List<Customer> listCustomers();

  Customer getCustomerById(UUID id);

  Customer saveCustomer(Customer customer);
}
