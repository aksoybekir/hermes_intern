package com.example.hermes_intern.domain;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Address {

    @Field("city")
    private String city;

    @Field("district")
    private String district;

    @Field("address")
    private String address;

}
