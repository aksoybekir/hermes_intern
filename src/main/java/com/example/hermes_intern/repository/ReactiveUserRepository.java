package com.example.hermes_intern.repository;

import com.example.hermes_intern.security.model.User;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "user", viewName = "all")
public interface ReactiveUserRepository extends ReactiveCouchbaseSortingRepository<User, String> {
    Mono<User> findByUsername(String username);
}
