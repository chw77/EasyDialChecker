package com.codechallenge.easydialchecker.service;

import com.codechallenge.easydialchecker.exception.KeypadException;
import com.codechallenge.easydialchecker.exception.PersistenceException;
import com.codechallenge.easydialchecker.model.EasyDialText;

public interface EasyDialService {
    /**
     * Checks whether the given phone number is "easy to dial" based on
     * an easy dialing pattern of adjacency on a standard telephone keypad.
     *
     * @param phoneNumber The phone number to check, with all whitespace removed.
     * @return An {@code EasyDialText} object containing the result of the check,
     * including whether the number is easy to dial and whitespace removed phone number.
     */
    EasyDialText checkEasyToDial(String phoneNumber);

    /**
     * Persist the provided EasyDialText record to the data store
     * ensuring that  the processed phone number and its evaluation details
     * are stored for future reference or caching purposes.
     *
     * @param easyDialText The {@code EasyDialText} object containing the
     *                     phone number and its easy-to-dial evaluation (true/false).
     *
     * @throws PersistenceException If an error occurs while persisting the records.
     */
    void saveEasyDialText(EasyDialText easyDialText) throws PersistenceException;

    /**
     * Builds the adjacency map for the telephone keypad, excluding '*' and '#'.
     * This map helps validate easy-to-dial phone numbers based on adjacent keys.
     *
     * @throws KeypadException If an error occurs during the keypad generation.
     */
    void buildAdjacentKeyMap() throws KeypadException;

}
