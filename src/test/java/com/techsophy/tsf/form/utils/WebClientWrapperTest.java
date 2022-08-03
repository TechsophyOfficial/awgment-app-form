package com.techsophy.tsf.form.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.tsf.form.exception.ExternalServiceErrorException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static com.techsophy.tsf.form.constants.FormTestConstants.*;
import static com.techsophy.tsf.form.constants.FormModelerConstants.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith({SpringExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles(TEST_ACTIVE_PROFILE)
class WebClientWrapperTest
{
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    WebClientWrapper webClientWrapper;
    private WebClient webClientMock;

    @BeforeEach
    void mockWebClient()
    {
        WebClient.RequestBodyUriSpec requestBodyUriMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.RequestBodySpec requestBodyMock = mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec responseMock = mock(WebClient.ResponseSpec.class);
        webClientMock = mock(WebClient.class);
        when(webClientMock.get()).thenReturn(requestHeadersUriSpec);
        when(webClientMock.method(HttpMethod.DELETE)).thenReturn(requestBodyUriMock);
        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(webClientMock.put()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(LOCAL_HOST_URL)).thenReturn(requestBodyMock);
        when(requestHeadersUriSpec.uri(LOCAL_HOST_URL)).thenReturn(requestHeadersMock);
        when(requestBodyMock.bodyValue(TOKEN)).thenReturn(requestHeadersMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.onStatus(any(),any())).thenReturn(responseMock);
        when(responseMock.bodyToMono(String.class))
                .thenReturn(Mono.just(TEST));
    }

    @Test
    void createWebClientTest()
    {
      WebClient webClientTest=  webClientWrapper.createWebClient(TOKEN);
      Assertions.assertNotNull(webClientTest);
    }

    @Test
    void getWebClientRequestTest()
    {
        String getResponse= webClientWrapper.webclientRequest(webClientMock,LOCAL_HOST_URL, GET,null);
        assertEquals(TEST,getResponse);
        String putResponse= webClientWrapper.webclientRequest(webClientMock,LOCAL_HOST_URL,PUT,TOKEN);
        assertEquals(TEST,putResponse);
        String deleteResponse= webClientWrapper.webclientRequest(webClientMock,LOCAL_HOST_URL,DELETE,null);
        assertEquals(TEST,deleteResponse);
        String deleteBodyResponse= webClientWrapper.webclientRequest(webClientMock,LOCAL_HOST_URL,DELETE,TOKEN);
        assertEquals(TEST,deleteBodyResponse);
        String postResponse= webClientWrapper.webclientRequest(webClientMock,LOCAL_HOST_URL,POST,TOKEN);
        assertEquals(TEST,postResponse);
    }

    @Test
    void availableMethodTest() throws JsonProcessingException {
        Exception ex= new Exception("exception");
        Map<String,Object> map = new HashMap<>();
        map.put("key","value");
        Mockito.when(this.objectMapper.readValue("abc", Map.class)).thenReturn(map);
        Assertions.assertThrows(NullPointerException.class,()->webClientWrapper.availableMethod(ex));
    }

}
