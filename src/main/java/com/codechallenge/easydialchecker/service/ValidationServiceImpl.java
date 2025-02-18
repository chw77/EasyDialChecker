package com.codechallenge.easydialchecker.service;

import com.codechallenge.easydialchecker.model.EasyDialRequest;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationServiceImpl.class);

    @Value(value = "${validationRegex}")
    private String validationRegex;

    public boolean isValidRequest(EasyDialRequest easyDialRequest) {
        boolean isValid = false;
        try {
            if (null != easyDialRequest) {
                String inputText = easyDialRequest.getInputText();
                //validate input phone number text.
                if (StringUtils.isNotBlank(inputText)) {
                    boolean isValidInput = validateInput(inputText);
                    if (isValidInput) {
                        isValid = true;
                    }
                }
            }
        } catch (Exception exception) {
            LOGGER.error("Easydial input request validation failure. Message " + exception.getMessage());
        }
        return isValid;
    }

    private boolean validateInput(String input) {
        String regex = validationRegex;
        Pattern pattern = Pattern.compile(regex);
        boolean isMatch = pattern.matcher(input).find();

        return isMatch;
    }
}
