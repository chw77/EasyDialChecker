package com.codechallenge.easydialchecker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanupUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CleanupUtils.class);

    private CleanupUtils() {
    }

    /**
     * Removes all whitespace characters (spaces, tabs, newlines) from the given input string.
     *
     * This method trims leading and trailing whitespace and replaces any internal whitespaces.
     * If an exception occurs during processing, it logs an error message and rethrows the exception.
     *
     * @param input the input string from which whitespace should be removed
     * @return a new string with all whitespace removed
     * @throws RuntimeException if an error occurs during whitespace removal
     */
    public static String removeWhitespace(String input) {
        try {
            return input.replaceAll("^\\s+|\\s+$|\\s+", "");
        } catch (Exception exception) {
            LOGGER.error("Remove whitespace Validation Failure. Message " + exception.getMessage());
            throw exception;
        }
    }
}
