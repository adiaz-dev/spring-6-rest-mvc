package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  //default settings for page request
  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_PAGE_SIZE = 25;

  public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
    int queryPageNumber = DEFAULT_PAGE_NUMBER;
    int queryPageSize = DEFAULT_PAGE_SIZE;

    if (pageNumber != null && pageNumber > 0 ) {
      queryPageNumber = pageNumber -1 ;
    }

    if(pageSize != null && pageSize > 0){
      if (pageSize > 1000) {
        queryPageSize = 1000;
      } else {
        queryPageSize = pageSize;
      }
    }

    Sort sort = Sort.by(Sort.Order.asc("beerName"));

    return PageRequest.of(queryPageNumber, queryPageSize, sort);
  }


  @Override
  public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
      Integer pageNumber, Integer pageSize) {
    Page<Beer> beerPage;

    //build the page request
    PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

    //filter by name
    if(StringUtils.hasText(beerName) && beerStyle == null) {
      beerPage = listBeersByName(beerName, pageRequest);
    } else if (!StringUtils.hasText(beerName) && beerStyle != null) { //filter by beerStyle
      beerPage = listBeerByStyle(beerStyle, pageRequest);
    } else if (StringUtils.hasText(beerName) && beerStyle != null){
      beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
    } else {
      beerPage = beerRepository.findAll(pageRequest);
    }

    if(showInventory != null && !showInventory) {
      beerPage.forEach(beer -> {
        beer.setQuantityOnHand(null);
      });
    }

    return beerPage.map(beerMapper::beerToBeerDto);
  }

  public Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
    return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageable);
  }

  public Page<Beer> listBeersByName(String beerName, Pageable pageable) {
    return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
  }

  public Page<Beer> listBeerByStyle(BeerStyle beerStyle, Pageable pageable) {
    return beerRepository.findAllByBeerStyle(beerStyle, pageable);
  }

  @Override
  public Optional<BeerDTO> getBeerById(UUID id) {
    return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id).orElse(null)));
  }

  @Override
  public BeerDTO saveBeer(BeerDTO beer) {
    return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
  }

  @Override
  public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {

    AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

    beerRepository.findById(beerId).ifPresentOrElse(foundBeer ->{
      foundBeer.setBeerName(beer.getBeerName());
      foundBeer.setBeerStyle(beer.getBeerStyle());
      foundBeer.setUpc(beer.getUpc());
      foundBeer.setPrice(beer.getPrice());
      atomicReference.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(foundBeer))));
    }, () -> {
      atomicReference.set(Optional.empty());
    });

    return atomicReference.get();
  }

  @Override
  public Boolean deleteBeerById(UUID beerId) {
    if(beerRepository.existsById(beerId)) {
      beerRepository.deleteById(beerId);
      return true;
    }

    return false;
  }

  @Override
  public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
    AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

    beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
      if (StringUtils.hasText(beer.getBeerName())){
        foundBeer.setBeerName(beer.getBeerName());
      }
      if (beer.getBeerStyle() != null){
        foundBeer.setBeerStyle(beer.getBeerStyle());
      }
      if (StringUtils.hasText(beer.getUpc())){
        foundBeer.setUpc(beer.getUpc());
      }
      if (beer.getPrice() != null){
        foundBeer.setPrice(beer.getPrice());
      }
      if (beer.getQuantityOnHand() != null){
        foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
      }
      atomicReference.set(Optional.of(beerMapper
          .beerToBeerDto(beerRepository.save(foundBeer))));
    }, () -> {
      atomicReference.set(Optional.empty());
    });

    return atomicReference.get();
  }
}
