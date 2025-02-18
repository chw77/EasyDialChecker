package com.codechallenge.easydialchecker.service;

import com.codechallenge.easydialchecker.model.EasyDialRequest;

public interface ValidationService {
    /**
     * Validates the given EasyDialRequest to ensure if the request contains valid input data before processing.
     * The validation criteria includes checking for null values, ensuring the input format is correct.
     *
     * @param easyDialRequest the request object containing the input text to be validated
     * @return {@code true} if the request is valid, otherwise {@code false}
     */
    boolean isValidRequest(EasyDialRequest easyDialRequest);
}
