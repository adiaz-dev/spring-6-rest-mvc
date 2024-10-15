package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Customer service
 * */
public interface CustomerService {

  List<CustomerDTO> listCustomers();

  Optional<CustomerDTO> getCustomerById(UUID id);

  CustomerDTO saveCustomer(CustomerDTO customer);

  void updateCustomer(UUID customerId, CustomerDTO customer);

  void deleteCustomerById(UUID customerId);

  void patchCustomerById(java.util.UUID customerId, CustomerDTO customer);
}
