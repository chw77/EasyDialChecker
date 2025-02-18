package com.codechallenge.easydialchecker.service;

import com.codechallenge.easydialchecker.exception.KeypadException;
import com.codechallenge.easydialchecker.exception.PersistenceException;
import com.codechallenge.easydialchecker.model.EasyDialKeypad;
import com.codechallenge.easydialchecker.model.EasyDialText;
import com.codechallenge.easydialchecker.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EasyDialServiceImpl implements EasyDialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyDialServiceImpl.class);

    private static final String KEYPAD_ERROR_MESSAGE = "Adjacent key map creation failed.";
    private static final String PERSISTENCE_ERROR = "Easy dial text response persistence failed.";

    private final Map<Character, List<Character>> keypadMap = new HashMap<>();
    private final BaseRepository baseRepository;

    public EasyDialServiceImpl(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Override
    public EasyDialText checkEasyToDial(String phoneNumber) {
        EasyDialText easyDialText = new EasyDialText();

        // Easy to dial check logic.
        boolean isEasyToDial = true;
        for (int i = 0; i < phoneNumber.length() - 1; i++) {
            char current = phoneNumber.charAt(i);
            char next = phoneNumber.charAt(i + 1);
            if (!keypadMap.getOrDefault(current, Collections.emptyList()).contains(next)) {
                isEasyToDial = false;
            }
        }
        easyDialText.setValue(phoneNumber);
        easyDialText.setEasyToDial(isEasyToDial);
        return easyDialText;
    }

    @Override
    public void saveEasyDialText(EasyDialText easyDialText) throws PersistenceException {
        // Persist easy to dial phone numbers.
        try {
            baseRepository.save(easyDialText);
        } catch (Exception exception) {
            LOGGER.error(PERSISTENCE_ERROR + " {}", exception.getMessage());
            throw new PersistenceException(PERSISTENCE_ERROR, exception);
        }

    }

    @Override
    public void buildAdjacentKeyMap() throws KeypadException {
        char[][] keypad = EasyDialKeypad.generateKeypad();
        int rows = keypad.length;
        int cols = keypad[0].length;

        // Up, Down, Left, Right and Diagonal moves.
        int[][] direction = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        try {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    char key = keypad[i][j];
                    // Skip '*' and '#' while adding to the adjacency map
                    if (key == '*' || key == '#') continue;

                    List<Character> adjacentKeys = new ArrayList<>();

                    // Iterate through the phone number, checking each adjacent digits
                    for (int[] dir : direction) {
                        int newRow = i + dir[0];
                        int newCol = j + dir[1];

                        if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                            char adjacent = keypad[newRow][newCol];
                            // exclude '*' and '#' from the adjacency map
                            if (adjacent != '*' && adjacent != '#') {
                                adjacentKeys.add(keypad[newRow][newCol]);
                            }
                        }
                    }
                    keypadMap.put(key, adjacentKeys);

                }
            }
        } catch (Exception exception) {
            LOGGER.error(KEYPAD_ERROR_MESSAGE + " {}", exception.getMessage());
            throw new KeypadException(KEYPAD_ERROR_MESSAGE, exception);
        }
    }
}
