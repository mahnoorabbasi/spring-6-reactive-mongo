package guru.springframework.reactivemongo.web.fn;

import guru.springframework.reactivemongo.domain.Beer;
import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.services.BeerService;
import guru.springframework.reactivemongo.services.BeerServiceImpl;
import guru.springframework.reactivemongo.services.BeerServiceImplTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@SpringBootTest
@AutoConfigureWebTestClient
class EndpointTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void testListBeerbyStyle() {
        BeerDTO beerDTO=getSavedTestBeer();
        final String beerStyle="TEST";
        beerDTO.setBeerStyle(beerStyle);

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(beerDTO), BeerDTO.class)
                .exchange();

        webTestClient                .mutateWith(mockOAuth2Login())
                .get().uri(UriComponentsBuilder
                        .fromPath(BeerRouterConfig.BEER_PATH)
                        .queryParam("beerStyle", beerStyle)
                        .build().toUri())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.size()").value(equalTo(1));


    }

    @Test
    void testCreateBeer() {
        BeerDTO beerDTO=getSavedTestBeer();

        webTestClient                .mutateWith(mockOAuth2Login())
                .post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(beerDTO), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    public void testListBeers(){
        webTestClient                .mutateWith(mockOAuth2Login())
                .get().uri(BeerRouterConfig.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type","application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));

    }

    @Test
    void testGetById() {
        BeerDTO beerDTO=getSavedTestBeer();
        webTestClient                .mutateWith(mockOAuth2Login())
                .get().uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDTO.class);
    }
    @Test
    void testGetByIdNotFound() {
        webTestClient                .mutateWith(mockOAuth2Login())
                .get().uri(BeerRouterConfig.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testUpdateBeer() {
        BeerDTO beerDTO=getSavedTestBeer();
        beerDTO.setBeerName("New");
        webTestClient                .mutateWith(mockOAuth2Login())
                .put()
                .uri(BeerRouterConfig.BEER_PATH_ID,beerDTO.getId())
                .body(Mono.just(beerDTO), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testPatchIdFound() {
        BeerDTO beerDTO=getSavedTestBeer();
        beerDTO.setBeerName("patchme");

        webTestClient                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .body(Mono.just(beerDTO), BeerDTO.class)
                .exchange()
                .expectStatus()
                .isNoContent();

    }

    @Test
    void testPathIdNotFound() {
        BeerDTO beerDTO=getSavedTestBeer();

        webTestClient                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(BeerRouterConfig.BEER_PATH_ID,999)
                .body(Mono.just(beerDTO), BeerDTO.class)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testDeleteBeer() {
        BeerDTO beerDTO=getSavedTestBeer();
        webTestClient                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .exchange()
                .expectStatus().isNoContent();


    }

    @Test
    void testDeletNotFound() {
        webTestClient                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(BeerRouterConfig.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    private BeerDTO getSavedTestBeer() {
        FluxExchangeResult<BeerDTO> beerDTOFluxExchangeResult=webTestClient                .mutateWith(mockOAuth2Login())
                .post()
                .body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .returnResult(BeerDTO.class);

        List<String> location=beerDTOFluxExchangeResult.getResponseHeaders()
                .get("Location");

        return webTestClient                .mutateWith(mockOAuth2Login())
                .get().uri(BeerRouterConfig.BEER_PATH)
                .exchange()
                .returnResult(BeerDTO.class)
                .getResponseBody()
                .blockFirst();
    }
}