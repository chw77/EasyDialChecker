package com.codechallenge.easydialchecker.repository;

import com.codechallenge.easydialchecker.exception.PersistenceException;
import com.codechallenge.easydialchecker.model.EasyDialText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class FileRepository implements BaseRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileRepository.class);

    @Value("${persistence-file}")
    private String persistenceFile;

    @Override
    public List<String> read() throws PersistenceException {
        List<String> records = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(persistenceFile))) {
            while (fileScanner.hasNext()) {
                String record = fileScanner.nextLine();
                records.add(record);
            }
        } catch (FileNotFoundException exception) {
            LOGGER.error("FileNotFoundException occurred - error message " + exception.getMessage());
            throw new PersistenceException(exception);
        }
        return records;
    }

    @Override
    public void save(EasyDialText easyDialText) throws PersistenceException {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(persistenceFile, true))) {
            printWriter.println(easyDialText.toString());
            printWriter.flush();
        } catch (IOException exception) {
            LOGGER.error("Internal exception occurred - error message " + exception.getMessage());
            throw new PersistenceException(exception);
        }
    }
}
