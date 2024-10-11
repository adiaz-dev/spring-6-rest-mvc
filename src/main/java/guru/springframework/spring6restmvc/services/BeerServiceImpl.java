package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.util.StringUtils;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

  private Map<UUID, Beer> beerMap;


  public BeerServiceImpl() {
    this.beerMap = new HashMap<>();

    Beer beer1 = Beer.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Galaxy Cat")
        .beerStyle(BeerStyle.PALE_ALE)
        .upc("12356")
        .price(new BigDecimal("12.99"))
        .quantityOnHand(122)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Beer beer2 = Beer.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Crank")
        .beerStyle(BeerStyle.PALE_ALE)
        .upc("12356222")
        .price(new BigDecimal("11.99"))
        .quantityOnHand(392)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Beer beer3 = Beer.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Sunshine City")
        .beerStyle(BeerStyle.IPA)
        .upc("12356")
        .price(new BigDecimal("13.99"))
        .quantityOnHand(144)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    beerMap.put(beer1.getId(), beer1);
    beerMap.put(beer2.getId(), beer2);
    beerMap.put(beer3.getId(), beer3);
  }

  @Override
  public List<Beer> listBeers() {
    return new ArrayList<>(beerMap.values());
  }

  @Override
  public Beer getBeerById(UUID id) {

    log.debug("Get Beer by Id - in service. Id: " + id.toString());

    return beerMap.get(id);
  }

  @Override
  public Beer saveBeer(Beer beer) {
    Beer newBeer = Beer.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName(beer.getBeerName())
        .price(beer.getPrice())
        .quantityOnHand(beer.getQuantityOnHand())
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .upc(beer.getUpc())
        .beerStyle(beer.getBeerStyle()).beerStyle(beer.getBeerStyle()).build();

    beerMap.put(newBeer.getId(), newBeer);

    return newBeer;
  }

  @Override
  public void updateBeerById(UUID beerId, Beer beer) {
    Beer existingBeer = beerMap.get(beerId);
    if (existingBeer != null) {
      existingBeer.setVersion(existingBeer.getVersion() + 1);
      existingBeer.setBeerName(beer.getBeerName());
      existingBeer.setPrice(beer.getPrice());
      existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
      existingBeer.setUpc(beer.getUpc());
      existingBeer.setBeerStyle(beer.getBeerStyle());
      existingBeer.setUpdateDate(LocalDateTime.now());
    }
  }

  @Override
  public void deleteBeerById(UUID beerId) {
    beerMap.remove(beerId);
  }

  @Override
  public void patchBeerById(UUID beerId, Beer beer) {
    Beer existingBeer = beerMap.get(beerId);

    if(StringUtils.hasText(beer.getBeerName())){
      existingBeer.setBeerName(beer.getBeerName());
    }

    if (beer.getBeerStyle() != null) {
      existingBeer.setBeerStyle(beer.getBeerStyle());
    }

    if(beer.getPrice() != null){
      existingBeer.setPrice(beer.getPrice());
    }

    if(beer.getQuantityOnHand() != null){
      existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
    }

    if(StringUtils.hasText(beer.getUpc())){
      existingBeer.setUpc(beer.getUpc());
    }

    //log the update
    existingBeer.setUpdateDate(LocalDateTime.now());
    existingBeer.setVersion(existingBeer.getVersion() + 1);

  }
}
