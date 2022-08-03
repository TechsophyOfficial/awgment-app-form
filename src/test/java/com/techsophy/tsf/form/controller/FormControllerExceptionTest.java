package com.techsophy.tsf.form.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.tsf.form.config.CustomFilter;
import com.techsophy.tsf.form.dto.FormSchema;
import com.techsophy.tsf.form.exception.*;
import com.techsophy.tsf.form.utils.TokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static com.techsophy.tsf.form.constants.ErrorConstants.TOKEN_NOT_NULL;
import static com.techsophy.tsf.form.constants.FormModelerConstants.FORMS_URL;
import static com.techsophy.tsf.form.constants.FormTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(TEST_ACTIVE_PROFILE)
@ExtendWith({MockitoExtension.class})
@AutoConfigureMockMvc(addFilters = false)
class FormControllerExceptionTest
{
    @MockBean
    TokenUtils mockTokenUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    CustomFilter customFilter;
    @Mock
    private FormController mockFormController;

    @BeforeEach
    public void setUp()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(new GlobalExceptionHandler(),mockFormController).addFilters(customFilter).build();
    }

    @Test
    void FormIdNotFoundExceptionTest() throws Exception
    {
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormController.getFormById(ID_VALUE)).thenThrow(new FormIdNotFoundException(FORM_NOT_FOUND_WITH_GIVEN_ID,FORM_NOT_FOUND_WITH_GIVEN_ID));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.get(BASE_URL + VERSION_V1 + FORM_BY_ID_URL,1)
               .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isInternalServerError()).andReturn();
        assertEquals(500,mvcResult.getResponse().getStatus());
    }

    @Test
    void entityIdNotFoundExceptionTest() throws Exception
    {
        Mockito.when(mockTokenUtils.getIssuerFromToken(TOKEN)).thenReturn(TENANT);
        Mockito.when(mockFormController.deleteFormById(ID_VALUE)).thenThrow(new EntityIdNotFoundException(ENTITY_NOT_FOUND_WITH_GIVEN_ID,ENTITY_NOT_FOUND_WITH_GIVEN_ID));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.delete(BASE_URL + VERSION_V1 + FORM_BY_ID_URL,1)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isInternalServerError()).andReturn();
        assertEquals(500,mvcResult.getResponse().getStatus());
    }

    @Test
    void dataAccessExceptionTest() throws Exception
    {
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.post(BASE_URL + VERSION_V1 + FORMS_URL)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().is4xxClientError()).andReturn();
        assertEquals(400,mvcResult.getResponse().getStatus());
    }

    @Test
    void userDetailsNotFoundExceptionTest() throws Exception
    {
        Mockito.when(mockFormController.getFormById(ID_VALUE)).thenThrow(new UserDetailsIdNotFoundException(USER_DETAILS_NOT_FOUND_WITH_GIVEN_ID,USER_DETAILS_NOT_FOUND_WITH_GIVEN_ID));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.get(BASE_URL + VERSION_V1 + FORM_BY_ID_URL,1)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isInternalServerError()).andReturn();
        assertEquals(500,mvcResult.getResponse().getStatus());
    }

    @Test
    void invalidInputExceptionTest() throws Exception
    {
        InputStream inputStreamTest=new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormSchema.class);
        Mockito.when(mockFormController.saveForm(formSchemaTest)).thenThrow(new InvalidInputException(TOKEN_NOT_NULL,TOKEN_NOT_NULL));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.post(BASE_URL+VERSION_V1+FORMS_URL)
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isInternalServerError()).andReturn();
        assertEquals(500,mvcResult.getResponse().getStatus());
    }

    @Test
    void ExternalServiceErrorExceptionTest() throws Exception
    {
        InputStream inputStreamTest=new ClassPathResource(FORM_CONTENT).getInputStream();
        ObjectMapper objectMapperTest=new ObjectMapper();
        FormSchema formSchemaTest=objectMapperTest.readValue(inputStreamTest,FormSchema.class);
        Mockito.when(mockFormController.saveForm(formSchemaTest)).thenThrow(new ExternalServiceErrorException(TOKEN_NOT_NULL,TOKEN_NOT_NULL));
        RequestBuilder requestBuilderTest = MockMvcRequestBuilders.post(BASE_URL + VERSION_V1+ FORMS_URL)
                .content(objectMapperTest.writeValueAsString(formSchemaTest))
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(requestBuilderTest).andExpect(status().isInternalServerError()).andReturn();
        assertEquals(500,mvcResult.getResponse().getStatus());
    }
}
