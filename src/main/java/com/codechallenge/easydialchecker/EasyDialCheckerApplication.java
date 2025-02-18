package com.codechallenge.easydialchecker;

import com.codechallenge.easydialchecker.service.CacheService;
import com.codechallenge.easydialchecker.service.CacheServiceImpl;
import com.codechallenge.easydialchecker.service.EasyDialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EasyDialCheckerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyDialCheckerApplication.class);
    private static final String STARTUP_ERROR = "Unable to start the EasyDial Checker Application";

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(EasyDialCheckerApplication.class, args);

        try {
            // Generate Keypad adjacent map.
            EasyDialService easyDialService = applicationContext.getBean(EasyDialService.class);
            easyDialService.buildAdjacentKeyMap();

            // Preload phone number cache with persistence records.
            CacheService cacheService = applicationContext.getBean(CacheServiceImpl.class);
            cacheService.preloadCache();

        } catch (Exception exception) {
            LOGGER.error(STARTUP_ERROR + " {}", exception.getMessage());
            throw new RuntimeException(STARTUP_ERROR, exception);
        }
    }
}
