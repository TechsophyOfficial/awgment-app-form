package com.techsophy.tsf.form.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.idgenerator.IdGeneratorImpl;
import com.techsophy.tsf.form.config.GlobalMessageSource;
import com.techsophy.tsf.form.dto.*;
import com.techsophy.tsf.form.entity.FormDefinition;
import com.techsophy.tsf.form.exception.EntityIdNotFoundException;
import com.techsophy.tsf.form.exception.FormIdNotFoundException;
import com.techsophy.tsf.form.repository.FormDefinitionRepository;
import com.techsophy.tsf.form.service.impl.FormServiceImpl;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.FormTestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles(TEST_ACTIVE_PROFILE)
@ExtendWith({SpringExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FormsServiceTest
{
    @Mock
    UserDetails mockUserDetails;
    @Mock
    TokenUtils tokenUtils;
    @Mock
    FormDefinitionRepository mockFormDefinitionRepository;
    @Mock
    ObjectMapper mockObjectMapper;
    @Mock
    IdGeneratorImpl mockIdGenerator;
    @Mock
    GlobalMessageSource mockGlobalMessageSource;
    @Mock
    FormAuditService formAuditService;
    @InjectMocks
    FormServiceImpl mockFormServiceImpl;

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
        map.put(ID, BIGINTEGER_ID);
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
    void saveFormTest() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_2).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData,FormDefinition.class);
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setElasticPush(elasticPush);
        FormAuditSchema formAuditSchema =new FormAuditSchema();
        formAuditSchema.setId(ID_VALUE);
        formAuditSchema.setName(NAME);
        formAuditSchema.setComponents(COMPONENTS);
        formAuditSchema.setAcls(List.of(accessControlListDTO));
        formAuditSchema.setElasticPush(elasticPush);
        when(mockObjectMapper.convertValue(any(), eq(FormAuditSchema.class))).thenReturn(formAuditSchema);
        when(mockIdGenerator.nextId()).thenReturn(BigInteger.valueOf(Long.parseLong(ID_VALUE)));
        Mockito.when(mockUserDetails.getUserDetails()).thenReturn(userList);
        when(this.mockObjectMapper.convertValue(any(), eq(FormDefinition.class))).thenReturn(formDefinitionTest);
        when(mockFormDefinitionRepository.save(any())).thenReturn(formDefinitionTest.withId(BigInteger.valueOf(Long.parseLong(ID_VALUE))));
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponse.class))).thenReturn(new FormResponse(ID_VALUE, VERSION_VALUE));
        mockFormServiceImpl.saveForm(formSchemaTest);
        verify(mockFormDefinitionRepository, times(1)).save(any());
    }

    @Test
    void saveFormAndSaveTest() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_2).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData,FormDefinition.class);
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setElasticPush(elasticPush);
        FormAuditSchema formAuditSchema =new FormAuditSchema();
        formAuditSchema.setId(ID_VALUE);
        formAuditSchema.setName(NAME);
        formAuditSchema.setComponents(COMPONENTS);
        formAuditSchema.setAcls(List.of(accessControlListDTO));
        formAuditSchema.setElasticPush(elasticPush);
        when(mockObjectMapper.convertValue(any(), eq(FormAuditSchema.class))).thenReturn(formAuditSchema);
        when(mockIdGenerator.nextId()).thenReturn(BigInteger.valueOf(Long.parseLong(ID_VALUE)));
        Mockito.when(mockUserDetails.getUserDetails())
                .thenReturn(userList);
        when(this.mockObjectMapper.convertValue(any(), eq(FormDefinition.class)))
                .thenReturn(formDefinitionTest);
        when(mockFormDefinitionRepository.save(any())).thenReturn(formDefinitionTest.withId(BigInteger.valueOf(Long.parseLong(ID_VALUE))));
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponse.class))).thenReturn(new FormResponse(ID_VALUE, VERSION_VALUE));
        mockFormServiceImpl.saveForm(formSchemaTest);
        verify(mockFormDefinitionRepository, times(1)).save(any());
    }

    @Test
    void updateFormTypFormTest() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData,FormDefinition.class);
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setElasticPush(elasticPush);
        FormAuditSchema formAuditSchema =new FormAuditSchema();
        formAuditSchema.setId(ID_VALUE);
        formAuditSchema.setName(NAME);
        formAuditSchema.setComponents(COMPONENTS);
        formAuditSchema.setAcls(List.of(accessControlListDTO));
        formAuditSchema.setElasticPush(elasticPush);
        when(mockObjectMapper.convertValue(any(), eq(FormAuditSchema.class))).thenReturn(formAuditSchema);
        when(mockIdGenerator.nextId()).thenReturn(BigInteger.valueOf(Long.parseLong(ID_VALUE)));
        Mockito.when(mockUserDetails.getUserDetails())
                .thenReturn(userList);
        when(this.mockObjectMapper.convertValue(any(), eq(FormDefinition.class)))
                .thenReturn(formDefinitionTest);
        when(mockFormDefinitionRepository.save(any())).thenReturn(formDefinitionTest.withId(BigInteger.valueOf(Long.parseLong(ID_VALUE))));
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponse.class))).thenReturn(new FormResponse(ID_VALUE, VERSION_VALUE));
        mockFormServiceImpl.saveForm(formSchemaTest);
        verify(mockFormDefinitionRepository, times(1)).save(any());
    }

    @Test
    void updateFormTypeComponentTest() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setProperties(PROPERTIES);
        formSchemaTest.setType(TYPE_FORM);
        formSchemaTest.setVersion(VERSION_VALUE);
        formSchemaTest.setIsDefault(IS_DEFAULT_VALUE);
        formSchemaTest.setElasticPush(elasticPush);
        FormAuditSchema formAuditSchema =new FormAuditSchema();
        formAuditSchema.setId(ID_VALUE);
        formAuditSchema.setName(NAME);
        formAuditSchema.setComponents(COMPONENTS);
        formAuditSchema.setAcls(List.of(accessControlListDTO));
        formAuditSchema.setElasticPush(elasticPush);
        when(mockObjectMapper.convertValue(any(), eq(FormAuditSchema.class))).thenReturn(formAuditSchema);
        when(mockIdGenerator.nextId()).thenReturn(BigInteger.valueOf(Long.parseLong(ID_VALUE)));
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData,FormDefinition.class);
        Mockito.when(mockUserDetails.getUserDetails())
                .thenReturn(userList);
        when(this.mockObjectMapper.convertValue(any(), eq(FormDefinition.class)))
                .thenReturn(formDefinitionTest);
        when(mockFormDefinitionRepository.save(any())).thenReturn(formDefinitionTest.withId(BigInteger.valueOf(Long.parseLong(ID_VALUE))));
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponse.class))).thenReturn(new FormResponse(ID_VALUE, VERSION_VALUE));
        when(mockFormDefinitionRepository.existsById(BigInteger.valueOf(Long.parseLong(ID_VALUE)))).thenReturn(true);
        when(mockFormDefinitionRepository.findById(BigInteger.valueOf(Long.parseLong(ID_VALUE)))).thenReturn(Optional.of(formDefinitionTest));
        mockFormServiceImpl.saveForm(formSchemaTest);
        verify(mockFormDefinitionRepository, times(1)).save(any());
    }

    @Test
    void getFormByIdTest() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findById(BigInteger.valueOf(Long.parseLong(String.valueOf(1))))).thenReturn(Optional.ofNullable(formDefinitionTest));
        mockFormServiceImpl.getFormById(ID_VALUE);
        verify(mockFormDefinitionRepository, times(1)).findById(BigInteger.valueOf(1));
    }

    @Test
    void getAllFormsIncludeFormContentAndFormTest() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formsData = new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formsData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findByTypeSorting(TYPE_FORM,null )).thenReturn(List.of(formDefinitionTest));
        mockFormServiceImpl.getAllForms(true, TYPE_FORM,null,null,null );
        verify(mockFormDefinitionRepository,times(1)).findByTypeSorting(TYPE_FORM,null );
    }

    @Test
    void getAllFormsIncludeFormContentAndComponentTest() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formsData = new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formsData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findByTypeSorting(TYPE_COMPONENT,null)).thenReturn(List.of(formDefinitionTest));
        mockFormServiceImpl.getAllForms(true, TYPE_COMPONENT, null,null,null);
        verify(mockFormDefinitionRepository,times(1)).findByTypeSorting(TYPE_COMPONENT, null);
    }

    @Test
    void getAllFormsIncludeFormContentAndNoTypeTest() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formsSchema = new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formsSchema, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findAll((Sort) any())).thenReturn(List.of(formDefinitionTest));
        mockFormServiceImpl.getAllForms(true, null,null ,null,Sort.unsorted()).collect(Collectors.toList());
        verify(mockFormDefinitionRepository,times(1)).findAll((Sort) any());
    }

    @Test
    void getAllFormsNoFormContentAndNoTypeTest() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findAll()).thenReturn(List.of(formDefinitionTest));
        mockFormServiceImpl.getAllForms(false, null,null ,null,null);
        verify(mockFormDefinitionRepository,times(1)).findAll((Sort) any());
    }

    @Test
    void deleteFormById(){
        Status elasticPush = Status.DISABLED;
        Map<String,Object> component = new HashMap<>();
        component.put("key","value");
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        FormDefinition formDefinition = new FormDefinition();
        formDefinition.setId(BigInteger.ONE);
        formDefinition.setName(NAME);
        formDefinition.setComponents(COMPONENTS);
        formDefinition.setAcls(List.of(accessControlListDTO));
        formDefinition.setElasticPush(elasticPush);
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
        formResponseSchema.setElasticPush(elasticPush);
        when(mockFormDefinitionRepository.existsById(BigInteger.valueOf(1))).thenReturn(true);
        when(mockFormDefinitionRepository.findById(BigInteger.valueOf(1))).thenReturn(Optional.of(formDefinition));
        when(mockFormDefinitionRepository.deleteById(BigInteger.valueOf(1))).thenReturn(Integer.valueOf(ID_VALUE));
        when(mockObjectMapper.convertValue(any(),eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        mockFormServiceImpl.deleteFormById(ID_VALUE);
        verify(mockFormDefinitionRepository, times(1)).deleteById(BigInteger.valueOf(1));
    }

    @Test
    void deleteFormByIdExceptionTest() {
        when(mockFormDefinitionRepository.existsById(BigInteger.valueOf(1))).thenReturn(false);
        Assertions.assertThrows(EntityIdNotFoundException.class, () -> mockFormServiceImpl.deleteFormById(ID_VALUE));
    }

    @Test
    void searchFormByIdOrNameLikeTest() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest=new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData= new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest =objectMapperTest.readValue(formData,FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(),eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findByNameOrIdAndType(ID_OR_NAME_LIKE_ABC, TYPE_FORM)).thenReturn(Collections.singletonList(formDefinitionTest));
        mockFormServiceImpl.searchFormByIdOrNameLike(ID_OR_NAME_LIKE_ABC, TYPE_FORM);
        verify(mockFormDefinitionRepository, times(1)).findByNameOrIdAndType(ID_OR_NAME_LIKE_ABC, TYPE_FORM);
    }

    @Test
    void searchFormByIdAndType() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest=new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData= new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest =objectMapperTest.readValue(formData,FormDefinition.class);
        when(mockObjectMapper.convertValue(any(),eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findByNameOrIdAndType(ID_OR_NAME_LIKE_ABC, TYPE_FORM)).thenReturn(Collections.singletonList(formDefinitionTest));
        mockFormServiceImpl.searchFormByIdOrNameLike(ID_OR_NAME_LIKE_ABC, TYPE_FORM);
        verify(mockFormDefinitionRepository, times(1)).findByNameOrIdAndType(ID_OR_NAME_LIKE_ABC, TYPE_FORM);
    }

    @Test
    void searchFormByIdOrNameLikeTypeNullTest() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest=new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData= new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest =objectMapperTest.readValue(formData,FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(),eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findByNameOrId(ID_OR_NAME_LIKE_ABC)).thenReturn(Collections.singletonList(formDefinitionTest));
        mockFormServiceImpl.searchFormByIdOrNameLike(ID_OR_NAME_LIKE_ABC, null);
        verify(mockFormDefinitionRepository, times(1)).findByNameOrId(ID_OR_NAME_LIKE_ABC);
    }

    @Test
    void getAllFormsIncludeDeploymentIdListTest() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findByIdIn(List.of(ONE))).thenReturn(List.of(formDefinitionTest));
        mockFormServiceImpl.getAllForms(false, null,ONE ,null,null);
        verify(mockFormDefinitionRepository,times(1)).findByIdIn(List.of(ONE));
    }

    @Test
    void getAllFormsIncludeContentTest() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findAll()).thenReturn(List.of(formDefinitionTest));
        mockFormServiceImpl.getAllForms(true, EMPTY_TYPE,EMPTY_DEPLOYMENT_ID_LIST,null,null);
        verify(mockFormDefinitionRepository,times(1)).findAll((Sort) any());
    }

    @Test
    void getAllFormsIncludeTypeTest() throws IOException
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
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
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormResponseSchema.class))).thenReturn(formResponseSchema);
        when(mockFormDefinitionRepository.findByTypeSorting(FORM, null)).thenReturn(List.of(formDefinitionTest));
        mockFormServiceImpl.getAllForms(false, FORM,null,null,null);
        verify(mockFormDefinitionRepository,times(1)).findByTypeSorting(FORM,null );
    }

    @Test
    void getAllFormsAndFindAllTest() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setElasticPush(elasticPush);
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormSchema.class))).thenReturn(formSchemaTest);
        when(mockFormDefinitionRepository.findAll()).thenReturn(List.of(formDefinitionTest));
        mockFormServiceImpl.getAllForms(false, null,null ,null,null);
        verify(mockFormDefinitionRepository,times(1)).findAll((Sort) any());
    }

    @Test
    void getAllFormsAndPageableTest1() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formsData = new String(inputStreamTest.readAllBytes());
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setElasticPush(elasticPush);
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formsData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormSchema.class))).thenReturn(formSchemaTest);
        Page<FormDefinition> page =new PageImpl<>(List.of(formDefinitionTest)) ;
        Pageable pageable= PageRequest.of(1,1);
        Mockito. when(mockFormDefinitionRepository.findFormsByQPageable(eq(Q),any(Pageable.class))).thenReturn(page);
        PaginationResponsePayload paginationResponsePayload = new PaginationResponsePayload();
        when(tokenUtils.getPaginationResponsePayload(any(),any())).thenReturn(paginationResponsePayload);
        mockFormServiceImpl.getAllForms(true,null,Q,pageable);
        verify(mockFormDefinitionRepository,times(1)).findFormsByQPageable(eq(Q),any(Pageable.class));
    }

    @Test
    void getAllFormsFindByTypeAndQPaginationTest() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formsData = new String(inputStreamTest.readAllBytes());
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setElasticPush(elasticPush);
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formsData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormSchema.class))).thenReturn(formSchemaTest);
        Page<FormDefinition> page =new PageImpl<>(List.of(formDefinitionTest)) ;
        Pageable pageable= PageRequest.of(1,1);
        Mockito. when(mockFormDefinitionRepository.findByTypeAndQPagination(eq(TYPE_VALUE),eq(Q),any(Pageable.class))).thenReturn(page);
        PaginationResponsePayload paginationResponsePayload = new PaginationResponsePayload();
        when(tokenUtils.getPaginationResponsePayload(any(),any())).thenReturn(paginationResponsePayload);
        mockFormServiceImpl.getAllForms(true,TYPE_VALUE,Q,pageable);
        verify(mockFormDefinitionRepository,times(1)).findByTypeAndQPagination(eq(TYPE_VALUE),eq(Q),any(Pageable.class));
    }

    @Test
    void getAllFormsFindAllTest() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formsData = new String(inputStreamTest.readAllBytes());
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setElasticPush(elasticPush);
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formsData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormSchema.class))).thenReturn(formSchemaTest);
        Page<FormDefinition> page =new PageImpl<>(List.of(formDefinitionTest)) ;
        Pageable pageable= PageRequest.of(1,1);
        Mockito. when(mockFormDefinitionRepository.findAll(any(Pageable.class))).thenReturn(page);
        PaginationResponsePayload paginationResponsePayload = new PaginationResponsePayload();
        when(tokenUtils.getPaginationResponsePayload(any(),any())).thenReturn(paginationResponsePayload);
        mockFormServiceImpl.getAllForms(true,null,null,pageable);
        verify(mockFormDefinitionRepository,times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getAllFormsFindByTypePagination() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formsData = new String(inputStreamTest.readAllBytes());
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setElasticPush(elasticPush);
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formsData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormSchema.class))).thenReturn(formSchemaTest);
        Page<FormDefinition> page =new PageImpl<>(List.of(formDefinitionTest)) ;
        Pageable pageable= PageRequest.of(1,1);
        Mockito. when(mockFormDefinitionRepository.findByTypePagination(eq(TYPE_VALUE),any(Pageable.class))).thenReturn(page);
        PaginationResponsePayload paginationResponsePayload = new PaginationResponsePayload();
        when(tokenUtils.getPaginationResponsePayload(any(),any())).thenReturn(paginationResponsePayload);
        mockFormServiceImpl.getAllForms(true,TYPE_VALUE,null,pageable);
        verify(mockFormDefinitionRepository,times(1)).findByTypePagination(eq(TYPE_VALUE),any(Pageable.class));
    }

    @Test
    void getAllFormsFindByTypeAndQSortingTest() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setElasticPush(elasticPush);
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormSchema.class))).thenReturn(formSchemaTest);
        when(mockFormDefinitionRepository.findByTypeAndQSorting(TYPE_VALUE,Q,null)).thenReturn(List.of(formDefinitionTest));
        mockFormServiceImpl.getAllForms(false, TYPE_VALUE,null,Q,null);
        verify(mockFormDefinitionRepository,times(1)).findByTypeAndQSorting(TYPE_VALUE,Q, null);
    }

    @Test
    void getAllFormsAndFindFormsByQSortingTest() throws IOException
    {
        Status elasticPush = Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formData = new String(inputStreamTest.readAllBytes());
        FormSchema formSchemaTest =new FormSchema();
        formSchemaTest.setId(ID_VALUE);
        formSchemaTest.setName(NAME);
        formSchemaTest.setComponents(COMPONENTS);
        formSchemaTest.setAcls(List.of(accessControlListDTO));
        formSchemaTest.setElasticPush(elasticPush);
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formData, FormDefinition.class);
        when(this.mockObjectMapper.convertValue(any(), eq(FormSchema.class))).thenReturn(formSchemaTest);
        when(mockFormDefinitionRepository.findFormsByQSorting(Q, null)).thenReturn(Stream.of(formDefinitionTest));
        mockFormServiceImpl.getAllForms(false, null,null ,Q,null);
        verify(mockFormDefinitionRepository,times(1)).findFormsByQSorting(Q, null);
    }
}
