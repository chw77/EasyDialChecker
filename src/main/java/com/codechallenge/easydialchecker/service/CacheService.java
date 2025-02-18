package com.codechallenge.easydialchecker.service;

import com.codechallenge.easydialchecker.model.EasyDialText;

public interface CacheService {
    /**
     * Retrieves records from the cache based on the provided phone number.
     * This method checks the cache for a pre-existing entry
     * corresponding to the sanitized phone number text.
     *
     * @param text The sanitized phone number text to search for in the cache.
     * @return {@code EasyDialText} object from the cache, or {@code null}
     * if no matching entry is found.
     */
    EasyDialText readFromCache(String text);

    /**
     * Stores the {@code EasyDialText} object in the cache for future use,
     * allowing quicker retrieval of previously checked easy dial numbers.
     *
     * @param easyDialText The {@code EasyDialText} object to be added to the cache.
     */
    void addToCache(EasyDialText easyDialText);

    /**
     * Preloads the cache with initial data to ensure quick access.
     */
    void preloadCache();
}
