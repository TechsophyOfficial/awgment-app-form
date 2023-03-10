package com.techsophy.tsf.form.config;

import com.techsophy.tsf.form.utils.TokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import static org.mockito.ArgumentMatchers.any;


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
    void resolve() {
        ReflectionTestUtils.setField(tenantAuthenticationManagerResolver, "keycloakIssuerUri", "abc");
        Mockito.when(tokenUtils.getIssuerFromToken(any())).thenReturn("/user");
        Assertions.assertThrows(IllegalArgumentException.class,()->tenantAuthenticationManagerResolver.resolve(httpRequest));
    }
}
