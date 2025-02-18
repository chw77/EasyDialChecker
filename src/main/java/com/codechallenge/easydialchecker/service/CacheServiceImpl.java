package com.codechallenge.easydialchecker.service;

import com.codechallenge.easydialchecker.exception.PersistenceException;
import com.codechallenge.easydialchecker.model.EasyDialText;
import com.codechallenge.easydialchecker.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CacheServiceImpl implements CacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheServiceImpl.class);
    private static final String COMMA_DELIMITER = ",";

    private static HashMap<String, Boolean> EASYDIAL_CACHE;

    final BaseRepository baseRepository;

    public CacheServiceImpl(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
        if (EASYDIAL_CACHE == null) {
            EASYDIAL_CACHE = new HashMap<>();
        }
    }

    @Override
    public EasyDialText readFromCache(String text) {
        EasyDialText easyDialText = null;
        if (EASYDIAL_CACHE.containsKey(text)) {
            easyDialText = new EasyDialText();
            easyDialText.setEasyToDial(EASYDIAL_CACHE.get(text));
            easyDialText.setValue(text);
        }
        return easyDialText;
    }

    @Override
    public void addToCache(EasyDialText easyDialText) {
        EASYDIAL_CACHE.put(easyDialText.getValue(), easyDialText.isEasyToDial());
    }

    @Override
    public void preloadCache() {
        List<String> records = null;
        try {
            records = baseRepository.read();
        } catch (PersistenceException e) {
            // Unable to preload data from the persistence file.
            // The new file will be generated, if the persistence file isn't available.
            LOGGER.warn("Persistence layer error: Unable to preload data.");
        }

        if (null != records) {
            for (String record : records) {
                LOGGER.debug("Cache preload record: " + record);
                String[] entry = record.split(COMMA_DELIMITER);
                if (entry.length == 2) {
                    boolean value = Boolean.parseBoolean(entry[1]);
                    EASYDIAL_CACHE.put(entry[0], value);
                } else {
                    // Expect data in TEXT,STATUS format.
                    LOGGER.warn("Persistence data error: Found tampered data.");
                }
            }
        }

    }
}
