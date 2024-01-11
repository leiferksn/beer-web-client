package tech.bouncystream.beerclient.client;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import tech.bouncystream.beerclient.model.Beer;
import tech.bouncystream.beerclient.model.BeerList;

public interface BeerClient {

    Mono<BeerList> beers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnHand);

    Mono<ResponseEntity<Void>> newBeer(Beer beer);

    Mono<Beer> beerById(String beerId, Boolean showInventoryOnHand);

    Mono<ResponseEntity<Void>> updateBeer(String beerId, Beer beer, Boolean showInventoryOnHand);

    Mono<ResponseEntity<Void>> deleteBeer(String beerId);

    Mono<Beer> beerByUPC(String upc);

}
