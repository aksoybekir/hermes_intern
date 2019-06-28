package com.example.hermes_intern.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractReactiveCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;
import org.springframework.data.couchbase.repository.support.IndexManager;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableReactiveCouchbaseRepositories
class ApplicationConfig extends AbstractReactiveCouchbaseConfiguration {

    @Value("${storage.host}")
    private String host;

    @Value("${storage.bucket}")
    private String bucket;

    @Value("${storage.username}")
    private String username;

    @Value("${storage.password}")
    private String password;

    @Override
    protected List<String> getBootstrapHosts() {
        return Collections.singletonList(host);
    }

    @Override
    protected String getBucketName() {
        return bucket;
    }

    @Override
    protected String getBucketPassword() {
        return password;
    }

    @Override
    protected String getUsername() {
        return username;
    }

    @Override
    public IndexManager indexManager() {
        return new IndexManager(true, true, false);    //auto indexing for views only
    }
}
