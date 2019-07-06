package com.example.hermes_intern.service;

import com.example.hermes_intern.controller.ResourceREST;
import com.example.hermes_intern.domain.Actions;
import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.domain.DeliveryStatus;
import com.example.hermes_intern.model.*;
import com.example.hermes_intern.repository.ReactiveDeliveryRepository;
import com.example.hermes_intern.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

@Service
public class DeliveryService {

    private final ReactiveDeliveryRepository deliveries;
    Date today = Calendar.getInstance().getTime();


    @Autowired
    public DeliveryService(ReactiveDeliveryRepository deliveries) {
        this.deliveries = deliveries;
    }

    @Autowired
    private JWTUtil jwtUtil;


    @View
    public Flux<Delivery> getAll() {
        return this.deliveries.findAll();

    }

    public Flux<Delivery> getCourierMyDeliveries(Principal principal) {
        return this.deliveries.findByCourierid(principal.getName());
    }

    public Flux<Delivery> getByStatusOnCourier() {
        String status = "ON_COURIER";
        return this.deliveries.findDeliveriesByStatus(status);
    }

    public Flux<Delivery> getByStatusInBranch() {
        String status = "IN_BRANCH";
        return this.deliveries.findDeliveriesByStatus(status);
    }

    public Flux<Delivery> getByStatusOnWayWarehouse() {
        String status = "ON_WAY_WAREHOUSE";
        return this.deliveries.findDeliveriesByStatus(status);
    }

    public Flux<Delivery> getByStatusInWarehouse() {
        String status = "IN_WAREHOUSE";
        return this.deliveries.findDeliveriesByStatus(status);
    }


    public Mono<Delivery> create(@RequestBody Delivery delivery) {
        Date defaultDate = null;

        delivery.setStatus(String.valueOf(DeliveryStatus.ON_COURIER));

        delivery.setId(UUID.randomUUID().toString());
        delivery.getCustomer().setId(UUID.randomUUID().toString());
        delivery.getActions().setDateCourierRecieved(new Date());
        delivery.getActions().setDateDeliveredToBranch(defaultDate);
        delivery.getActions().setDateDeliveredToWarehouse(defaultDate);
        delivery.getActions().setDateLeftBranch(defaultDate);
        return this.deliveries.save(delivery);
    }

    public Mono<DeliveryLocation> getLocation(@PathVariable("id") String id) {
        return this.deliveries.findById(id).map(response -> {
            DeliveryLocation deliveryLocation = new DeliveryLocation();
            deliveryLocation.setId(id);
            deliveryLocation.setLocation(response.getStatus());
            return deliveryLocation;
        });
    }

