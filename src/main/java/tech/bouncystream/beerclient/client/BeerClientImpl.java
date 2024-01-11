package tech.bouncystream.beerclient.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.bouncystream.beerclient.config.WebClientProperties;
import tech.bouncystream.beerclient.model.Beer;
import tech.bouncystream.beerclient.model.BeerList;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

    private final WebClient webClient;

    @Override
    public Mono<BeerList> beers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnHand) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(WebClientProperties.BEER_V1_PATH)
                        .queryParamIfPresent("pageNumber", Optional.ofNullable(pageNumber))
                        .queryParamIfPresent("pageSize", Optional.ofNullable(pageSize))
                        .queryParamIfPresent("beerName", Optional.ofNullable(beerName))
                        .queryParamIfPresent("beerStyle", Optional.ofNullable(beerStyle))
                        .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
                        .build())
                .retrieve()
                .bodyToMono(BeerList.class);
    }

    @Override
    public Mono<ResponseEntity<Void>> newBeer(Beer beer) {
        return webClient.post().uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_PATH)
                .build())
                .body(BodyInserters.fromValue(beer))
                .retrieve().toBodilessEntity();
    }

    @Override
    public Mono<Beer> beerById(String beerId, Boolean showInventoryOnHand) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .path(WebClientProperties.BEER_BY_ID_V1_PATH)
                .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
                .build(beerId)).retrieve().bodyToMono(Beer.class);
    }

    @Override
    public Mono<ResponseEntity<Void>> updateBeer(String beerId, Beer beer, Boolean showInventoryOnHand) {
        return webClient.put().uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_BY_ID_V1_PATH).build(beerId))
                .body(BodyInserters.fromValue(beer))
                .retrieve().toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteBeer(String beerId) {
        return webClient.delete().uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_BY_ID_V1_PATH).build(beerId))
                .retrieve().toBodilessEntity();
    }

    @Override
    public Mono<Beer> beerByUPC(String upc) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .path(WebClientProperties.BEER_UPC_V1_PATH)
                .build(upc)).retrieve().bodyToMono(Beer.class);
    }
}
