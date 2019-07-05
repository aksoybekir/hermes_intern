package com.example.hermes_intern.repository;

import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.model.DeliveryCount;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "delivery", viewName = "all")
public interface ReactiveDeliveryRepository extends ReactiveCouchbaseSortingRepository<Delivery, String> {

    Mono<Long> countByStatus(String status);

    Mono<Delivery> findById(String id);

    Flux<Delivery> findByIdAndCourierid(String id, String courierid);

    Flux<Delivery> findByCourierid(String courierid);

    Flux<Delivery> findDeliveriesByStatus(String status);

    @Query("select customer ,courierid ,branch ,warehouse ,status ,actions, META().id AS _ID, META().cas AS _CAS from `HermesDemo` data where status=$1 and actions.dateCourierRecieved BETWEEN $2 AND $3")
    Flux<Delivery> getDeliveryCountByDateCourierRecieved(String status, long today, long tomarrow);

    @Query("select customer ,courierid ,branch ,warehouse ,status ,actions, META().id AS _ID, META().cas AS _CAS from `HermesDemo` data where status=$1 and actions.dateDeliveredToBranch BETWEEN $2 AND $3")
    Flux<Delivery> getDeliveryCountByDateDeliveredToBranch(String status, long today, long tomarrow);

    @Query("select customer ,courierid ,branch ,warehouse ,status ,actions, META().id AS _ID, META().cas AS _CAS from `HermesDemo` data where status=$1 and actions.dateLeftBranch BETWEEN $2 AND $3")
    Flux<Delivery> getDeliveryCountByDateLeftBranch(String status, long today, long tomarrow);

    @Query("select customer ,courierid ,branch ,warehouse ,status ,actions, META().id AS _ID, META().cas AS _CAS from `HermesDemo` data where status=$1 and actions.dateDeliveredToWarehouse BETWEEN $2 AND $3")
    Flux<Delivery> getDeliveryCountByDateDeliveredToWarehouse(String status, long today, long tomarrow);


    @Query("select customer ,courierid ,branch ,warehouse ,status ,actions, META().id AS _ID, META().cas AS _CAS from `HermesDemo` data where status=$1 and courierid=$2 and actions.dateCourierRecieved BETWEEN $3 AND $4")
    Flux<Delivery> getDeliveryCountByDateCourierRecievedAndCourierId(String status, String courierid, long today, long tomarrow);

    @Query("select customer ,courierid ,branch ,warehouse ,status ,actions, META().id AS _ID, META().cas AS _CAS from `HermesDemo` data where status=$1 and courierid=$2 and actions.dateDeliveredToBranch BETWEEN $3 AND $4")
    Flux<Delivery> getDeliveryCountByDateDeliveredToBranchAndCourierId(String status, String courierid, long today, long tomarrow);

}