package guru.springframework.spring6restmvc.repositories;

import static org.junit.jupiter.api.Assertions.*;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BeerRepositoryTest {

  @Autowired
  BeerRepository beerRepository;

  @Test
  void testSaveBeer() {
    Beer savedBeer = beerRepository.save(Beer.builder()
        .beerName("New beer")
            .beerStyle(BeerStyle.ALE)
            .upc("asdf4323e4rt")
            .price(new BigDecimal("11.99"))
        .build());

    //force to write inmediatly to the DB
    beerRepository.flush();

    assertNotNull(savedBeer);
    assertNotNull(savedBeer.getId());
  }

}