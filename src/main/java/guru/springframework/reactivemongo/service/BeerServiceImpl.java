//package guru.springframework.reactivemongo.service;
//
//import guru.springframework.reactivemongo.mappers.BeerMapper;
//import guru.springframework.reactivemongo.model.BeerDTO;
//import guru.springframework.reactivemongo.repositories.BeerRepository;
//import guru.springframework.reactivemongo.services.BeerService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//@Service
//@RequiredArgsConstructor
//public class BeerServiceImpl implements BeerService {
//
//    private final BeerMapper beerMapper;
//    private final BeerRepository beerRepository;
//
//    @Override
//    public Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO) {
//        return beerDTO.map(beerMapper::beerDtoToBeer)
//                .flatMap(beerRepository::save)
//                .map(beerMapper::beerToBeerDto);
//
//    }
//
//    @Override
//    public Mono<BeerDTO> getyId(String beerId) {
//        return null;
//    }
//}
