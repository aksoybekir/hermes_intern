package com.example.hermes_intern.domain;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;


import org.springframework.data.annotation.Id;


@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Delivery {

    @Id
    private String id;

    @Field("customer")
    private Customer customer;

    @Field("courierid")
    private String courierid;

    @Field("branch")
    private Branch branch;

    @Field("warehouse")
    private WareHouse warehouse;

    @Field("status")
    private String status;  //enum olarak değiştirilecek

    @Field("actions")
    private Actions actions;

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Actions getActions() {
        return actions;
    }
}
