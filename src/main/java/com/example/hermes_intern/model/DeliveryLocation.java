package com.example.hermes_intern.model;


import com.couchbase.client.java.repository.annotation.Field;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class DeliveryLocation {

    @Field("id")
    private String id;

    @Field("Location")
    private String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}