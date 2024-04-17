package guru.springframework.reactivemongo.web.fn;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class BeerRouterConfig {

    public static final String BEER_PATH="/api/v3/beer";
    public static final String BEER_PATH_ID="/api/v3/beer/{beerId}";

    private final BeerHandler handler;
    @Bean
    public RouterFunction<ServerResponse> beerRoutes(){
        return route()
                .GET(BEER_PATH,accept(APPLICATION_JSON), handler::listAllBeers)
                .GET(BEER_PATH_ID, accept(APPLICATION_JSON),handler::getById)
                .POST(BEER_PATH, accept(APPLICATION_JSON),handler::createNewBeer)
                .PUT(BEER_PATH_ID,accept(APPLICATION_JSON), handler::updateBeerById)
                .PATCH(BEER_PATH_ID,accept(APPLICATION_JSON), handler::patchBeerById)
                .DELETE(BEER_PATH_ID, accept(APPLICATION_JSON),handler::deleteById)
                .build();
    }



}
