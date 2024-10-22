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

  Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer);

  Boolean deleteCustomerById(UUID customerId);

  Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer);
}
