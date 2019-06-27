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
public class Gonderi {

    @Id
    private String id;

    @Field("customer")
    private Customer customer;

    @Field("courierid")
    private String courierID;

    @Field("branch")
    private Branch branch;

    @Field("warehouse")
    private WareHouse wareHouse;

    @Field("status")
    private String status;  //enum olarak değiştirilecek

    @Field("actions")
    private Actions actions;


}
