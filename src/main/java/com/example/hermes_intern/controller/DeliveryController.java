package com.example.hermes_intern.controller;

import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.model.*;
import com.example.hermes_intern.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(method = RequestMethod.GET, params = {"status"})
    public Flux<Delivery> getByStatus(@RequestParam String status) {
        return this.deliveryService.getByStatus(status);
    }

    @PostMapping("")
    public Mono<Delivery> create(@RequestBody Delivery delivery) {
        return this.deliveryService.create(delivery);
    }


    @GetMapping("/count")
    public Mono<DeliveryCount> getDeliveryCount(@RequestParam String status) {
        return deliveryService.getDeliveryCount(status);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/count/today", params = {"status"})
    public Mono<DeliveryCount> getDeliveyCountByStatusToday(@RequestParam String status) {
        return this.deliveryService.getDeliveyCountByStatusToday(status);
    }


    //Müşteriler iade id bilgisi gönderip, iadenin son durumunu görüntülerler.
    @GetMapping("/location/{id}")
    public Mono<DeliveryLocation> getLocation(@PathVariable("id") String id) {
        Mono<DeliveryLocation> deliveryLocationMono = this.deliveryService.getLocation(id);
        return deliveryLocationMono;
        //return this.deliveryService.getLocation(id);
    }

    //Müşteriler iade id bilgisi gönderip, iadenin son durumunu görüntülerler.
    @GetMapping("/actions/{id}")
    public Mono<DeliveryActions> getActions(@PathVariable("id") String id) {
        return this.deliveryService.getActions(id);
    }

    @RequestMapping(method = RequestMethod.GET, params = {"courierid"})
    public Flux<Delivery> getCourierDeliveries(@RequestParam String courierid) {
        return this.deliveryService.getCourierDeliveries(courierid);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/today", params = {"courierid", "status"})
    public Flux<Delivery> getCourierDeliveriesToday(@RequestParam String courierid, @RequestParam String status) {
        return this.deliveryService.getCourierDeliveriesToday(courierid, status);
    }
   
      
    @RequestMapping(method = RequestMethod.GET, value = "/checkin", params = {"id","owner"})
    public Mono<DeliveryCheckIn> checkInDelivery(@RequestParam String id, @RequestParam String owner) {
        return this.deliveryService.checkInDelivery(id,owner);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/checkout", params = {"id"})
    public Mono<DeliveryCheckOut> checkInDelivery(@RequestParam String id) {
        return this.deliveryService.checkOutDelivery(id);
    }

}

