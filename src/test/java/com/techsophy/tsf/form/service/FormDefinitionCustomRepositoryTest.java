package com.techsophy.tsf.form.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.tsf.form.entity.FormDefinition;
import com.techsophy.tsf.form.repository.impl.FormDefinitionCustomRepositoryImpl;
import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.FormTestConstants.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles(TEST_ACTIVE_PROFILE)
@SpringBootTest
class FormDefinitionCustomRepositoryTest
{
    @Mock
    MongoTemplate mongoTemplate;
    @Mock
    FormDefinition mockFormDefinition;
    @InjectMocks
    FormDefinitionCustomRepositoryImpl mockFormDefinitionCustomRepositoryImpl;

    @Test
    void findByNameOrIdTest()
    {
         when(mockFormDefinitionCustomRepositoryImpl.findByNameOrId(ABC)).thenReturn(List.of(mockFormDefinition));
         List<FormDefinition> formDefinitionListTest=mockFormDefinitionCustomRepositoryImpl.findByNameOrId(ABC);
         Assertions.assertNotNull(formDefinitionListTest);
    }

    @Test
    void findByIdInTest()
    {
        when(mockFormDefinitionCustomRepositoryImpl.findByIdIn(List.of(ONE,TWO))).thenReturn(List.of(mockFormDefinition));
        List<FormDefinition> formDefinitionListTest= mockFormDefinitionCustomRepositoryImpl.findByIdIn(List.of(ONE,TWO));
        Assertions.assertNotNull(formDefinitionListTest);
    }

    @Test
    void findByNameOrIdAndTypeTest()
    {
        when(mockFormDefinitionCustomRepositoryImpl.findByNameOrIdAndType(ABC,FORM)).thenReturn(List.of(mockFormDefinition));
        List<FormDefinition> formDefinitionListTest= mockFormDefinitionCustomRepositoryImpl.findByNameOrIdAndType(ABC,FORM);
        Assertions.assertNotNull(formDefinitionListTest);
    }

    @Test
    void findByTypeAndQPagination() throws IOException
    {
        ObjectMapper objectMapperTest = new ObjectMapper();
        @Cleanup InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formsData = new String(inputStreamTest.readAllBytes());
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formsData, FormDefinition.class);
        org.springframework.data.domain.Pageable pageable= (org.springframework.data.domain.Pageable) PageRequest.of(PAGE_NUMBER,SIZE_NUMBER);
        Mockito.when(mongoTemplate.count(ArgumentMatchers.any(),eq(FormDefinition.class))).thenReturn(Long.valueOf(ID_VALUE));
        Mockito.when(mongoTemplate.find(ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(List.of(formDefinitionTest));
        Page<FormDefinition> formDefinitionListTest= mockFormDefinitionCustomRepositoryImpl.findByTypeAndQPagination(ABC,ABC,(org.springframework.data.domain.Pageable) pageable);
        Assertions.assertNotNull(formDefinitionListTest);
    }

    @Test
    void findByTypePaginationTest()
    {
        org.springframework.data.domain.Pageable pageable= PageRequest.of(PAGE_NUMBER,SIZE_NUMBER);
        Mockito.when(mongoTemplate.count(ArgumentMatchers.any(),eq(FormDefinition.class))).thenReturn(Long.valueOf(ID_VALUE));
        Page<FormDefinition> formDefinitionListTest= mockFormDefinitionCustomRepositoryImpl.findByTypePagination(ABC,pageable);
        Assertions.assertNotNull(formDefinitionListTest);
    }

    @Test
    void findByTypeSortingTest()
    {
        when(mockFormDefinitionCustomRepositoryImpl.findByTypeSorting(ABC,Sort.unsorted())).thenReturn(List.of(mockFormDefinition));
        List<FormDefinition> formDefinitionListTest= mockFormDefinitionCustomRepositoryImpl.findByTypeSorting(ABC,Sort.unsorted());
        Assertions.assertNotNull(formDefinitionListTest);
    }

    @Test
    void findByTypeAndQSortingTest()
    {
        when(mockFormDefinitionCustomRepositoryImpl.findByTypeAndQSorting(ABC,ABC,Sort.unsorted())).thenReturn(List.of(mockFormDefinition));
        List<FormDefinition> formDefinitionListTest= mockFormDefinitionCustomRepositoryImpl.findByTypeAndQSorting(ABC,ABC,Sort.unsorted());
        Assertions.assertNotNull(formDefinitionListTest);
    }

    @Test
    void findFormsByQSortingTest() throws IOException
    {
        ObjectMapper objectMapperTest = new ObjectMapper();
        InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formsData = new String(inputStreamTest.readAllBytes());
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formsData, FormDefinition.class);
        Mockito.when(mongoTemplate.find(ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(List.of(formDefinitionTest));
        Stream<FormDefinition>  formDefinitionListTest= mockFormDefinitionCustomRepositoryImpl.findFormsByQSorting(ABC,Sort.unsorted());
        Assertions.assertNotNull(formDefinitionListTest);
    }

    @Test
    void findFormsByQPageableTest() throws IOException
    {
        ObjectMapper objectMapperTest = new ObjectMapper();
        InputStream inputStreamTest = new ClassPathResource(FORMS_DATA_1).getInputStream();
        String formsData = new String(inputStreamTest.readAllBytes());
        FormDefinition formDefinitionTest = objectMapperTest.readValue(formsData, FormDefinition.class);
        org.springframework.data.domain.Pageable pageable= PageRequest.of(PAGE_NUMBER,SIZE_NUMBER);
        Mockito.when(mongoTemplate.count(ArgumentMatchers.any(),eq(FormDefinition.class))).thenReturn(Long.valueOf(ID_VALUE));
        Mockito.when(mongoTemplate.find(ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(List.of(formDefinitionTest));
        Page<FormDefinition> formDefinitionListTest= mockFormDefinitionCustomRepositoryImpl.findFormsByQPageable(ABC,pageable);
        Assertions.assertNotNull(formDefinitionListTest);
    }
}
