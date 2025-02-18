package com.codechallenge.easydialchecker.service;

import com.codechallenge.easydialchecker.model.EasyDialText;
import com.codechallenge.easydialchecker.repository.BaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EasyDialServiceImplTest {

    @Mock
    private BaseRepository baseRepository;

    private EasyDialServiceImpl easyDialService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        easyDialService = new EasyDialServiceImpl(baseRepository);

        // Mock the keypadMap to simulate the behavior for testing
        Map<Character, List<Character>> keypadMap = new HashMap<>();
        keypadMap.put('1', Arrays.asList('2', '5', '4'));
        keypadMap.put('2', Arrays.asList('1', '3', '4', '5', '6'));
        keypadMap.put('3', Arrays.asList('2', '5', '6'));
        keypadMap.put('4', Arrays.asList('1', '2', '5', '7', '8'));
        keypadMap.put('6', Arrays.asList('2', '3', '5', '8', '9'));

        // Set the keypadMap using reflection
        setKeypadMap(keypadMap);
    }

    @Test
    public void testEasyToDial() {
        String phoneNumber = "2362412368";
        EasyDialText result = easyDialService.checkEasyToDial(phoneNumber);

        assertNotNull(result);
        assertTrue(result.isEasyToDial());
        assertEquals(phoneNumber, result.getValue());
    }

    @Test
    public void testHardToDial() {
        String phoneNumber = "1953785642";
        EasyDialText result = easyDialService.checkEasyToDial(phoneNumber);

        assertNotNull(result);
        assertFalse(result.isEasyToDial());
        assertEquals(phoneNumber, result.getValue());
    }

    @Test
    public void testEmptyPhoneNumber() {
        String phoneNumber = "";
        EasyDialText result = easyDialService.checkEasyToDial(phoneNumber);

        assertNotNull(result);
        assertTrue(result.isEasyToDial());  // Assuming an empty number is "easy" by default.
        assertEquals(phoneNumber, result.getValue());
    }

    @Test
    public void testSingleDigitPhoneNumber() {
        String phoneNumber = "2";
        EasyDialText result = easyDialService.checkEasyToDial(phoneNumber);

        assertNotNull(result);
        assertTrue(result.isEasyToDial());  // A single digit is considered "easy."
        assertEquals(phoneNumber, result.getValue());
    }

    private void setKeypadMap(Map<Character, List<Character>> keypadMap) {
        try {
            java.lang.reflect.Field field = EasyDialServiceImpl.class.getDeclaredField("keypadMap");
            field.setAccessible(true);
            field.set(easyDialService, keypadMap);
        } catch (Exception e) {
        }
    }
}

