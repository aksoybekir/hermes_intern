package com.example.hermes_intern.controller;

import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.model.*;
import com.example.hermes_intern.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Flux<Delivery> getAll() {
        return this.deliveryService.getAll();
    }


    @GetMapping("/oncourier")
    @PreAuthorize("hasRole('BRANCH_WORKER') or hasRole('COURIER')")
    public Flux<Delivery> getByStatusOnCourier() {
        return this.deliveryService.getByStatusOnCourier();
    }


    @GetMapping("/inbranch")
    @PreAuthorize("hasRole('BRANCH_WORKER')")
    public Flux<Delivery> getByStatusInBranch() {
        return this.deliveryService.getByStatusInBranch();
    }

    @GetMapping("/onwaywarehouse")
    @PreAuthorize("hasRole('BRANCH_WORKER') or hasRole('WAREHOUSE_WORKER')")
    public Flux<Delivery> getByStatusOnWayWarehouse() {
        return this.deliveryService.getByStatusOnWayWarehouse();
    }

    @GetMapping("/inwarehouse")
    @PreAuthorize("hasRole('WAREHOUSE_WORKER')")
    public Flux<Delivery> getByStatusInWarehouse() {
        return this.deliveryService.getByStatusInWarehouse();
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Delivery> create(@RequestBody Delivery delivery) {
        return this.deliveryService.create(delivery);
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<DeliveryCount> getDeliveryCount(@RequestParam String status) {
        return deliveryService.getDeliveryCount(status);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/count/today", params = {"status"})
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<DeliveryCount> getDeliveyCountByStatusToday(@RequestParam String status) {
        return this.deliveryService.getDeliveyCountByStatusToday(status);
    }


    @GetMapping("/location/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Mono<DeliveryLocation> getLocation(@PathVariable("id") String id) {
        return this.deliveryService.getLocation(id);
    }

    @GetMapping("/actions/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Mono<DeliveryActions> getActions(@PathVariable("id") String id) {
        return this.deliveryService.getActions(id);
    }

    @RequestMapping(method = RequestMethod.GET, params = {"courierid"})
    @PreAuthorize("hasRole('COURIER')")
    public Flux<Delivery> getCourierDeliveries(@RequestParam String courierid) {
        return this.deliveryService.getCourierDeliveries(courierid);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/today", params = {"courierid", "status"})
    @PreAuthorize("hasRole('COURIER')")
    public Flux<Delivery> getCourierDeliveriesToday(@RequestParam String courierid, @RequestParam String status) {
        return this.deliveryService.getCourierDeliveriesToday(courierid, status);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/checkin/branch", params = {"id"})
    @PreAuthorize("hasRole('BRANCH_WORKER')")
    public Mono<DeliveryCheckIn> checkInDeliveryBranch(@RequestParam String id) {
        return this.deliveryService.checkInDeliveryBranch(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/checkin/warehouse", params = {"id"})
    @PreAuthorize("hasRole('WAREHOUSE_WORKER')")
    public Mono<DeliveryCheckIn> checkInDeliveryWarehouse(@RequestParam String id) {
        return this.deliveryService.checkInDeliveryWarehouse(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/checkout", params = {"id"})
    @PreAuthorize("hasRole('BRANCH_WORKER')")
    public Mono<DeliveryCheckOut> checkOutDelivery(@RequestParam String id) {
        return this.deliveryService.checkOutDelivery(id);
    }

}