    public Mono<ResponseEntity<?>> getLocationbyCustomer(@PathVariable("id") String id, Principal principal) {
        return this.deliveries.findById(id).map(response -> {
            if (principal.getName().equals(response.getCustomer().getId())){
                return ResponseEntity.ok().body(response.getStatus());
            }else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public Mono<Actions> getActions(@PathVariable("id") String id) {
        return this.deliveries.findById(id).map(response -> {
            return response.getActions();
        });
    }

    public Mono<ResponseEntity<?>> getActionsbyCustomer(@PathVariable("id") String id, Principal principal) {
        return this.deliveries.findById(id).map(response -> {
            if (principal.getName().equals(response.getCustomer().getId())){
                return ResponseEntity.ok().body(response.getActions());
            }else
               return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public Mono<DeliveryCount> getDeliveryCount(@RequestParam String status) {
        return this.deliveries.countByStatus(status).map(aLong -> {
            DeliveryCount deliveryCount = new DeliveryCount();
            deliveryCount.setDeliveryCount(aLong);
            deliveryCount.setStatus(status);
            return deliveryCount;
        });
    }

    public Long getStartOfToday() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public Long getStartOfTomarrow() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTimeInMillis();
    }

    public Mono<DeliveryCount> getDeliveyCountByStatusToday(@RequestParam String status) {

        Flux<Delivery> deliveries = null;

        if (status.equals(DeliveryStatus.ON_COURIER.toString())) {

            deliveries = this.deliveries.getDeliveryCountByDateCourierRecieved(status, getStartOfToday(), getStartOfTomarrow());
        } else if (status.equals(DeliveryStatus.IN_BRANCH.toString())) {

            deliveries = this.deliveries.getDeliveryCountByDateDeliveredToBranch(status, getStartOfToday(), getStartOfTomarrow());
        } else if (status.equals(DeliveryStatus.ON_WAY_WAREHOUSE.toString())) {

            deliveries = this.deliveries.getDeliveryCountByDateLeftBranch(status, getStartOfToday(), getStartOfTomarrow());
        } else if (status.equals(DeliveryStatus.IN_WAREHOUSE.toString())) {

            deliveries = this.deliveries.getDeliveryCountByDateDeliveredToWarehouse(status, getStartOfToday(), getStartOfTomarrow());
        }


        return deliveries.count().map(aLong -> {
            DeliveryCount deliveryCount = new DeliveryCount();
            deliveryCount.setDeliveryCount(aLong);
            deliveryCount.setStatus(status);
            return deliveryCount;
        });
    }

    public Flux<Delivery> getCourierDeliveriesToday(@RequestParam String courierid, @RequestParam String status) {
        Flux<Delivery> deliveries = null;
        if (status.equals(DeliveryStatus.ON_COURIER.toString())) {
            deliveries = this.deliveries.getDeliveryCountByDateCourierRecievedAndCourierId(status, courierid, getStartOfToday(), getStartOfTomarrow());
        } else if (status.equals(DeliveryStatus.IN_BRANCH.toString())) {

            deliveries = this.deliveries.getDeliveryCountByDateDeliveredToBranchAndCourierId(status, courierid, getStartOfToday(), getStartOfTomarrow());
        }
        return deliveries;
    }

    public Flux<Delivery> getCourierMyDeliveriesToday(Principal principal) {
            return this.deliveries.getDeliveryCountByDateCourierRecievedAndCourierId("IN_BRANCH", principal.getName(), getStartOfToday(), getStartOfTomarrow());
    }


    public Mono<DeliveryCheckIn> checkInDeliveryBranch(@RequestParam(value = "id") String id) {

        return this.deliveries.findById(id).flatMap(response -> {
            Delivery delivery = new Delivery();

            delivery.setId(response.getId());
            delivery.setWarehouse(response.getWarehouse());
            delivery.setCustomer(response.getCustomer());
            delivery.setCourierid(response.getCourierid());
            delivery.setBranch(response.getBranch());
            delivery.setActions(response.getActions());

            delivery.setStatus(String.valueOf(DeliveryStatus.IN_BRANCH));
            delivery.getActions().setDateDeliveredToBranch(today);

            DeliveryCheckIn deliveryCheckIn = new DeliveryCheckIn();

            return this.deliveries.save(delivery).map(delivery1 -> {
                deliveryCheckIn.setId(delivery.getId());
                deliveryCheckIn.setSuccess(true);
                return deliveryCheckIn;
            });
        });
    }

    public Mono<DeliveryCheckIn> checkInDeliveryWarehouse(@RequestParam(value = "id") String id) {

        return this.deliveries.findById(id).flatMap(response -> {
            Delivery delivery = new Delivery();

            delivery.setId(response.getId());
            delivery.setWarehouse(response.getWarehouse());
            delivery.setCustomer(response.getCustomer());
            delivery.setCourierid(response.getCourierid());
            delivery.setBranch(response.getBranch());
            delivery.setActions(response.getActions());


            delivery.setStatus(String.valueOf(DeliveryStatus.IN_WAREHOUSE));
            delivery.getActions().setDateDeliveredToWarehouse(today);

            DeliveryCheckIn deliveryCheckIn = new DeliveryCheckIn();

            return this.deliveries.save(delivery).map(delivery1 -> {
                deliveryCheckIn.setId(delivery.getId());
                deliveryCheckIn.setSuccess(true);
                return deliveryCheckIn;
            });
        });
    }

    public Mono<DeliveryCheckOut> checkOutDelivery(@RequestParam(value = "id") String id) {

        return this.deliveries.findById(id).flatMap(response -> {
            Delivery delivery = new Delivery();

            delivery.setId(response.getId());
            delivery.setWarehouse(response.getWarehouse());
            delivery.setCustomer(response.getCustomer());
            delivery.setCourierid(response.getCourierid());
            delivery.setBranch(response.getBranch());
            delivery.setActions(response.getActions());


            delivery.setStatus(String.valueOf(DeliveryStatus.ON_WAY_WAREHOUSE));
            delivery.getActions().setDateLeftBranch(today);

            DeliveryCheckOut deliveryCheckOut = new DeliveryCheckOut();

            return this.deliveries.save(delivery).map(delivery1 -> {
                deliveryCheckOut.setId(delivery.getId());
                deliveryCheckOut.setSuccess(true);
                return deliveryCheckOut;
            });
        });
    }
}
