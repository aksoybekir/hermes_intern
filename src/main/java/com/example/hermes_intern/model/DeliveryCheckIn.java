package com.example.hermes_intern.model;

import com.couchbase.client.java.repository.annotation.Field;

public class DeliveryCheckIn {

    @Field("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Field("success")
    private boolean success;

}
