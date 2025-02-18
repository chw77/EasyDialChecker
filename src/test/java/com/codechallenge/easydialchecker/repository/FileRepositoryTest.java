package com.codechallenge.easydialchecker.repository;

import com.codechallenge.easydialchecker.exception.PersistenceException;
import com.codechallenge.easydialchecker.model.EasyDialText;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FileRepositoryTest {

    @Mock
    private BaseRepository baseRepository;

    @Test
    @DisplayName("Test Read Method Throws PersistenceException")
    void testFileRepositoryReadThrowsPersistenceException() throws PersistenceException {
        // Arrange.
        when(baseRepository.read()).thenThrow(PersistenceException.class);

        // Act.
        Exception exception = null;
        try {
            baseRepository.read();
        } catch (Exception e) {
            exception = e;
        }

        // Assert.
        assertThat(exception, is(notNullValue()));
        assertThat(exception, instanceOf(PersistenceException.class));
    }

    @Test
    @DisplayName("Test Save Method Throws PersistenceException")
    void testFileRepositorySaveThrowsPersistenceException() throws PersistenceException {
        // Arrange.
        EasyDialText easyDialText = new EasyDialText();
        willThrow(new PersistenceException()).given(baseRepository).save(easyDialText);

        // Act.
        Exception exception = null;
        try {
            baseRepository.save(easyDialText);
        } catch (Exception e) {
            exception = e;
        }

        // Assert.
        assertThat(exception, is(notNullValue()));
        assertThat(exception, instanceOf(PersistenceException.class));
    }

    @Test
    @DisplayName("Test Read Method Returns List of Records")
    void testFileRepositoryReadReturnsList() throws PersistenceException {
        // Arrange.
        List<String> expected = new ArrayList<>();
        expected.add("1254709853,true");
        expected.add("7854235708,true");
        when(baseRepository.read()).thenReturn(expected);

        // Act.
        List<String> actual = baseRepository.read();

        // Assert.
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(expected.size()));
    }

}
