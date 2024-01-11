package tech.bouncystream.beerclient.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tech.bouncystream.beerclient.config.WebClientConfiguration;
import tech.bouncystream.beerclient.model.Beer;
import tech.bouncystream.beerclient.model.BeerList;
import tech.bouncystream.beerclient.model.BeerStyle;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class BeerClientImplTest {

    private BeerClient beerClient;

    @BeforeEach
    void setUp() {
        beerClient = new BeerClientImpl(new WebClientConfiguration().webClient());
    }

    @Test
    void testShouldDeliverBeerById() {
        Mono<BeerList> beerListMono = beerClient.beers(null, null, null, null, null);
        final BeerList beerList = beerListMono.block();
        final var firstBeer = beerClient.beerById(beerList.getContent().get(0).getId(), false).block();

        assertThat(firstBeer).isNotNull();
    }

    @Test
    void testShouldDeliverAllBeers() {
        Mono<BeerList> beerListMono = beerClient.beers(null, null, null, null, null);
        final BeerList beerList = beerListMono.block();

        assertThat(beerList).isNotNull();
        assertThat(beerList.getContent().size()).isGreaterThan(0);
    }


    @Test
    void testShouldDeliver10Beers() {
        Mono<BeerList> beerListMono = beerClient.beers(1, 10, null, null, null);
        final BeerList beerList = beerListMono.block();

        assertThat(beerList).isNotNull();
        assertThat(beerList.getContent().size()).isEqualTo(10);
    }

    @Test
    void testShouldDeliverNoBeers() {
        Mono<BeerList> beerListMono = beerClient.beers(20, 10, null, null, null);
        final BeerList beerList = beerListMono.block();

        assertThat(beerList).isNotNull();
        assertThat(beerList.getContent().size()).isEqualTo(0);
    }

    @Test
    void testShouldDeliverBeerByUpc() {
        Mono<BeerList> beerListMono = beerClient.beers(null, null, null, null, null);
        final BeerList beerList = beerListMono.block();
        final var upc = beerList.getContent().get(0).getUpc();
        final var beerByUpc = beerClient.beerByUPC(upc).block();

        assertThat(beerByUpc).isNotNull();
        assertThat(beerByUpc.getUpc()).isEqualTo(upc);
    }

    @Test
    void testShouldCreateABeer() {

        final var beer = Beer.builder()
                .beerStyle(BeerStyle.GOSE)
                .beerName("LOL BEER")
                .createdDate(new Date())
                .lastModifiedDate(new Date())
                .price("12.2")
                .quantityOnHand(14)
                .upc("IDK")
                .version("1.0")
                .build();

        final var responseMono = beerClient.newBeer(beer);
        final var responseEntity = responseMono.block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void testShouldUpdateABeer() {

        Mono<BeerList> beerListMono = beerClient.beers(null, null, null, null, null);
        final BeerList beerList = beerListMono.block();
        final var beerToUpdate = beerClient.beerById(beerList.getContent().get(0).getId(), false).block();

        final var updatedBeer = Beer.builder()
                .beerStyle(BeerStyle.IPA)
                .beerName("YOLO BEER")
                .createdDate(new Date())
                .lastModifiedDate(new Date())
                .price("13.2")
                .quantityOnHand(1)
                .upc("IDK")
                .version("2.0")
                .build();

        final var responseMono = beerClient.updateBeer(beerToUpdate.getId(), updatedBeer, false);
        final var responseEntity = responseMono.block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testShouldDeleteABeer() {
        Mono<BeerList> beerListMono = beerClient.beers(null, null, null, null, null);
        final BeerList beerList = beerListMono.block();
        final var beerToDelete = beerClient.beerById(beerList.getContent().get(0).getId(), false).block();

        final var responseMono = beerClient.deleteBeer(beerToDelete.getId());
        final var responseEntity = responseMono.block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    void testShouldDeleteBeerHandleException() {
        final var responseMono = beerClient.deleteBeer(UUID.randomUUID().toString());

            final var responseEntity = responseMono.onErrorResume(throwable -> {
                if (throwable instanceof WebClientResponseException) {
                    final var ex = (WebClientResponseException) throwable;
                    return Mono.just(ResponseEntity.status(ex.getStatusCode()).build());
                } else {
                    throw new RuntimeException(throwable);
                }
            }).block();

            assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);

    }

    @Test
    void testShouldDeleteBeerNotFound() {
        final var responseMono = beerClient.deleteBeer(UUID.randomUUID().toString());

        assertThrows(WebClientResponseException.class,() -> {
            final var responseEntity = responseMono.block();
            assertThat(responseEntity).isEqualTo(NOT_FOUND);
        });

    }
}

