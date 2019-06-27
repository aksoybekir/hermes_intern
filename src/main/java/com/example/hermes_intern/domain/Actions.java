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

    @Field("dateCourierRecieved")
    private Date dateCourierRecieved;

    @Field("dateDeliveredToBranch")
    private Date dateDeliveredToBranch;

    @Field("dateLeftBranch")
    private Date dateLeftBranch;

    @Field("dateDeliveredToWarehouse")
    private Date dateDeliveredToWarehouse;

    public void setDateCourierRecieved(Date dateCourierRecieved) {
        this.dateCourierRecieved = dateCourierRecieved;
    }

    public void setDateDeliveredToBranch(Date dateDeliveredToBranch) {
        this.dateDeliveredToBranch = dateDeliveredToBranch;
    }

    public void setDateLeftBranch(Date dateLeftBranch) {
        this.dateLeftBranch = dateLeftBranch;
    }

    public void setDateDeliveredToWarehouse(Date dateDeliveredToWarehouse) {
        this.dateDeliveredToWarehouse = dateDeliveredToWarehouse;
    }
}
