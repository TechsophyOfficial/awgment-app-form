package com.techsophy.tsf.form.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.idgenerator.IdGeneratorImpl;
import com.techsophy.tsf.form.config.GlobalMessageSource;
import com.techsophy.tsf.form.dto.AccessControlListDTO;
import com.techsophy.tsf.form.dto.FormAuditSchema;
import com.techsophy.tsf.form.dto.FormResponse;
import com.techsophy.tsf.form.dto.PaginationResponsePayload;
import com.techsophy.tsf.form.entity.FormAuditDefinition;
import com.techsophy.tsf.form.repository.FormDefinitionAuditRepository;
import com.techsophy.tsf.form.service.impl.FormAuditServiceImpl;
import com.techsophy.tsf.form.service.impl.Status;
import com.techsophy.tsf.form.utils.TokenUtils;
import com.techsophy.tsf.form.utils.UserDetails;
import lombok.Cleanup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.FormTestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles(TEST_ACTIVE_PROFILE)
@ExtendWith({SpringExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FormAuditServiceImplTest
{
    @Mock
    UserDetails mockUserDetails;
    @Mock
    ObjectMapper mockObjectMapper;
    @Mock
    TokenUtils tokenUtils;
    @Mock
    FormDefinitionAuditRepository formDefinitionAuditRepository;
    @Mock
    IdGeneratorImpl mockIdGenerator;
    @Mock
    GlobalMessageSource mockGlobalMessageSource;
    @InjectMocks
    FormAuditServiceImpl mockFormServiceImpl;
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
        map.put(ID, BIGINTEGER_ID);
        map.put(USER_NAME, USER_FIRST_NAME);
        map.put(FIRST_NAME, USER_LAST_NAME);
        map.put(LAST_NAME, USER_FIRST_NAME);
        map.put(MOBILE_NUMBER, NUMBER);
        map.put(EMAIL_ID, MAIL_ID);
        map.put(DEPARTMENT, NULL);
        userList.add(map);
    }

    @Test
    void saveFormTest() throws IOException
    {
        Status elasticPush = Status.disabled;
        List<AccessControlListDTO> list=new ArrayList<>();
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        list.add(accessControlListDTO);
        Mockito.when(mockUserDetails.getUserDetails()).thenReturn(userList);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_2).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormAuditDefinition formDefinitionTest = objectMapperTest.readValue(formData,FormAuditDefinition.class);
        FormAuditSchema formAuditSchema =new FormAuditSchema();
        formAuditSchema.setId(ID_VALUE);
        formAuditSchema.setName(NAME);
        formAuditSchema.setComponents(COMPONENTS);
        formAuditSchema.setAcls(List.of(accessControlListDTO));
        formAuditSchema.setElasticPush(elasticPush);
        when(this.mockObjectMapper.convertValue(any(), eq(FormAuditDefinition.class))).thenReturn(formDefinitionTest);
        when(formDefinitionAuditRepository.save(any())).thenReturn(formDefinitionTest);
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponse.class))).thenReturn(new FormResponse(ID_VALUE, VERSION_VALUE));
        mockFormServiceImpl.saveForm(formAuditSchema);
        verify(formDefinitionAuditRepository, times(1)).save(any());
    }

    @Test
    void getFormByIdTest() throws IOException
    {
        Status elasticPush = Status.disabled;
        List<AccessControlListDTO> list=new ArrayList<>();
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        list.add(accessControlListDTO);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_2).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormAuditDefinition formDefinitionTest = objectMapperTest.readValue(formData,FormAuditDefinition.class);
        FormAuditSchema formAuditSchema =new FormAuditSchema();
        formAuditSchema.setId(ID_VALUE);
        formAuditSchema.setName(NAME);
        formAuditSchema.setComponents(COMPONENTS);
        formAuditSchema.setAcls(List.of(accessControlListDTO));
        formAuditSchema.setElasticPush(elasticPush);
        when(this.mockObjectMapper.convertValue(any(), eq(FormAuditSchema.class))).thenReturn(formAuditSchema);
        when(formDefinitionAuditRepository.findById(BigInteger.valueOf(Long.parseLong(ID_VALUE)),VERSION_VALUE)).thenReturn(Optional.ofNullable(formDefinitionTest));
        mockFormServiceImpl.getFormsById(ID_VALUE,VERSION_VALUE);
        verify(formDefinitionAuditRepository, times(1)).findById(BigInteger.valueOf(Long.parseLong(ID_VALUE)),VERSION_VALUE);
    }

    @Test
    void getAllFormsAndSortingTest() throws IOException
    {
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_2).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormAuditDefinition formDefinitionTest = objectMapperTest.readValue(formData,FormAuditDefinition.class);
        when(formDefinitionAuditRepository.findAllById(BigInteger.valueOf(1L),(Sort) null)).thenReturn(Stream.of( formDefinitionTest));
        mockFormServiceImpl.getAllForms(ID_VALUE,true,(Sort) null);
        verify(formDefinitionAuditRepository,times(1)).findAllById(BigInteger.valueOf(Long.parseLong(ID_VALUE)),(Sort) null);
    }

    @Test
    void getAllFormsAndPaginationTest() throws IOException
    {
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_2).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormAuditDefinition formDefinitionTest = objectMapperTest.readValue(formData,FormAuditDefinition.class);
        Page<FormAuditDefinition> page =new PageImpl<>(List.of(formDefinitionTest)) ;
        Pageable pageable= PageRequest.of(PAGE_NUMBER,SIZE_NUMBER);
        PaginationResponsePayload paginationResponsePayload = new PaginationResponsePayload();
        when(tokenUtils.getPaginationResponsePayload(any(),any())).thenReturn(paginationResponsePayload);
        when(formDefinitionAuditRepository.findAllById(BigInteger.valueOf(Long.parseLong(ID_VALUE)),pageable)).thenReturn(page);
        mockFormServiceImpl.getAllForms(ID_VALUE,true,pageable);
        verify(formDefinitionAuditRepository,times(1)).findAllById(BigInteger.valueOf(Long.parseLong(ID_VALUE)),pageable);
    }
}
