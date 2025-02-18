package com.codechallenge.easydialchecker;

import com.codechallenge.easydialchecker.service.CacheService;
import com.codechallenge.easydialchecker.service.CacheServiceImpl;
import com.codechallenge.easydialchecker.service.EasyDialService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EasyDialCheckerApplication {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(EasyDialCheckerApplication.class, args);
        // Create Keypad Adjacent Map
        EasyDialService easyDialService = applicationContext.getBean(EasyDialService.class);
        easyDialService.buildAdjacentKeyMap();

        // Preload cache with persistence records during the application startup.
        CacheService cacheService = applicationContext.getBean(CacheServiceImpl.class);
        cacheService.preloadCache();
    }

}
