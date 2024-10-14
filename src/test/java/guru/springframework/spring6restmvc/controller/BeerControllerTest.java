package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(BeerController.class)
class BeerControllerTest {

    //brings the mockMvc to be used, and then you do not need to instance/run an embedded tomcat
    @Autowired
    MockMvc mockMvc;

    //This bean is autowired into the beanController, otherwise the service will be null
    @MockBean
    BeerService beerService;

    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Autowired
    ObjectMapper objectMapper;//you get a Jackson object wrapper which you can use to transform objects to json and from json to objects, this one is autoconfigured by spring

    //just create an instance to retrieve one of the beeers
    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        //given
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID())).andExpect(status().isNotFound());
    }


    @Test
    void testPatchBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New name of beer");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId() )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap)))
            .andExpect(status().isNoContent());

        //verify that the service was called at least 1 time
        verify(beerService, times(1)).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        //we want to make sure the UUID was actually sent to the service: check the id sent to the controller is forwarded to the service at the end
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void testDeleteBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId() )
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        //verify that the service was called at least 1 time
        verify(beerService, times(1)).deleteBeerById(uuidArgumentCaptor.capture());

        //we want to make sure the UUID was actually sent to the service: check the id sent to the controller is forwarded to the service at the end
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    /**
     * Test a put request
     * */
    @Test
    void updateBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);


        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId() )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
            .andExpect(status().isNoContent());

        //verify that the service was called at least 1 time
        verify(beerService, times(1)).updateBeerById(any(UUID.class), any(Beer.class));
    }

    /**
     * Test a post request
     * */
    @Test
    void createBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);
        beer.setVersion(null);
        beer.setId(null);

        //this is going to be the request body
        System.out.println(objectMapper.writeValueAsString(beer));

        //given
        given(beerService.saveBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post(BeerController.BEER_PATH )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Test
    void testListBeers() throws Exception {
        //given
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get(BeerController.BEER_PATH )
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", is(3)));

    }

    @Test
    void getBeerById() throws Exception {

        Beer beer = beerServiceImpl.listBeers().get(0);

        //given
        given(beerService.getBeerById(beer.getId())).willReturn(Optional.of(beer));

        mockMvc.perform(get(BeerController.BEER_PATH_ID, beer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(beer.getId().toString())))
            .andExpect(jsonPath("$.beerName", is(beer.getBeerName())));


    }
}