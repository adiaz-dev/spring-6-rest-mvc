package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

  private Map<UUID, BeerDTO> beerMap;


  public BeerServiceImpl() {
    this.beerMap = new HashMap<>();

    BeerDTO beer1 = BeerDTO.builder()
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

    BeerDTO beer2 = BeerDTO.builder()
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

    BeerDTO beer3 = BeerDTO.builder()
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
  public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
      Integer pageNumber, Integer pageSize) {
    return new PageImpl<>(new ArrayList<>(beerMap.values()));
  }

  @Override
  public Optional<BeerDTO> getBeerById(UUID id) {

    log.debug("Get Beer by Id - in service. Id: " + id.toString());

    return Optional.of(beerMap.get(id));
  }

  @Override
  public BeerDTO saveBeer(BeerDTO beer) {
    BeerDTO newBeer = BeerDTO.builder()
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
  public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
    BeerDTO existingBeer = beerMap.get(beerId);
    if (existingBeer != null) {
      existingBeer.setVersion(existingBeer.getVersion() + 1);
      existingBeer.setBeerName(beer.getBeerName());
      existingBeer.setPrice(beer.getPrice());
      existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
      existingBeer.setUpc(beer.getUpc());
      existingBeer.setBeerStyle(beer.getBeerStyle());
      existingBeer.setUpdateDate(LocalDateTime.now());
    }

    return Optional.of(existingBeer);
  }

  @Override
  public Boolean deleteBeerById(UUID beerId) {
    beerMap.remove(beerId);

    return true;
  }

  @Override
  public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
    BeerDTO existingBeer = beerMap.get(beerId);

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

    return Optional.of(existingBeer);
  }


}
