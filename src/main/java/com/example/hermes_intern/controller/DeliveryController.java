package com.example.hermes_intern.controller;

import com.example.hermes_intern.domain.Delivery;
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

    @GetMapping("")
    public Flux<Delivery> getAll(@RequestParam(value = "status", required = false) String status) {
        return this.deliveryService.getAll(status);
    }

    @PostMapping("")
    public Mono<Delivery> create(@RequestBody Delivery delivery) {
        return this.deliveryService.create(delivery);
    }


}
