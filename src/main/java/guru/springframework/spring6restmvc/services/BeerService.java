package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
public interface BeerService {

  List<BeerDTO> listBeers();

  Optional<BeerDTO> getBeerById(UUID id);

  BeerDTO saveBeer(BeerDTO beer);

  void updateBeerById(UUID beerId, BeerDTO beer);

  void deleteBeerById(UUID beerId);

  void patchBeerById(java.util.UUID beerId, BeerDTO beer);
}
