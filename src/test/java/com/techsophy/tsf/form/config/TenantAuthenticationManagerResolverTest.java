package com.techsophy.tsf.form.config;

import com.techsophy.tsf.form.utils.TokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class TenantAuthenticationManagerResolverTest
{
    @Mock
    TokenUtils tokenUtils;
    @Mock
    JWTRoleConverter jwtRoleConverter;
    @Mock
    HttpServletRequest httpRequest;
    @InjectMocks
    TenantAuthenticationManagerResolver tenantAuthenticationManagerResolver;

    @Test
    void resolve() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ReflectionTestUtils.setField(tenantAuthenticationManagerResolver, "keycloakIssuerUri", "abc");
        Mockito.when(tokenUtils.getIssuerFromToken(any())).thenReturn("/user");
        Assertions.assertThrows(IllegalArgumentException.class,()->tenantAuthenticationManagerResolver.resolve(httpRequest));
    }
}
