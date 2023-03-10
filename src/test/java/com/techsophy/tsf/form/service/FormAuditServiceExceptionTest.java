package com.techsophy.tsf.form.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.idgenerator.IdGeneratorImpl;
import com.techsophy.tsf.form.config.GlobalMessageSource;
import com.techsophy.tsf.form.config.LocaleConfig;
import com.techsophy.tsf.form.dto.AccessControlListDTO;
import com.techsophy.tsf.form.dto.FormAuditSchema;
import com.techsophy.tsf.form.exception.UserDetailsIdNotFoundException;
import com.techsophy.tsf.form.repository.FormDefinitionRepository;
import com.techsophy.tsf.form.service.impl.FormAuditServiceImpl;
import com.techsophy.tsf.form.service.impl.Status;
import com.techsophy.tsf.form.utils.UserDetails;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.techsophy.tsf.form.constants.FormTestConstants.*;
import static org.mockito.Mockito.when;

@ActiveProfiles(TEST_ACTIVE_PROFILE)
@ExtendWith({SpringExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class FormAuditServiceExceptionTest
{
    @Mock
    UserDetails mockUserDetails;
    @Mock
    FormDefinitionRepository mockFormDefinitionRepository;
    @Mock
    ObjectMapper mockObjectMapper;
    @Mock
    IdGeneratorImpl idGeneratorImpl;
    @Mock
    GlobalMessageSource mockGlobalMessageSource;
    @Mock
    LocaleConfig mockLocaleConfig;
    @InjectMocks
    FormAuditServiceImpl mockFormAuditServiceImpl;

    List<Map<String, Object>> userList = new ArrayList<>();
    List<Map<String,Object>> list=new ArrayList<>();

    @BeforeEach
    public void init()
    {
        Map<String, Object> map = new HashMap<>();
        map.put(CREATED_BY_ID, NULL);
        map.put(CREATED_BY_NAME, NULL);
        map.put(CREATED_ON, NULL);
        map.put(UPDATED_BY_ID, NULL);
        map.put(UPDATED_BY_NAME, NULL);
        map.put(UPDATED_ON, NULL);
        map.put(ID, EMPTY_STRING);
        map.put(USER_NAME, USER_FIRST_NAME);
        map.put(FIRST_NAME, USER_LAST_NAME);
        map.put(LAST_NAME, USER_FIRST_NAME);
        map.put(MOBILE_NUMBER, NUMBER);
        map.put(EMAIL_ID, MAIL_ID);
        map.put(DEPARTMENT, NULL);
        userList.add(map);
        Map<String,Object> mapData =new HashMap<>();
        mapData.put("create","true");
        list.add(mapData);
    }

    @Test
    void saveAuditServiceExceptionTest() throws JsonProcessingException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        when(mockUserDetails.getUserDetails()).thenReturn(userList);
        FormAuditSchema formAuditSchema =new FormAuditSchema();
        formAuditSchema.setId(ID_VALUE);
        formAuditSchema.setName(NAME);
        formAuditSchema.setComponents(COMPONENTS);
        formAuditSchema.setAcls(List.of(accessControlListDTO));
        formAuditSchema.setElasticPush(elasticPush);
        Assertions.assertThrows(UserDetailsIdNotFoundException.class,() -> mockFormAuditServiceImpl.saveForm(formAuditSchema));
    }
}
