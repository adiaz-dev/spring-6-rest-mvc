package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

/**
 * Created by jt, Spring Framework Guru.
 */
public interface BeerService {

  Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
      Integer pageNumber, Integer pageSize);

  Optional<BeerDTO> getBeerById(UUID id);

  BeerDTO saveBeer(BeerDTO beer);

  Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);

  Boolean deleteBeerById(UUID beerId);

  Optional<BeerDTO> patchBeerById(java.util.UUID beerId, BeerDTO beer);
}
