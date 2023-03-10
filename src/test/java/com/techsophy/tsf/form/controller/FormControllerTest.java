package com.techsophy.tsf.form.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.tsf.form.config.CustomFilter;
import com.techsophy.tsf.form.dto.*;
import com.techsophy.tsf.form.service.FormService;
import com.techsophy.tsf.form.service.impl.Status;
import com.techsophy.tsf.form.utils.TokenUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.FormTestConstants.*;
import static com.techsophy.tsf.form.constants.FormTestConstants.BASE_URL;
import static com.techsophy.tsf.form.constants.FormTestConstants.COMPONENTS;
import static com.techsophy.tsf.form.constants.FormTestConstants.FORMS_URL;
import static com.techsophy.tsf.form.constants.FormTestConstants.FORM_BY_ID_URL;
import static com.techsophy.tsf.form.constants.FormTestConstants.ID;
import static com.techsophy.tsf.form.constants.FormTestConstants.ID_OR_NAME_LIKE;
import static com.techsophy.tsf.form.constants.FormTestConstants.INCLUDE_CONTENT;
import static com.techsophy.tsf.form.constants.FormTestConstants.SEARCH_FORM_URL;
import static com.techsophy.tsf.form.constants.FormTestConstants.TOKEN;
import static com.techsophy.tsf.form.constants.FormTestConstants.TYPE;
import static com.techsophy.tsf.form.constants.FormTestConstants.VERSION_V1;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(TEST_ACTIVE_PROFILE)
@ExtendWith({MockitoExtension.class})
@AutoConfigureMockMvc(addFilters = false)
class FormControllerTest
{
    private static  final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtSaveOrUpdate = jwt().authorities(new SimpleGrantedAuthority(AWGMENT_FORM_CREATE_OR_UPDATE));
    private static  final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRead = jwt().authorities(new SimpleGrantedAuthority(AWGMENT_FORM_READ));
    private static  final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtDelete = jwt().authorities(new SimpleGrantedAuthority(AWGMENT_FORM_DELETE));

    @MockBean
    TokenUtils mockTokenUtils;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    FormService mockFormService;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    CustomFilter customFilter;

    @BeforeEach public void setUp()
    {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(customFilter)
                .apply(springSecurity())
                .build();
    }

