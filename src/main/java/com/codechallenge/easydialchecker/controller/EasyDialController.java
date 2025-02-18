package com.codechallenge.easydialchecker.controller;

import com.codechallenge.easydialchecker.exception.PersistenceException;
import com.codechallenge.easydialchecker.model.EasyDialRequest;
import com.codechallenge.easydialchecker.model.EasyDialResponse;
import com.codechallenge.easydialchecker.model.EasyDialText;
import com.codechallenge.easydialchecker.service.CacheService;
import com.codechallenge.easydialchecker.service.EasyDialService;
import com.codechallenge.easydialchecker.service.ValidationService;
import com.codechallenge.easydialchecker.utils.CleanupUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class EasyDialController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EasyDialController.class);

    private static final String APPLICATION_JSON = "application/json";
    private static final String EASY_DIAL_PATH = "easydial";

    private static final String BAD_REQUEST_MESSAGE = "Bad Request - Request validation failure.";
    private static final String CACHE_ENTRY_NOT_FOUND = "Unable to read input text from cache.";
    private static final String EASY_DIAL_CHECK_MESSAGE = "Easy dial check tasks are completed.";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error - Unable to process the request.";

    private final CacheService cacheService;
    private final EasyDialService easyDialService;
    private final ValidationService validationService;

    public EasyDialController(CacheService cacheService, EasyDialService easyDialService, ValidationService validationService) {
        this.cacheService = cacheService;
        this.easyDialService = easyDialService;
        this.validationService = validationService;
    }

    @RequestMapping(method = RequestMethod.POST, path = EASY_DIAL_PATH, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<EasyDialResponse> checkEasyDial(@RequestBody EasyDialRequest easyDialRequest) {
        //Validate the incoming request
        boolean isValidRequest = validationService.isValidRequest(easyDialRequest);
        EasyDialText easyDialText;
        ResponseEntity<EasyDialResponse> easyDialResponse;
        if (isValidRequest) {
            String text = easyDialRequest.getInputText();
            //Remove whitespaces (spaces, tabs, newlines) in the input text
            String numberText = CleanupUtils.removeWhitespace(text);
            //Check input text from cache.
            easyDialText = cacheService.readFromCache(numberText);

            if (null == easyDialText) {
                LOGGER.info(CACHE_ENTRY_NOT_FOUND);
                //Execute easy dial check logic.
                easyDialText = easyDialService.checkEasyToDial(numberText);
                //Add input text to cache
                cacheService.addToCache(easyDialText);
                //persist input text and record.
                try {
                    easyDialService.saveEasyDialText(easyDialText);
                } catch (PersistenceException exception) {
                    // Internal server error occurred.
                    easyDialResponse = generateResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
                    LOGGER.error(INTERNAL_SERVER_ERROR_MESSAGE);

                    return easyDialResponse;
                }
            }
            // Successful request check.
            easyDialResponse = generateResponse(HttpStatus.OK.value(), easyDialText);
            LOGGER.info(EASY_DIAL_CHECK_MESSAGE);
        } else {
            // Bad input request.
            easyDialResponse = generateResponse(HttpStatus.BAD_REQUEST.value(), null);
            LOGGER.warn(BAD_REQUEST_MESSAGE);
        }
        return easyDialResponse;
    }

    private static ResponseEntity<EasyDialResponse> generateResponse(int statusCode, EasyDialText easyDialText) {
        EasyDialResponse easyDialResponse = new EasyDialResponse();
        if (null != easyDialText) {
            easyDialResponse.setStatus(String.valueOf(easyDialText.isEasyToDial()));
        }
        ResponseEntity<EasyDialResponse> responseEntity = ResponseEntity
                .status(statusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(easyDialResponse);
        return responseEntity;
    }
}