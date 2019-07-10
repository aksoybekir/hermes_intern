package com.example.hermes_intern.model;


import com.couchbase.client.java.repository.annotation.Field;
import com.example.hermes_intern.domain.Actions;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class DeliveryActions {

    @Field("id")
    private String id;

    @Field("Actions")
    private Actions actions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Actions getActions() {
        return actions;
    }

    public void setActions(Actions actions) {
        this.actions = actions;
    }
}