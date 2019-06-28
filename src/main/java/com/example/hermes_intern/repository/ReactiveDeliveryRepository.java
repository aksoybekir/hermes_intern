package com.example.hermes_intern.repository;

import com.example.hermes_intern.domain.Delivery;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "delivery", viewName = "all")
public interface ReactiveDeliveryRepository extends ReactiveCouchbaseSortingRepository<Delivery, String> {

    Flux<Delivery> findByStatus(String status);

}