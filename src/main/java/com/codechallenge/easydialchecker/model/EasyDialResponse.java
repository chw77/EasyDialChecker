package com.codechallenge.easydialchecker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EasyDialResponse {

    @JsonProperty
    private String status;

    public EasyDialResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
