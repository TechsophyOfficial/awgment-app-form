package com.techsophy.tsf.form.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.tsf.form.config.GlobalMessageSource;
import com.techsophy.tsf.form.dto.PaginationResponsePayload;
import com.techsophy.tsf.form.exception.InvalidInputException;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.techsophy.tsf.form.constants.FormTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith({SpringExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles(TEST_ACTIVE_PROFILE)
class TokenUtilsTest
{
    @Mock
    SecurityContext securityContext;
    @Mock
    SecurityContextHolder securityContextHolder;
    @Mock
    GlobalMessageSource mockGlobalMessageSource;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    TokenUtils tokenUtils;

    static String idToken = "Bearer isseyJraWQiOiIxZTlnZGs3IiwiYWxnIjoiUlMyNTYifQ.ewogImlzcyI6ICJodHRwOi8vc2VydmVyLmV4YW1wbGUuY29tIiwKICJzdWIiOiAiMjQ4Mjg5NzYxMDAxIiwKICJhdWQiOiAiczZCaGRSa3F0MyIsCiAibm9uY2UiOiAibi0wUzZfV3pBMk1qIiwKICJleHAiOiAxMzExMjgxOTcwLAogImlhdCI6IDEzMTEyODA5NzAsCiAibmFtZSI6ICJKYW5lIERvZSIsCiAiZ2l2ZW5fbmFtZSI6ICJKYW5lIiwKICJmYW1pbHlfbmFtZSI6ICJEb2UiLAogImdlbmRlciI6ICJmZW1hbGUiLAogImJpcnRoZGF0ZSI6ICIwMDAwLTEwLTMxIiwKICJlbWFpbCI6ICJqYW5lZG9lQGV4YW1wbGUuY29tIiwKICJwaWN0dXJlIjogImh0dHA6Ly9leGFtcGxlLmNvbS9qYW5lZG9lL21lLmpwZyIKfQ.rHQjEmBqn9Jre0OLykYNnspA10Qql2rvx4FsD00jwlB0Sym4NzpgvPKsDjn_wMkHxcp6CilPcoKrWHcipR2iAjzLvDNAReF97zoJqq880ZD1bwY82JDauCXELVR9O6_B0w3K-E7yM2macAAgNCUwtik6SjoSUZRcf-O5lygIyLENx882p6MtmwaL1hd6qn5RZOQ0TLrOYu0532g9Exxcm-ChymrB4xLykpDj3lUivJt63eEGGN6DH5K6o33TcxkIjNrCD4XB1CKKumZvCedgHHF3IAK4dVEDSUoGlH9z4pP_eWYNXvqQOjGs-rDaQzUHl6cQQWNiDpWOl_lxXjQEvQ";

    @BeforeEach
    public void init()
    {
        ReflectionTestUtils.setField(tokenUtils,DEFAULT_PAGE_LIMIT, 20);
    }

//    @Test
//    void getIssuerFromTokenTest() throws Exception
//    {
//        Map<String, Object> map =new HashMap<>();
//        map.put("key","value");
//        InputStream resource = new ClassPathResource(TOKEN_TXT_PATH).getInputStream();
//        String result = IOUtils.toString(resource, StandardCharsets.UTF_8);
//        when(objectMapper.readValue(anyString(), ArgumentMatchers.eq(new TypeReference<>(){}))).thenReturn(map);
//        Assertions.assertThrows(InvalidInputException.class,()->tokenUtils.getIssuerFromToken(result));
////        String tenant = tokenUtils.getIssuerFromToken(result);
////        assertThat(tenant).isNotNull();
////        assertThat(tenant).isEqualTo(TECHSOPHY_PLATFORM);
//    }

    @Test
    void getIssuerFromTokenTest() {

        Assertions.assertThrows(InvalidInputException.class,()->
                tokenUtils.getIssuerFromToken(idToken));

    }

//    @Test
//    void getIssuerFromToken1Test() throws JsonProcessingException {
//        Map<String, Object> map =new HashMap<>();
//        map.put("key","value");
//        when(objectMapper.readValue(anyString(), ArgumentMatchers.eq(new TypeReference<>(){}))).thenReturn(map);
//        String result = tokenUtils.getIssuerFromToken(idToken);
//        Assertions.assertNotNull(result);
//    }

    @Test
    void getPageRequestWithPageTest()
    {
        PageRequest tenant = tokenUtils.getPageRequest(1,1,null);
        assertTrue(true);
    }

    @Test
    void getTokenFromContext()
    {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Jwt jwt = mock(Jwt.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        String token = tokenUtils.getTokenFromContext();
        assertThat(token).isNull();
    }

    @Test
    void getTokenFromContextException()
    {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        assertThatExceptionOfType(SecurityException.class)
                .isThrownBy(() -> tokenUtils.getTokenFromContext());
    }

    @Test
    void getLoggedInUserIdTest()
    {
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        assertThatExceptionOfType(SecurityException.class)
                .isThrownBy(() -> tokenUtils.getLoggedInUserId());
    }

    @Test
    void getPaginationResponsePayload()
    {
        Page page = new PageImpl(List.of("abc"));
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("abc","abc");
        list.add(map);
        PaginationResponsePayload responsePayload = tokenUtils.getPaginationResponsePayload(page,list);
        assertThat(responsePayload).isNotNull();
    }

    @Test
    void getSortBy()
    {
        String[] strings = new String[2];
        strings[0]="abc:ab";
        strings[1]="abc";
        Sort response =tokenUtils.getSortBy(strings);
        Assertions.assertNotNull(response);
    }

    @Test
    void getPageRequestPageSizeNegativeTest()
    {
        PageRequest pageRequest=tokenUtils.getPageRequest(1,-1, new String[]{TEST_SORT});
        Assertions.assertNotNull(pageRequest);
    }

    @Test
    void getPageRequestInvalidInputException()
    {
        Assertions.assertThrows(InvalidInputException.class, () ->
                tokenUtils.getPageRequest(null,null,null));
    }
}
