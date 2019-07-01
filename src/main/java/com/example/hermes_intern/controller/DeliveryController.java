package com.example.hermes_intern.controller;

import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.model.DeliveryActions;
import com.example.hermes_intern.model.DeliveryLocation;
import com.example.hermes_intern.model.DeliveryCount;
import com.example.hermes_intern.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping(value = "/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("")
    public Flux<Delivery> getAll(@RequestParam(value = "status", required = false) String status) {
        if (status!=null)
        {
            return this.deliveryService.getByStatus(status);
        }
        else
        {
            return this.deliveryService.getAll();
        }
    }
    @PostMapping("")
    public Mono<Delivery> create(@RequestBody Delivery delivery) {
        return this.deliveryService.create(delivery);
    }


    @GetMapping("/count")
    public Mono<DeliveryCount> getDeliveryCount(@RequestParam(value = "status", required = false) String status) {
        return deliveryService.getDeliveryCount(status);
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
 }

