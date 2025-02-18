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
    private static final String PRELOAD_ERROR = "Persistence layer error: Unable to preload data.";
    private static final String PERSISTENCE_ERROR = "Persistence data error: Found tampered data, expected format PHONE_NUMBER,STATUS.";

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
    public void preloadCache() throws PersistenceException {
        List<String> records = null;
        try {
            records = baseRepository.read();
        } catch (PersistenceException exception) {
            // Unable to preload data from the persistence file.
            // The new file will be generated, if the persistence file isn't available.
            LOGGER.error(PRELOAD_ERROR + " {}", exception.getMessage());
            throw new PersistenceException(PRELOAD_ERROR, exception);
        }

        processRecords(records);
    }

    private void processRecords(List<String> records) throws PersistenceException {
        if (null != records) {
            for (String record : records) {
                LOGGER.debug("Cache preload record: " + record);
                String[] entry = record.split(COMMA_DELIMITER);
                if (entry.length == 2) {
                    boolean value = Boolean.parseBoolean(entry[1]);
                    EASYDIAL_CACHE.put(entry[0], value);
                } else {
                    // Expect data in PHONE_NUMBER,STATUS format.
                    LOGGER.error(PERSISTENCE_ERROR);
                    throw new PersistenceException(PERSISTENCE_ERROR);
                }
            }
        }
    }
}
