package com.codechallenge.easydialchecker.service;

import com.codechallenge.easydialchecker.exception.PersistenceException;
import com.codechallenge.easydialchecker.model.EasyDialText;
import com.codechallenge.easydialchecker.repository.BaseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CacheServiceImplTest {

    @Mock
    private CacheService cacheService;

    @Mock
    private BaseRepository baseRepository;

    // Read method tests.
    @ParameterizedTest
    @MethodSource("generateReadFromCacheTestData")
    @DisplayName("Test Input Text Reads From Cache")
    void testReadFromCache(String input, EasyDialText expected) {
        // Arrange.
        when(cacheService.readFromCache(input)).thenReturn(expected);

        // Act.
        EasyDialText actual = cacheService.readFromCache(input);

        // Assert.
        assertThat(actual.getValue(), is(expected.getValue()));
        assertThat(actual.isEasyToDial(), is(expected.isEasyToDial()));
    }

    @Test
    @DisplayName("Test Input Text Is Unavailable To Read From Cache")
    void testUnableToReadFromCache() {
        // Arrange.
        when(cacheService.readFromCache("2365148095")).thenReturn(null);

        // Act.
        EasyDialText actual = cacheService.readFromCache("2365148095");

        // Assert.
        assertNull(actual);
    }

    // Preload method test.
    @Test
    @DisplayName("Test Preload Data From Cache")
    void testPreLoadCache() throws PersistenceException {
        // Arrange.
        List<String> records = new ArrayList<>();
        records.add("1254709853,true");
        records.add("7854235708,true");
        when(baseRepository.read()).thenReturn(records);

        // Act.
        cacheService.preloadCache();

        // Assert.
        verify(cacheService, times(1)).preloadCache();
    }

    // Private methods.
    private static Stream<Arguments> generateReadFromCacheTestData() {
        List<EasyDialText> easyDialTexts = generateEasyDialTexts();

        return Stream.of(Arguments.of("4521470856", easyDialTexts.get(0)),
                Arguments.of("7854263258", easyDialTexts.get(1)),
                Arguments.of("2365780965", easyDialTexts.get(2)));
    }

    private static List<EasyDialText> generateEasyDialTexts() {
        EasyDialText easyDialText1 = new EasyDialText();
        easyDialText1.setValue("4521470856");
        easyDialText1.setEasyToDial(true);

        EasyDialText easyDialText2 = new EasyDialText();
        easyDialText2.setValue("7854263258");
        easyDialText2.setEasyToDial(true);

        EasyDialText easyDialText3 = new EasyDialText();
        easyDialText3.setValue("2365780965");
        easyDialText3.setEasyToDial(false);

        List<EasyDialText> easyDialTexts = new ArrayList<>();
        easyDialTexts.add(easyDialText1);
        easyDialTexts.add(easyDialText2);
        easyDialTexts.add(easyDialText3);

        return easyDialTexts;
    }

}
