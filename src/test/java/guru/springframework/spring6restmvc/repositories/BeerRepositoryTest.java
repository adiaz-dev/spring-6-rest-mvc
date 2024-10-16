package guru.springframework.spring6restmvc.repositories;

import static org.junit.jupiter.api.Assertions.*;

import guru.springframework.spring6restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BeerRepositoryTest {

  @Autowired
  BeerRepository beerRepository;

  @Test
  void testSaveBeer() {
    Beer savedBeer = beerRepository.save(Beer.builder().beerName("New beer").build());

    assertNotNull(savedBeer);
    assertNotNull(savedBeer.getId());
  }

}