package com.codechallenge.easydialchecker.controller;

import com.codechallenge.easydialchecker.model.EasyDialRequest;
import com.codechallenge.easydialchecker.model.EasyDialResponse;
import com.codechallenge.easydialchecker.model.EasyDialText;
import com.codechallenge.easydialchecker.service.CacheService;
import com.codechallenge.easydialchecker.service.EasyDialService;
import com.codechallenge.easydialchecker.service.ValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EasyDialControllerTest {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CacheService cacheService;

    @Mock
    private EasyDialService easyDialService;

    @Mock
    private ValidationService validationService;

    @Mock
    private EasyDialController easyDialController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(easyDialController).build();
    }

    @ParameterizedTest
    @MethodSource("generateTestData")
    @DisplayName("Test EasyDial Endpoint Returns Successful Responses For Valid Request. ")
    void testValidEasyDialRequest(EasyDialRequest easyDialRequest, EasyDialResponse easyDialResponse) throws Exception {

        when(validationService.isValidRequest(easyDialRequest)).thenReturn(true);
        when(cacheService.readFromCache(any(String.class))).thenReturn(null);
        EasyDialText easyDialText = new EasyDialText();
        easyDialText.setValue(easyDialRequest.getInputText());
        easyDialText.setEasyToDial(Boolean.parseBoolean(easyDialResponse.getStatus()));
        when(easyDialService.checkEasyToDial(easyDialRequest.getInputText())).thenReturn(easyDialText);

        // Validate response code and content type.
        postToEasyDialEndpoint(mockMvc, easyDialRequest)
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> generateTestData() {
        EasyDialRequest easyDialRequest1 = new EasyDialRequest();
        easyDialRequest1.setInputText("4521470856");

        EasyDialResponse easyDialResponse1 = new EasyDialResponse();
        easyDialResponse1.setStatus("true");

        EasyDialRequest easyDialRequest2 = new EasyDialRequest();
        easyDialRequest2.setInputText("1956284642");

        EasyDialResponse easyDialResponse2 = new EasyDialResponse();
        easyDialResponse2.setStatus("false");

        EasyDialRequest easyDialRequest3 = new EasyDialRequest();
        easyDialRequest3.setInputText("4563 258095");

        EasyDialResponse easyDialResponse3 = new EasyDialResponse();
        easyDialResponse3.setStatus("true");

        return Stream.of(Arguments.of(easyDialRequest1, easyDialResponse1),
                Arguments.of(easyDialRequest2, easyDialResponse2),
                Arguments.of(easyDialRequest3, easyDialResponse3));

    }

    private ResultActions postToEasyDialEndpoint(MockMvc mockMvc, EasyDialRequest easyDialRequest) throws Exception {
        String requestBody = convertToJsonString(easyDialRequest);

        ResultActions resultActions = mockMvc.perform(
                post("/easydial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(requestBody)
        );
        return resultActions;
    }

    private static String convertToJsonString(final Object value) {
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return json;
    }
}
