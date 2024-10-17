package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  @Override
  public List<CustomerDTO> listCustomers() {
    return List.of();
  }

  @Override
  public Optional<CustomerDTO> getCustomerById(UUID id) {
    return Optional.empty();
  }

  @Override
  public CustomerDTO saveCustomer(CustomerDTO customer) {
    return null;
  }

  @Override
  public void updateCustomer(UUID customerId, CustomerDTO customer) {

  }

  @Override
  public void deleteCustomerById(UUID customerId) {

  }

  @Override
  public void patchCustomerById(UUID customerId, CustomerDTO customer) {

  }
}
