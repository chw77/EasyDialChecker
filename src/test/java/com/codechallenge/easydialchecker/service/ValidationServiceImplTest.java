package com.codechallenge.easydialchecker.service;

import com.codechallenge.easydialchecker.model.EasyDialRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
public class ValidationServiceImplTest {

    @Autowired
    ValidationService validationService;

    @ParameterizedTest
    @MethodSource("generateEasyDialRequestTestData")
    @DisplayName("Validate Input Request Properties")
    void testInputRequest(EasyDialRequest request, boolean expected) {
        // Arrange.
        // Act.
        boolean isValid = validationService.isValidRequest(request);

        // Assert.
        assertThat(isValid, is(expected));
    }

    private static Stream<Arguments> generateEasyDialRequestTestData() {
        EasyDialRequest validEasyDialRequest1 = new EasyDialRequest();
        validEasyDialRequest1.setInputText("4521");

        EasyDialRequest validEasyDialRequest2 = new EasyDialRequest();
        validEasyDialRequest2.setInputText("4521470856");

        EasyDialRequest validEasyDialRequest3 = new EasyDialRequest();
        validEasyDialRequest3.setInputText("23\n6514809");

        EasyDialRequest validEasyDialRequest4 = new EasyDialRequest();
        validEasyDialRequest4.setInputText("45 21 4809");

        EasyDialRequest validEasyDialRequest5 = new EasyDialRequest();
        validEasyDialRequest5.setInputText("   56\t98 ");

        EasyDialRequest invalidEasyDialRequest1 = new EasyDialRequest();
        invalidEasyDialRequest1.setInputText("1256345.67");

        EasyDialRequest invalidEasyDialRequest2 = new EasyDialRequest();
        invalidEasyDialRequest2.setInputText("fghjgjhvk");

        EasyDialRequest invalidEasyDialRequest3 = new EasyDialRequest();
        invalidEasyDialRequest3.setInputText("hj#£g*$");

        EasyDialRequest invalidEasyDialRequest4 = new EasyDialRequest();
        invalidEasyDialRequest4.setInputText("A456hjkhg");

        EasyDialRequest invalidEasyDialRequest5 = new EasyDialRequest();
        invalidEasyDialRequest5.setInputText("-5698412570");

        return Stream.of(Arguments.of(validEasyDialRequest1, true),
                Arguments.of(validEasyDialRequest2, true),
                Arguments.of(validEasyDialRequest3, true),
                Arguments.of(validEasyDialRequest4, true),
                Arguments.of(validEasyDialRequest5, true),
                Arguments.of(invalidEasyDialRequest1, false),
                Arguments.of(invalidEasyDialRequest2, false),
                Arguments.of(invalidEasyDialRequest3, false),
                Arguments.of(invalidEasyDialRequest4, false),
                Arguments.of(invalidEasyDialRequest5, false));
    }
}
