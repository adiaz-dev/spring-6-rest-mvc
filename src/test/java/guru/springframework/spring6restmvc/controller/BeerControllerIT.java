package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BeerControllerIT {

  @Autowired
  BeerController beerController;

  @Autowired
  BeerRepository beerRepository;

  @Autowired
  BeerMapper beerMapper;



  @Test
  void testDeleteByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      beerController.deleteBeerById(UUID.randomUUID());
    });
  }

  @Rollback
  @Transactional
  @Test
  /**
   * After deleting the beer, it should not be returned, therefore must be null
   * */
  void deleteByIdFound() {
    Beer beer = beerRepository.findAll().get(0);

    //send to delete the found beer
    ResponseEntity responseEntity = beerController.deleteBeerById(beer.getId());

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
    assertThat(beerRepository.findById(beer.getId()).isEmpty());

  }

  @Test
  void testUpdateNotFound() {
    assertThrows(NotFoundException.class, () -> {
      beerController.updateBeerById(UUID.randomUUID(), BeerDTO.builder().build());
    });
  }

  @Rollback
  @Transactional
  @Test
  void updateBeer() {
    Beer beer = beerRepository.findAll().get(0);
    BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);

    beerDTO.setId(null);
    beerDTO.setVersion(null);

    final String beerName = "UPDATED";
    beerDTO.setBeerName(beerName);

    ResponseEntity responseEntity = beerController.updateBeerById(beer.getId(), beerDTO);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

    Beer updatedBeer = beerRepository.findById(beer.getId()).get();
    assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
  }

  @Test
  void testListBeers() {
    List<BeerDTO> dtos  = beerController.listBeers();
    assertThat(dtos.size()).isEqualTo(3);
  }

  @Rollback
  @Transactional
  @Test
  void testEmptyListBeers() {
    beerRepository.deleteAll();
    List<BeerDTO> dtos  = beerController.listBeers();
    assertThat(dtos.size()).isEqualTo(0);
  }

  @Test
  void testGetById() {
    Beer beer = beerRepository.findAll().get(0);
    BeerDTO dto =beerController.getBeerById(beer.getId());
    assertThat(dto).isNotNull();
  }

  @Test
  void testBeerIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
        beerController.getBeerById(UUID.randomUUID());
    });
  }
  @Rollback
  @Transactional
  @Test
  void saveNewBeer() {
    BeerDTO beerDTO = BeerDTO.builder().beerName("New Beer").build();

    ResponseEntity responseEntity = beerController.handlePost(beerDTO);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
    UUID savedUUID = UUID.fromString(locationUUID[locationUUID.length-1]);

    Beer beer = beerRepository.findById(savedUUID).get();
    assertThat(beer).isNotNull();


  }

}