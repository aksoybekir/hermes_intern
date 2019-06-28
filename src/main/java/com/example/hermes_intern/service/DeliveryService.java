package com.example.hermes_intern.service;

import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.domain.DeliveryStatus;
import com.example.hermes_intern.repository.ReactiveDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class DeliveryService {

    private final ReactiveDeliveryRepository deliveries;

    @Autowired
    public DeliveryService(ReactiveDeliveryRepository deliveries) {
        this.deliveries = deliveries;
    }

    @View
    public Flux<Delivery> getAll() {
        return this.deliveries.findAll();
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

    public Mono<Delivery> getLocation(@RequestParam(value = "id") String id) {
        return this.deliveries.findById(id);
    }


}
