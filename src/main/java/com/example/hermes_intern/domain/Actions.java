package com.example.hermes_intern.domain;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.Date;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Actions {

    @Field("datecourierrecieved")
    private Date dateCourierRecieved;

    @Field("datedeliveredtobranch")
    private Date dateDeliveredToBranch;

    @Field("dateleftbranch")
    private Date dateLeftBranch;

    @Field("datedeliveredtowarehouse")
    private Date dateDeliveredToWareHouse;

}
