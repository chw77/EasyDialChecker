package com.codechallenge.easydialchecker.repository;

import com.codechallenge.easydialchecker.exception.PersistenceException;
import com.codechallenge.easydialchecker.model.EasyDialText;

import java.util.List;

public interface BaseRepository {
    /**
     * Reads data from the persistence layer to retrieve a list of strings from the data store.
     * If an error occurs during the retrieval process, a {@code PersistenceException} is thrown.
     *
     * @return A list of strings representing the retrieved data.
     * @throws PersistenceException If an error occurs while accessing the data store.
     */
    List<String> read() throws PersistenceException;

    /**
     * Saves the provided {@code EasyDialText} to the persistence layer.
     *
     * @param easyDialText The {@code EasyDialText} object to be saved.
     * @throws PersistenceException If an error occurs during the save process.
     */
    void save(EasyDialText easyDialText) throws PersistenceException;
}
