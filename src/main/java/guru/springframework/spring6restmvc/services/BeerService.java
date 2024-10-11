package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
public interface BeerService {

  List<Beer> listBeers();

  Beer getBeerById(UUID id);

  Beer saveBeer(Beer beer);

  void updateBeerById(UUID beerId, Beer beer);

  void deleteBeerById(UUID beerId);

  void patchBeerById(java.util.UUID beerId, Beer beer);
}
