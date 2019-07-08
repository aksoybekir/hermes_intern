package com.example.hermes_intern.Service;

import com.example.hermes_intern.domain.Customer;
import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.repository.ReactiveDeliveryRepository;
import com.example.hermes_intern.security.JWTUtil;
import com.example.hermes_intern.service.DeliveryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.security.Principal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeliveryServiceTest {

    @Mock
    private ReactiveDeliveryRepository reactiveDeliveryRepository;

    @Mock
    private JWTUtil jwtUtil;

    private DeliveryService deliveryService;


    @Before
    public void setup(){
        deliveryService = new DeliveryService(reactiveDeliveryRepository,jwtUtil);
    }

    @Test
    public void getAllTest(){

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("ON_COURIER");

        when(deliveryService.getAll()).thenReturn(Flux.just(delivery));
        Flux<Delivery> source = this.deliveryService.getAll();


        StepVerifier
                .create(source)
                .expectNext(delivery)
                .expectComplete()
                .verify();
    }

    @Test
    public void getCourierMyDeliveriesTest(){
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "courierid";
            }
        };

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setCourierid("courierid");
        delivery.setStatus("ON_COURIER");

        when(deliveryService.getCourierMyDeliveries(principal)).thenReturn(Flux.just(delivery));
        Flux<Delivery> source = this.deliveryService.getCourierMyDeliveries(principal);

        StepVerifier
                .create(source)
                .consumeNextWith(response ->{
                    assertEquals("courierid", response.getCourierid());
                    assertEquals("ON_COURIER", response.getStatus());
                })
                .expectComplete()
                .verify();

    }

    @Test
    public void getByStatusOnCourierTest(){

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("ON_COURIER");

        when(deliveryService.getByStatusOnCourier()).thenReturn(Flux.just(delivery));
        Flux<Delivery> source = this.deliveryService.getByStatusOnCourier();

        StepVerifier
                .create(source)
                .consumeNextWith(response ->{
                    assertEquals("ON_COURIER", response.getStatus());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getByStatusInBranchTest(){

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("IN_BRANCH");

        when(deliveryService.getByStatusInBranch()).thenReturn(Flux.just(delivery));
        Flux<Delivery> source = this.deliveryService.getByStatusInBranch();

        StepVerifier
                .create(source)
                .consumeNextWith(response ->{
                    assertEquals("IN_BRANCH", response.getStatus());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getByStatusInWarehouseTest(){

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("IN_WAREHOUSE");

        when(deliveryService.getByStatusInWarehouse()).thenReturn(Flux.just(delivery));
        Flux<Delivery> source = this.deliveryService.getByStatusInWarehouse();

        StepVerifier
                .create(source)
                .consumeNextWith(response ->{
                    assertEquals("IN_WAREHOUSE", response.getStatus());
                })                .expectComplete()
                .verify();
    }

    @Test
    public void getByStatusOnWayWarehouseTest(){

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("ON_WAY_WAREHOUSE");

        when(deliveryService.getByStatusOnWayWarehouse()).thenReturn(Flux.just(delivery));
        Flux<Delivery> source = this.deliveryService.getByStatusOnWayWarehouse();

        StepVerifier
                .create(source)
                .consumeNextWith(response -> {
                    assertEquals("ON_WAY_WAREHOUSE", response.getStatus());
                })
                .expectComplete()
                .verify();
    }
}
