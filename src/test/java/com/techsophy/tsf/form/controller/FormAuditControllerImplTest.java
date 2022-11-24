package com.techsophy.tsf.form.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.tsf.form.config.CustomFilter;
import com.techsophy.tsf.form.dto.*;
import com.techsophy.tsf.form.service.FormAuditService;
import com.techsophy.tsf.form.utils.TokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import java.util.List;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.FormModelerConstants.FORMS_URL;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;
import static com.techsophy.tsf.form.constants.FormTestConstants.BASE_URL;
import static com.techsophy.tsf.form.constants.FormTestConstants.COMPONENTS;
import static com.techsophy.tsf.form.constants.FormTestConstants.FORM_BY_ID_URL;
import static com.techsophy.tsf.form.constants.FormTestConstants.INCLUDE_CONTENT;
import static com.techsophy.tsf.form.constants.FormTestConstants.TOKEN;
import static com.techsophy.tsf.form.constants.FormTestConstants.TYPE;
import static com.techsophy.tsf.form.constants.FormTestConstants.VERSION_V1;
import static com.techsophy.tsf.form.constants.FormTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(TEST_ACTIVE_PROFILE)
@ExtendWith({MockitoExtension.class})
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FormAuditControllerImplTest
{
    private static  final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtSaveOrUpdate = jwt().authorities(new SimpleGrantedAuthority(AWGMENT_FORM_CREATE_OR_UPDATE));
    private static  final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRead = jwt().authorities(new SimpleGrantedAuthority(AWGMENT_FORM_READ));

    @MockBean
    TokenUtils mockTokenUtils;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    FormAuditService mockFormService;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    CustomFilter customFilter;

    @BeforeEach
    public void setUp()
    {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .addFilters(customFilter)
                .build();
    }

    @Test
    void saveFormTest() throws Exception
    {
        InputStream inputStreamTest=new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormAuditSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormAuditSchema.class);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.saveForm(formSchemaTest)).thenReturn(new FormResponse(ID_VALUE, VERSION_VALUE));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.post(BASE_URL+VERSION_V1+ HISTORY+FORMS_URL)
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .with(jwtSaveOrUpdate)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void saveFormComponentTypeTest() throws Exception
    {
        InputStream inputStreamTest=new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormAuditSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormAuditSchema.class);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.saveForm(formSchemaTest)).thenReturn(new FormResponse(ID_VALUE, VERSION_VALUE));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.post(BASE_URL+VERSION_V1+ HISTORY+FORMS_URL)
                .content(objectMapperTest.writeValueAsString(formSchemaTest)).param(TYPE,"component")
                .with(jwtSaveOrUpdate)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void getFormByIdTest() throws Exception
    {
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        FormAuditResponseSchema formSchemaTest =new FormAuditResponseSchema(ID_VALUE,FORM_ID_VALUE,NAME,COMPONENTS, List.of(accessControlListDTO),PROPERTIES,TYPE_FORM, VERSION_VALUE, IS_DEFAULT_VALUE ,CREATED_BY_ID_VALUE, CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.getFormsById(ID_VALUE,1)).thenReturn(formSchemaTest);
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.get(BASE_URL+VERSION_V1+ HISTORY+FORM_VERSION_BY_ID_URL,1,1)
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void getAllFormsSortingTest() throws Exception
    {
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        FormAuditResponseSchema formSchemaTest =new FormAuditResponseSchema(ID_VALUE,FORM_ID_VALUE,NAME,COMPONENTS, List.of(accessControlListDTO),PROPERTIES,TYPE_FORM, VERSION_VALUE, IS_DEFAULT_VALUE ,CREATED_BY_ID_VALUE, CREATED_ON_INSTANT, UPDATED_BY_ID_VALUE, UPDATED_ON_INSTANT);
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormService.getAllForms(ID_VALUE,true, (Sort) null)).thenReturn(Stream.of(formSchemaTest));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders
                .get(BASE_URL+VERSION_V1+ HISTORY+FORM_BY_ID_URL,1)
                .param(INCLUDE_CONTENT, String.valueOf(true))
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void getAllFormsPaginationTest() throws Exception
    {
        AccessControlListDTO accessControlListDTO = new AccessControlListDTO(TYPE,"value",true,true,true,true,true);
        ObjectMapper objectMapperTest = new ObjectMapper();
//        FormAuditResponseSchema formSchemaTest =new FormAuditResponseSchema(ID_VALUE,ID_VALUE,NAME,COMPONENTS,List.of(accessControlListDTO),PROPERTIES,TYPE_FORM,VERSION_VALUE,IS_DEFAULT_VALUE,CREATED_BY_ID_VALUE,CREATED_ON_INSTANT,CREATED_BY_NAME,UPDATED_BY_ID_VALUE,UPDATED_ON_INSTANT,UPDATED_BY_NAME);
//        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        PaginationResponsePayload paginationResponsePayloadTest =new PaginationResponsePayload();
        Mockito.when(mockFormService.getAllForms(ID_VALUE,true, PageRequest.of(1,1,Sort.by(NAME)))).thenReturn(paginationResponsePayloadTest);
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.get(BASE_URL+VERSION_V1+ HISTORY+FORM_BY_ID_URL,1)
                .param(INCLUDE_CONTENT, String.valueOf(true))
                .param(PAGE,PAGE_VALUE)
                .param(SIZE,SIZE_VALUE)
                .with(jwtRead)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isOk()).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }
}
