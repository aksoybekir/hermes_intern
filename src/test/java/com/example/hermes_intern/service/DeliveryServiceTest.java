package com.example.hermes_intern.service;

import com.example.hermes_intern.domain.Actions;
import com.example.hermes_intern.domain.Customer;
import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.model.DeliveryCount;
import com.example.hermes_intern.model.DeliveryLocation;
import com.example.hermes_intern.domain.DeliveryStatus;
import com.example.hermes_intern.repository.ReactiveDeliveryRepository;
import com.example.hermes_intern.security.JWTUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeliveryServiceTest {

    @Mock
    private ReactiveDeliveryRepository reactiveDeliveryRepository;

    @Mock
    private JWTUtil jwtUtil;

    private DeliveryService deliveryService;


    @Before
    public void setUp() {
        deliveryService = new DeliveryService(reactiveDeliveryRepository, jwtUtil);
    }

    @Test
    public void getAllTest() {

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("ON_COURIER");

        when(reactiveDeliveryRepository.findAll()).thenReturn(Flux.just(delivery));

        StepVerifier
                .create(this.deliveryService.getAll())
                .expectNext(delivery)
                .expectComplete()
                .verify();
    }

    @Test
    public void getCourierMyDeliveriesTest() {
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

        when(reactiveDeliveryRepository.findByCourierid(principal.getName())).thenReturn(Flux.just(delivery));

        StepVerifier
                .create(this.deliveryService.getCourierMyDeliveries(principal))
                .consumeNextWith(response -> {
                    assertEquals("courierid", response.getCourierid());
                    assertEquals("ON_COURIER", response.getStatus());
                })
                .expectComplete()
                .verify();

    }

    @Test
    public void getByStatusOnCourierTest() {

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("ON_COURIER");

        when(reactiveDeliveryRepository.findDeliveriesByStatus(delivery.getStatus())).thenReturn(Flux.just(delivery));

        StepVerifier
                .create(this.deliveryService.getByStatusOnCourier())
                .consumeNextWith(response -> {
                    assertEquals("ON_COURIER", response.getStatus());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getByStatusInBranchTest() {

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("IN_BRANCH");

        when(reactiveDeliveryRepository.findDeliveriesByStatus(delivery.getStatus())).thenReturn(Flux.just(delivery));

        StepVerifier
                .create(this.deliveryService.getByStatusInBranch())
                .consumeNextWith(response -> {
                    assertEquals("IN_BRANCH", response.getStatus());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getByStatusInWarehouseTest() {

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("IN_WAREHOUSE");

        when(reactiveDeliveryRepository.findDeliveriesByStatus(delivery.getStatus())).thenReturn(Flux.just(delivery));

        StepVerifier
                .create(this.deliveryService.getByStatusInWarehouse())
                .consumeNextWith(response -> {
                    assertEquals("IN_WAREHOUSE", response.getStatus());
                }).expectComplete()
                .verify();
    }

    @Test
    public void getByStatusOnWayWarehouseTest() {

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("ON_WAY_WAREHOUSE");

        when(reactiveDeliveryRepository.findDeliveriesByStatus(delivery.getStatus())).thenReturn(Flux.just(delivery));

        StepVerifier
                .create(this.deliveryService.getByStatusOnWayWarehouse())
                .consumeNextWith(response -> {
                    assertEquals("ON_WAY_WAREHOUSE", response.getStatus());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getLocationTest() {

        Delivery delivery = new Delivery();
        delivery.setId("deliveryid");
        delivery.setStatus("ON_COURIER");

        DeliveryLocation deliveryLocation = new DeliveryLocation();
        deliveryLocation.setLocation("ON_COURIER");
        deliveryLocation.setId("locationid");

        when(reactiveDeliveryRepository.findById("deliveryid")).thenReturn(Mono.just(delivery));

        StepVerifier
                .create(this.deliveryService.getLocation("deliveryid"))
                .consumeNextWith(response -> {
                    assertEquals(deliveryLocation.getLocation(), response.getLocation());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getLocationByCustomerTest() {

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "customerid";
            }
        };

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("ON_COURIER");

        Customer customer = new Customer();
        customer.setId("customerid");

        delivery.setCustomer(customer);

        when(reactiveDeliveryRepository.findById("mytestdeliveryid")).thenReturn(Mono.just(delivery));

        Mono<ResponseEntity<?>> responseEntityMono = this.deliveryService.getLocationbyCustomer("mytestdeliveryid", principal);

        StepVerifier
                .create(responseEntityMono)
                .consumeNextWith(response -> {
                    assertEquals(200, response.getStatusCodeValue());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getActionsTest() {

        Delivery delivery = new Delivery();
        delivery.setId("deliveryid");
        delivery.setStatus("ON_COURIER");

        Actions actions = new Actions();
        actions.setDateCourierRecieved(null);

        delivery.setActions(actions);

        when(reactiveDeliveryRepository.findById("deliveryid")).thenReturn(Mono.just(delivery));

        StepVerifier
                .create(this.deliveryService.getActions("deliveryid"))
                .consumeNextWith(response -> {
                    assertEquals(actions.getDateCourierRecieved(), response.getDateCourierRecieved());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getActionsByCustomerTest() {

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "customerid";
            }
        };

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setStatus("ON_COURIER");

        Actions actions = new Actions();
        actions.setDateCourierRecieved(null);

        delivery.setActions(actions);

        Customer customer = new Customer();
        customer.setId("customerid");

        delivery.setCustomer(customer);

        when(reactiveDeliveryRepository.findById("mytestdeliveryid")).thenReturn(Mono.just(delivery));


        StepVerifier
                .create(this.deliveryService.getLocationbyCustomer("mytestdeliveryid", principal))
                .consumeNextWith(response -> {
                    assertEquals(200, response.getStatusCodeValue());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getDeliveryCountTest() {

        DeliveryCount deliveryCount = new DeliveryCount();
        deliveryCount.setDeliveryCount((long) 5);
        deliveryCount.setStatus("ON_COURIER");


        when(reactiveDeliveryRepository.countByStatus("ON_COURIER")).thenReturn(Mono.just((long) 5));

        StepVerifier
                .create(this.deliveryService.getDeliveryCount("ON_COURIER"))
                .consumeNextWith(response -> {
                    assertEquals(deliveryCount.getDeliveryCount(), response.getDeliveryCount());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getDeliveyCountByStatusTodayTest() {
        List<Delivery> deliveries = new ArrayList<>();

        deliveries.add(new Delivery());
        deliveries.add(new Delivery());
        deliveries.add(new Delivery());

        when(reactiveDeliveryRepository.getDeliveryCountByDateDeliveredToBranch(DeliveryStatus.IN_BRANCH.toString(), deliveryService.getStartOfToday(), deliveryService.getStartOfTomarrow())).thenReturn(Flux.fromIterable(deliveries));

        StepVerifier.create(deliveryService.getDeliveyCountByStatusToday(DeliveryStatus.IN_BRANCH.toString())).consumeNextWith(deliveryCount -> {
            assertThat(deliveryCount).isNotNull();
            assertThat(deliveryCount.getDeliveryCount()).isEqualTo(3);
        }).verifyComplete();
    }

    @Test
    public void getCourierDeliveriesTodayTest() {
        Principal principal = () -> "courierid";

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");


        when(reactiveDeliveryRepository.getDeliveryCountByDateDeliveredToBranchAndCourierId(DeliveryStatus.IN_BRANCH.toString(), principal.getName(), deliveryService.getStartOfToday(), deliveryService.getStartOfTomarrow())).thenReturn(Flux.just(delivery));

        StepVerifier.create(deliveryService.getCourierDeliveriesToday("courierid", "IN_BRANCH")).consumeNextWith(delivery1 -> {
            assertThat(delivery1).isNotNull();
        }).verifyComplete();
    }

    @Test
    public void getCourierMyDeliveriesTodayTest() {
        Principal principal = () -> "courierid";

        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");

        when(reactiveDeliveryRepository.getDeliveryCountByDateCourierRecievedAndCourierId(DeliveryStatus.IN_BRANCH.toString(), principal.getName(), deliveryService.getStartOfToday(), deliveryService.getStartOfTomarrow())).thenReturn(Flux.just(delivery));

        StepVerifier.create(deliveryService.getCourierMyDeliveriesToday(principal)).consumeNextWith(delivery1 -> {
            assertThat(delivery1).isNotNull();
        }).verifyComplete();

    }

    @Test
    public void checkInDeliveryBranchTest() {
        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setActions(new Actions());

        when(reactiveDeliveryRepository.findById(anyString())).thenReturn(Mono.just(delivery));
        when(reactiveDeliveryRepository.save(any(Delivery.class))).thenReturn(Mono.just(delivery));

        StepVerifier.create(deliveryService.checkInDeliveryBranch(delivery.getId())).consumeNextWith(deliveryCheckIn ->
        {
            assertThat(deliveryCheckIn).isNotNull();
            assertThat(deliveryCheckIn.getId()).isEqualTo(delivery.getId());
            assertThat(deliveryCheckIn.isSuccess()).isEqualTo(true);
        }).verifyComplete();
    }

    @Test
    public void checkInDeliveryWarehouseTest() {
        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setActions(new Actions());

        when(reactiveDeliveryRepository.findById(anyString())).thenReturn(Mono.just(delivery));
        when(reactiveDeliveryRepository.save(any(Delivery.class))).thenReturn(Mono.just(delivery));

        StepVerifier.create(deliveryService.checkInDeliveryWarehouse(delivery.getId())).consumeNextWith(deliveryCheckIn ->
        {
            assertThat(deliveryCheckIn).isNotNull();
            assertThat(deliveryCheckIn.getId()).isEqualTo(delivery.getId());
            assertThat(deliveryCheckIn.isSuccess()).isEqualTo(true);
        }).verifyComplete();
    }

    @Test
    public void checkOutDeliveryTest() {
        Delivery delivery = new Delivery();
        delivery.setId("mytestdeliveryid");
        delivery.setActions(new Actions());

        when(reactiveDeliveryRepository.findById(anyString())).thenReturn(Mono.just(delivery));
        when(reactiveDeliveryRepository.save(any(Delivery.class))).thenReturn(Mono.just(delivery));

        StepVerifier.create(deliveryService.checkOutDelivery(delivery.getId())).consumeNextWith(deliveryCheckOut ->
        {
            assertThat(deliveryCheckOut).isNotNull();
            assertThat(deliveryCheckOut.getId()).isEqualTo(delivery.getId());
            assertThat(deliveryCheckOut.isSuccess()).isEqualTo(true);
        }).verifyComplete();
    }
}