    @Test
    void saveFormComponentTypeTest() throws Exception
    {
        Status elasticPush = Status.DISABLED;
        InputStream inputStreamTest=new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormSchema.class);
        FormSchema formSchema= new FormSchema();
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
//        FormSchema formSchema = new FormSchema(ID_VALUE,NAME,COMPONENTS,List.of(accessControlListDTO),PROPERTIES,"component", VERSION_VALUE, IS_DEFAULT_VALUE);
        formSchema.setId(ID_VALUE);
        formSchema.setName(NAME);
        formSchema.setComponents(COMPONENTS);
        formSchema.setAcls(List.of(accessControlListDTO));
        formSchema.setElasticPush(elasticPush);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.saveForm(formSchemaTest)).thenReturn(new FormResponse(ID_VALUE, VERSION_VALUE));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.post(BASE_URL + VERSION_V1 + FORMS_URL)
                .content(objectMapperTest.writeValueAsString(formSchemaTest)).content(objectMapperTest.writeValueAsString(formSchema))
                .with(jwtSaveOrUpdate)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void saveFormTest() throws Exception
    {
        Status push = Status.DISABLED;
        ObjectMapper objectMapperTest=new ObjectMapper();
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        FormSchema formSchema = new FormSchema();
        formSchema.setId(ID_VALUE);
        formSchema.setName(NAME);
        formSchema.setComponents(COMPONENTS);
        formSchema.setAcls(List.of(accessControlListDTO));
        formSchema.setElasticPush(push);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.saveForm(formSchema)).thenReturn(new FormResponse(ID_VALUE, VERSION_VALUE));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.post(BASE_URL + VERSION_V1 + FORMS_URL)
                .content(objectMapperTest.writeValueAsString(formSchema))
                .with(jwtSaveOrUpdate)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void getFormByIdTest() throws Exception
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        InputStream inputStreamTest =new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormResponseSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormResponseSchema.class);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.getFormById(ID_VALUE)).thenReturn(new FormResponseSchema(ID_VALUE, NAME, COMPONENTS,List.of(accessControlListDTO),PROPERTIES, TYPE_FORM, VERSION_VALUE,IS_DEFAULT_VALUE,CREATED_BY_ID_VALUE,CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT,status));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.get(BASE_URL + VERSION_V1 + FORM_BY_ID_URL,1)
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void getFormByIdComponentTypeTest() throws Exception
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        InputStream inputStreamTest =new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormResponseSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormResponseSchema.class);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.getFormById(ID_VALUE)).thenReturn(new FormResponseSchema(ID_VALUE, NAME, COMPONENTS,List.of(accessControlListDTO),PROPERTIES, TYPE_FORM, VERSION_VALUE,IS_DEFAULT_VALUE,CREATED_BY_ID_VALUE,CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT,status));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.get(BASE_URL + VERSION_V1 + FORM_BY_ID_URL,1)
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }




    @Test
    void getAllFormsSortingTest() throws Exception
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        InputStream inputStreamTest =new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormResponseSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormResponseSchema.class);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.getAllForms(true, TYPE_FORM,null,null,null )).thenReturn(Stream.of(
                new FormResponseSchema(ID_VALUE, NAME, COMPONENTS,List.of(accessControlListDTO),PROPERTIES, TYPE_FORM, VERSION_VALUE,IS_DEFAULT_VALUE,CREATED_BY_ID_VALUE,CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT,status),
                new FormResponseSchema(ID_VALUE, NAME, COMPONENTS,List.of(accessControlListDTO),PROPERTIES, TYPE_FORM, VERSION_VALUE,IS_DEFAULT_VALUE,CREATED_BY_ID_VALUE,CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT,status)));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.get(BASE_URL + VERSION_V1 + FORMS_URL).param(INCLUDE_CONTENT, String.valueOf(true)).param(TYPE,FORM).param(DEPLOYMENT_ID_LIST,A)
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void getAllFormsSortingComponentTest() throws Exception
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        InputStream inputStreamTest =new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormResponseSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormResponseSchema.class);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.getAllForms(true, TYPE_FORM,null,null,null )).thenReturn(Stream.of(
                //new FormResponseSchema(ID_VALUE, NAME, COMPONENTS,List.of(accessControlListDTO),PROPERTIES,TYPE_FORM, VERSION_VALUE,IS_DEFAULT_VALUE, CREATED_BY_ID_VALUE, CREATED_ON_INSTANT,CREATED_BY_NAME, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT,UPDATED_BY_NAME),
                new FormResponseSchema(ID_VALUE, NAME, COMPONENTS,List.of(accessControlListDTO),PROPERTIES, TYPE_FORM, VERSION_VALUE,IS_DEFAULT_VALUE,CREATED_BY_ID_VALUE,CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT,status)));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.get(BASE_URL + VERSION_V1 + FORMS_URL).param(INCLUDE_CONTENT, String.valueOf(true)).param(TYPE,"component").param(DEPLOYMENT_ID_LIST,A)
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void getAllFormsPaginationTest() throws Exception
    {
        PaginationResponsePayload paginationResponsePayload=new PaginationResponsePayload();
        InputStream inputStreamTest =new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormResponseSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormResponseSchema.class);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.getAllForms(true,TYPE,QUERY, PageRequest.of(1,1, Sort.by(NAME)))).thenReturn(paginationResponsePayload);
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.get(BASE_URL + VERSION_V1 + FORMS_URL)
                .param(INCLUDE_CONTENT, String.valueOf(true))
                .param(PAGE, PAGE_VALUE).param(SIZE,SIZE_VALUE)
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void getAllFormsPaginationComponentTest() throws Exception
    {
        PaginationResponsePayload paginationResponsePayload=new PaginationResponsePayload();
        InputStream inputStreamTest =new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormResponseSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormResponseSchema.class);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.getAllForms(true,TYPE,QUERY, PageRequest.of(1,1, Sort.by(NAME)))).thenReturn(paginationResponsePayload);
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.get(BASE_URL + VERSION_V1 + FORMS_URL)
                .param(INCLUDE_CONTENT, String.valueOf(true)).param(TYPE,"component")
                .param(PAGE, PAGE_VALUE).param(SIZE,SIZE_VALUE)
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteFormTest() throws Exception
    {
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        RequestBuilder requestBuilderTest=MockMvcRequestBuilders
                .delete(BASE_URL + VERSION_V1 + FORM_BY_ID_URL,1)
                .with(jwtDelete);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteFormByIdTest() throws Exception
    {
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.deleteFormById(ID_VALUE)).thenReturn(true);
        RequestBuilder requestBuilderTest=MockMvcRequestBuilders
                .delete(BASE_URL + VERSION_V1 + FORM_BY_ID_URL,1)
                .with(jwtDelete);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void searchFormByIdOrNameLike() throws Exception
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        InputStream inputStreamTest =new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormSchema.class);
        Mockito.when(mockFormService.searchFormByIdOrNameLike(ID_VALUE, TYPE_FORM)).thenReturn(Stream.of(new FormResponseSchema(ID_VALUE, NAME, COMPONENTS,List.of(accessControlListDTO),PROPERTIES, TYPE_FORM, VERSION_VALUE,IS_DEFAULT_VALUE,CREATED_BY_ID_VALUE,CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT,status),
                new FormResponseSchema(ID_VALUE, NAME, COMPONENTS,List.of(accessControlListDTO),PROPERTIES, TYPE_FORM, VERSION_VALUE,IS_DEFAULT_VALUE,CREATED_BY_ID_VALUE,CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT,status)));
        RequestBuilder requestBuilderTest=MockMvcRequestBuilders.get(BASE_URL + VERSION_V1 + SEARCH_FORM_URL).param(ID_OR_NAME_LIKE, String.valueOf(1)).param(TYPE,FORM)
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void searchFormByIdOrNameLikeComponentTypeTest() throws Exception
    {
        Status status= Status.DISABLED;
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        InputStream inputStreamTest =new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormSchema.class);
        Mockito.when(mockFormService.searchFormByIdOrNameLike(ID_VALUE, TYPE_FORM)).thenReturn(Stream.of(new FormResponseSchema(ID_VALUE, NAME, COMPONENTS,List.of(accessControlListDTO),PROPERTIES, TYPE_FORM, VERSION_VALUE,IS_DEFAULT_VALUE,CREATED_BY_ID_VALUE,CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT,status),
                new FormResponseSchema(ID_VALUE, NAME, COMPONENTS,List.of(accessControlListDTO),PROPERTIES, TYPE_FORM, VERSION_VALUE,IS_DEFAULT_VALUE,CREATED_BY_ID_VALUE,CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT,status)));
        RequestBuilder requestBuilderTest=MockMvcRequestBuilders.get(BASE_URL + VERSION_V1 + SEARCH_FORM_URL).param(ID_OR_NAME_LIKE, String.valueOf(1)).param(TYPE,"component")
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }
}
