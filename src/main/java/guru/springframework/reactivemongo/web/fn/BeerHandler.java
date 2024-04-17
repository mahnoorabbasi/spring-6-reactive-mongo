package guru.springframework.reactivemongo.web.fn;

import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.services.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@AllArgsConstructor
public class BeerHandler {

    private final BeerService beerService;
    private final Validator validator;

    public void validate(BeerDTO beerDTO){
        Errors errors=new BeanPropertyBindingResult(beerDTO, "beerDto");
        validator.validate(beerDTO,errors);
        if(errors.hasErrors()){
            throw new ServerWebInputException(errors.toString());
        }

    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        return beerService.getById(request.pathVariable("beerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(foundDto-> beerService.deleteBeerById(foundDto.getId()))
                .then(ServerResponse.noContent().build());

//        return beerService.deleteBeerById(request.pathVariable("beerId"))
//                .then(ServerResponse.noContent().build());

    }
    public Mono<ServerResponse> patchBeerById(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate)
                .flatMap(beerDto-> beerService
                        .patchBeer(request.pathVariable("beerId"),beerDto))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto-> ServerResponse.noContent().build());

    }

    public Mono<ServerResponse> updateBeerById(ServerRequest request){
        Mono<BeerDTO> beerDTOMono1 = request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate);

        return beerDTOMono1.flatMap(beerDTO -> beerService.updateBeer(request.pathVariable("beerId"),beerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto->ServerResponse.noContent().build());

    }
    public Mono<ServerResponse> createNewBeer(ServerRequest request){
        Mono<BeerDTO> beerDTOMono = beerService.saveBeer(request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate));
        return beerDTOMono
                .flatMap(beerDTO -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_PATH_ID)
                                .build(beerDTO.getId()))
                        .build());
    }

    Mono<ServerResponse> getById(ServerRequest request){
        Mono<ServerResponse> sResponse = ServerResponse.ok()
                .body(beerService.getById(request.pathVariable("beerId"))
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),
                        BeerDTO.class);
        return sResponse;
    }


//    Mono<ServerResponse> listAllBeers(ServerRequest request){
//        return ServerResponse.ok()
//                .body(beerService.listBeers(), BeerDTO.class);
//
//    }

    Mono<ServerResponse> listAllBeers(ServerRequest request) {
        Flux<BeerDTO> flux;

        if (request.queryParam("beerStyle").isPresent()) {
            flux = beerService.findByBeerStyle(request.queryParam("beerStyle").get());
        }else flux=beerService.listBeers();


        return ServerResponse.ok()
                .body(flux, BeerDTO.class);

    }
}
