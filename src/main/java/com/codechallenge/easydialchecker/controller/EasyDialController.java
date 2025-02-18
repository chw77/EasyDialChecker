package com.codechallenge.easydialchecker.controller;

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
    private static final String EASYDIAL_PATH = "easydial";

    private final CacheService cacheService;
    private final EasyDialService easyDialService;
    private final ValidationService validationService;

    public EasyDialController(CacheService cacheService, EasyDialService easyDialService, ValidationService validationService) {
        this.cacheService = cacheService;
        this.easyDialService = easyDialService;
        this.validationService = validationService;
    }

    @RequestMapping(method = RequestMethod.POST, path = EASYDIAL_PATH, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
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
                LOGGER.info("Unable to read input text from cache.");
                //Execute easy dial check logic.
                easyDialText = easyDialService.checkEasyToDial(numberText);
                //Add input text to cache
                cacheService.addToCache(easyDialText);
                //persist input text and record.
                easyDialService.saveEasyDialText(easyDialText);
            }
            easyDialResponse = generateResponse(HttpStatus.OK.value(), easyDialText);
            LOGGER.info("Easy dial check tasks are completed.");
        } else {
            easyDialResponse = generateResponse(HttpStatus.BAD_REQUEST.value(), null);
            LOGGER.warn("Bad Request - Request validation failure.");
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