//package guru.springframework.reactivemongo.service;
//
//import guru.springframework.reactivemongo.domain.Beer;
//import guru.springframework.reactivemongo.mappers.BeerMapper;
//import guru.springframework.reactivemongo.model.BeerDTO;
//import guru.springframework.reactivemongo.repositories.BeerRepository;
//import guru.springframework.reactivemongo.services.BeerService;
//import org.bson.RawBsonArray;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import reactor.core.publisher.Mono;
//
//import java.math.BigDecimal;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import static org.awaitility.Awaitility.await;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class BeerServiceImplTest {
//
//
//    @Autowired
//    BeerService beerService;
//    @Autowired
//    BeerMapper beerMapper;
//
//    BeerDTO beerDTO;
//    @BeforeEach
//    void setUp() {
//        beerDTO=beerMapper.beerToBeerDto(getTestBeer());
//    }
//
//    @Test
//    void saveBeer() throws InterruptedException {
//        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
//        Mono<BeerDTO> saveMono=beerService.saveBeer(Mono.just(beerDTO));
//        saveMono.subscribe(savedDto->
//                System.out.println(savedDto.getId()));
//
//        atomicBoolean.set(true);
////    Thread.sleep(1000l);
//        await().untilTrue(atomicBoolean);
//    }
//
//    public static Beer getTestBeer(){
//        return Beer.builder()
//                .beerName("Boi")
//                .beerStyle("IPA")
//                .price(BigDecimal.TEN)
//                .quantityOnHand(12)
//                .upc("123213")
//                .build();
//    }
//
//}