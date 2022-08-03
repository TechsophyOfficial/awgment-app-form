package com.techsophy.tsf.form.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;
import com.techsophy.tsf.form.utils.WebClientWrapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static com.techsophy.tsf.form.constants.FormModelerConstants.CLIENT_ROLES;
import static com.techsophy.tsf.form.constants.FormModelerConstants.RESPONSE;
import static com.techsophy.tsf.form.constants.FormTestConstants.TEST_ACTIVE_PROFILE;
import static com.techsophy.tsf.form.constants.FormTestConstants.TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles(TEST_ACTIVE_PROFILE)
@SpringBootTest
public class JWTRoleConverterTest {
    @Mock
    WebClientWrapper webClientWrapper;
    @Mock
    ObjectMapper mockObjectMapper;

    @InjectMocks
    JWTRoleConverter jwtRoleConverter;

    @Test
    void convertTest() throws JsonProcessingException {
        Jwt jwt = mock(Jwt.class);
        Map<String,Object> userInformationMap = new HashMap<>();
        userInformationMap.put(CLIENT_ROLES, List.of("abc"));
        String response = RESPONSE;
        WebClient webClient= WebClient.builder().build();
        when(webClientWrapper.createWebClient(any())).thenReturn(webClient);
        Mockito.when(webClientWrapper.webclientRequest(any(WebClient.class),anyString(),anyString(), ArgumentMatchers.eq(null))).thenReturn(response);
        Mockito.when(this.mockObjectMapper.readValue(anyString(),ArgumentMatchers.eq(Map.class))).thenReturn(userInformationMap);
        Mockito.when(this.mockObjectMapper.convertValue(userInformationMap.get(CLIENT_ROLES),List.class)).thenReturn(List.of(response));
        Collection<GrantedAuthority> response1 = jwtRoleConverter.convert(jwt);
        Assertions.assertNotNull(response1);
    }
}
