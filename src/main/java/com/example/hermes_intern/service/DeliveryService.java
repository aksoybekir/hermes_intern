package com.example.hermes_intern.service;

import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.domain.DeliveryStatus;
import com.example.hermes_intern.model.DeliveryActions;
import com.example.hermes_intern.model.DeliveryLocation;
import com.example.hermes_intern.model.DeliveryCount;
import com.example.hermes_intern.repository.ReactiveDeliveryRepository;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Flux<Delivery> getByStatus(String status) {
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

    public Mono<DeliveryActions> getActions(@PathVariable("id") String id) {
        return this.deliveries.findById(id).map(response -> {
            DeliveryActions deliveryActions = new DeliveryActions();
            deliveryActions.setId(id);
            deliveryActions.setActions(response.getActions());
            return deliveryActions;
        });
    }

    public Mono<DeliveryCount> getDeliveryCount(@RequestParam String status) {
        return this.deliveries.countByStatus(status).map(aLong -> {
            DeliveryCount deliveryCount = new DeliveryCount();
            deliveryCount.setDeliveryCount(aLong);
            deliveryCount.setStatus(status);
            return deliveryCount;
        });
    }

    public Flux<Delivery> getCourierDeliveries(@PathVariable("courierid") String courierid) {
        return this.deliveries.findByCourierid(courierid);
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
}
