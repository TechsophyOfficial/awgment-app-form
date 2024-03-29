package com.techsophy.tsf.form.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.idgenerator.IdGeneratorImpl;
import com.techsophy.tsf.form.config.GlobalMessageSource;
import com.techsophy.tsf.form.config.LocaleConfig;
import com.techsophy.tsf.form.dto.AccessControlListDTO;
import com.techsophy.tsf.form.dto.FormResponseSchema;
import com.techsophy.tsf.form.dto.FormSchema;
import com.techsophy.tsf.form.exception.EntityIdNotFoundException;
import com.techsophy.tsf.form.exception.FormIdNotFoundException;
import com.techsophy.tsf.form.exception.UserDetailsIdNotFoundException;
import com.techsophy.tsf.form.repository.FormDefinitionRepository;
import com.techsophy.tsf.form.service.impl.FormServiceImpl;
import com.techsophy.tsf.form.service.impl.Status;
import com.techsophy.tsf.form.utils.UserDetails;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigInteger;
import java.util.*;
import static com.techsophy.tsf.form.constants.FormTestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles(TEST_ACTIVE_PROFILE)
@ExtendWith({SpringExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class FormServiceExceptionTest
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
    FormServiceImpl mockFormServiceImpl;

    List<Map<String, Object>> userList = new ArrayList<>();

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
    }

    @Test
    void saveFormExceptionTest() throws JsonProcessingException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("create","true");
        list.add(map);
        when(mockUserDetails.getUserDetails()).thenReturn(userList);
        FormSchema formSchema =new FormSchema();
        formSchema.setId(ID_VALUE);
        formSchema.setName(NAME);
        formSchema.setComponents(COMPONENTS);
        formSchema.setAcls(List.of(accessControlListDTO));
        formSchema.setElasticPush(elasticPush);
        Assertions.assertThrows(UserDetailsIdNotFoundException.class,() -> mockFormServiceImpl.saveForm(formSchema));
    }

    @Test
    void getFormByIdExceptionTest()
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("create","true");
        list.add(map);
        FormResponseSchema formResponseSchema = new FormResponseSchema();
        formResponseSchema.setId(ID_VALUE);
        formResponseSchema.setName(NAME);
        formResponseSchema.setComponents(COMPONENTS);
        formResponseSchema.setAcls(List.of(accessControlListDTO));
        formResponseSchema.setProperties(PROPERTIES);
        formResponseSchema.setType(TYPE_FORM);
        formResponseSchema.setVersion(VERSION_VALUE);
        formResponseSchema.setIsDefault(IS_DEFAULT_VALUE);
        formResponseSchema.setCreatedById(CREATED_BY_ID_VALUE);
        formResponseSchema.setCreatedOn(CREATED_ON_INSTANT);
        formResponseSchema.setUpdatedById(UPDATED_BY_ID_VALUE);
        formResponseSchema.setUpdatedOn(UPDATED_ON_INSTANT);
        formResponseSchema.setElasticPush(status);
        when(this.mockObjectMapper.convertValue(any(),eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findById(BigInteger.valueOf(Long.parseLong(ID_VALUE)))).thenReturn(Optional.empty());
        Assertions.assertThrows(FormIdNotFoundException.class,()-> mockFormServiceImpl.getFormById(ID_VALUE));
    }

    @Test
    void deleteFormByIdExceptionTest()
    {
        when(mockFormDefinitionRepository.existsById(BigInteger.valueOf(1))).thenReturn(false);
        Assertions.assertThrows(EntityIdNotFoundException.class,()-> mockFormServiceImpl.deleteFormById(ID_VALUE));
    }
}
