package com.codechallenge.easydialchecker.model;

public class EasyDialKeypad {
    public static final char[][] keypad = {
            {'1', '2', '3'},
            {'4', '5', '6'},
            {'7', '8', '9'},
            {'*', '0', '*'}
    };

    public static char[][] generateKeypad() {
        return keypad;
    }
}
