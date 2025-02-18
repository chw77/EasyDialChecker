package com.codechallenge.easydialchecker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EasyDialRequest {
    @JsonProperty
    private String inputText;

    public EasyDialRequest() {
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getInputText() {
        return inputText;
    }
}
