package guru.springframework.spring6restmvc.controller;

import static guru.springframework.spring6restmvc.controller.BeerControllerTest.jwtRequestPostProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class BeerControllerIT {

  @Autowired
  BeerController beerController;

  @Autowired
  BeerRepository beerRepository;

  @Autowired
  BeerMapper beerMapper;

  @Autowired
  WebApplicationContext wac;

  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac)
        .apply(springSecurity())
        .build();
  }

  @Test
  void testPatchBeer() throws Exception {
    Beer beer = beerRepository.findAll().get(0);

    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("beerName", "New name of beer is very long, such as long that I cannot cound the number of characters used in the beer name that describes the name of this delicious beer made in Mexico");

    MvcResult result = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId() )
            .with(jwtRequestPostProcessor)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerMap)))
        .andExpect(status().isBadRequest())
        .andReturn();

    System.out.println(result.getResponse().getContentAsString());
  }


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
  void testListBeersByName() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .with(jwtRequestPostProcessor)
        .queryParam("beerName","IPA")
        .queryParam("pageSize","800"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()",is(336)));
  }

  @Test
  void testListBeersByStyle() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .with(jwtRequestPostProcessor)
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("pageSize","800"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()",is(548)));
  }

  @Test
  void testListBeersByStyleAndName() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .with(jwtRequestPostProcessor)
            .queryParam("beerName","IPA")
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("pageSize","800"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()",is(310)));
  }

  @Test
  void testListBeersByStyleAndNameAnsShowInventoryTruePage2() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .with(jwtRequestPostProcessor)
            .queryParam("beerName","IPA")
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("showInventory", "true")
            .queryParam("pageNumber", "2")
            .queryParam("pageSize", "50"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()",is(50)))
        .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
  }

  @Test
  void testListBeersByStyleAndNameAnsShowInventoryFalse() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .with(jwtRequestPostProcessor)
            .queryParam("beerName","IPA")
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("showInventory", "false")
            .queryParam("pageSize", "800"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()",is(310)))
        .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.nullValue()));
  }

  @Test
  void testListBeersByStyleAndNameAnsShowInventoryTrue() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .with(jwtRequestPostProcessor)
            .queryParam("beerName","IPA")
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("showInventory", "true")
            .queryParam("pageSize", "800"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()",is(310)))
        .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
  }

  @Test
  void testListBeers() {
    Page<BeerDTO> dtos  = beerController.listBeers(null, null, false, 1, 2413);
    assertThat(dtos.getContent().size()).isEqualTo(1000);
  }

  @Rollback
  @Transactional
  @Test
  void testEmptyListBeers() {
    beerRepository.deleteAll();
    Page<BeerDTO> dtos  = beerController.listBeers(null, null, false, 1, 25);
    assertThat(dtos.getContent().size()).isEqualTo(0);
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

  /**
   * Verifies that the API requires authentication
   * */
  @Test
  void testNoAuth() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
              .queryParam("beerStyle", BeerStyle.IPA.name())
              .queryParam("pageSize","800"))
          .andExpect(status().isUnauthorized());
  }

}