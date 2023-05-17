package com.techsophy.tsf.form.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.idgenerator.IdGeneratorImpl;
import com.techsophy.tsf.form.config.GlobalMessageSource;
import com.techsophy.tsf.form.dto.*;
import com.techsophy.tsf.form.entity.FormDefinition;
import com.techsophy.tsf.form.exception.EntityIdNotFoundException;
import com.techsophy.tsf.form.exception.FormIdNotFoundException;
import com.techsophy.tsf.form.exception.UserDetailsIdNotFoundException;
import com.techsophy.tsf.form.repository.FormDefinitionRepository;
import com.techsophy.tsf.form.service.FormAuditService;
import com.techsophy.tsf.form.service.FormService;
import com.techsophy.tsf.form.utils.TokenUtils;
import com.techsophy.tsf.form.utils.UserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.techsophy.tsf.form.constants.ErrorConstants.FORM_ID_NOT_FOUND;
import static com.techsophy.tsf.form.constants.ErrorConstants.LOGGED_IN_USER_ID_NOT_FOUND;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;
import static org.apache.commons.lang3.StringUtils.SPACE;

@RefreshScope
@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class FormServiceImpl implements FormService
{
    private final FormDefinitionRepository formDefinitionRepository;
    private final ObjectMapper objectMapper;
    private final UserDetails userDetails;
    private final IdGeneratorImpl idGeneratorImpl;
    private final GlobalMessageSource globalMessageSource;
    private final TokenUtils tokenUtils;
    private final FormAuditService formAuditService;

    @Override
    public FormResponse saveForm(FormSchema formSchema) throws JsonProcessingException
    {
        FormResponse formResponseDTO;
        FormDefinition formDefinition;
        String id = formSchema.getId();
        BigInteger uniqueId;
        Map<String,Object> loggedInUserDetails =userDetails.getUserDetails().get(0);
        if (StringUtils.isEmpty(loggedInUserDetails.get(ID).toString()))
        {
            throw new UserDetailsIdNotFoundException(LOGGED_IN_USER_ID_NOT_FOUND,globalMessageSource.get(LOGGED_IN_USER_ID_NOT_FOUND,loggedInUserDetails.get(ID).toString()));
        }
        BigInteger loggedInUserId = BigInteger.valueOf(Long.parseLong(loggedInUserDetails.get(ID).toString()));
        if (StringUtils.isEmpty(id))
        {
            formDefinition = setCreatedDetails(id, formSchema,loggedInUserDetails);
        }
        else
        {
            uniqueId = BigInteger.valueOf(Long.parseLong(formSchema.getId()));
            if (!formDefinitionRepository.existsById(uniqueId))
            {
                formDefinition = setCreatedDetails(id, formSchema,loggedInUserDetails);
            }
            else
            {
                formDefinition= formDefinitionRepository.findById(uniqueId).orElseThrow(() -> new EntityIdNotFoundException(LOGGED_IN_USER_ID_NOT_FOUND,globalMessageSource.get(LOGGED_IN_USER_ID_NOT_FOUND,loggedInUserDetails.get(ID).toString())));
                formDefinition.setName(formSchema.getName());
                formDefinition.setVersion(formDefinition.getVersion()+1);
                formDefinition.setComponents(formSchema.getComponents());
                formDefinition.setProperties(formSchema.getProperties());
                formDefinition.setType(formSchema.getType());
                formDefinition.setIsDefault(formSchema.getIsDefault());
                formDefinition.setElasticPush(formSchema.getElasticPush());
            }
        }
        formDefinition.setUpdatedOn(Instant.now());
        formDefinition.setUpdatedById(loggedInUserId);
        FormDefinition formSave= this.formDefinitionRepository.save(formDefinition);
        FormAuditSchema formAuditSchema =this.objectMapper.convertValue(formDefinition,FormAuditSchema.class);
        formAuditSchema.setId(idGeneratorImpl.nextId().toString());
        formAuditSchema.setFormId(formSave.getId().toString());
        this.formAuditService.saveForm(formAuditSchema);
        formResponseDTO = this.objectMapper.convertValue(formDefinition, FormResponse.class);
        return formResponseDTO;
    }

    @Override
    public FormResponseSchema getFormById(String id)
    {
        FormDefinition definition = this.formDefinitionRepository.findById(BigInteger.valueOf(Long.parseLong(id)))
                .orElseThrow(() -> new FormIdNotFoundException(FORM_ID_NOT_FOUND,globalMessageSource.get(FORM_ID_NOT_FOUND,id)));
        return this.objectMapper.convertValue(definition, FormResponseSchema.class);
    }

    @Override
    public Stream<FormResponseSchema> getAllForms(boolean includeContent, String type, String deploymentIdList, String q, Sort sort) {
        log.info(FETCH_FORMS);
        Stream<FormDefinition> formStream = null;
        if (StringUtils.isNotEmpty(deploymentIdList)) {
            String[] idList = deploymentIdList.split(REGEX_SPLIT_BY_COMMA);
            List<String> deploymentList = Arrays.asList(idList);
            formStream = this.formDefinitionRepository.findByIdIn(deploymentList).stream();
        }
        else if (StringUtils.isBlank(type) && StringUtils.isBlank(deploymentIdList) && StringUtils.isBlank(q)) {
            formStream = this.formDefinitionRepository.findAll(sort).stream();
        }
        else if (StringUtils.isNotBlank(type) && StringUtils.isBlank(q)) {
            formStream = this.formDefinitionRepository.findByTypeSorting(type, sort).stream();
        }
        else if (StringUtils.isNotBlank(q) && StringUtils.isNotBlank(type)) {
            formStream = this.formDefinitionRepository.findByTypeAndQSorting(type, q, sort).stream();
        }
        else {
            formStream = this.formDefinitionRepository.findFormsByQSorting(q, sort);
        }
        return formStream.map(this::convertEntityToDTO)
                .map(formSchema ->
                {
                    if (includeContent) {
                        return formSchema;
                    }
                    formSchema.setComponents(null);
                    return formSchema;
                });
    }

    @Override
    public PaginationResponsePayload getAllForms(boolean includeContent, String type, String q, Pageable pageable)
    {
        if(StringUtils.isBlank(q)&&StringUtils.isBlank(type))
        {
            Page<FormDefinition> formDefinitions = this.formDefinitionRepository.findAll(pageable);
            List<Map<String,Object>> workflowSchemaList = formDefinitions.stream()
                                   .map(this::convertEntityToMap).collect(Collectors.toList());
            return tokenUtils.getPaginationResponsePayload(formDefinitions,workflowSchemaList);
        }
        if(StringUtils.isNotBlank(q)&&StringUtils.isBlank(type))
        {
        Page<FormDefinition> formDefinitionRepositoryPage = this.formDefinitionRepository.findFormsByQPageable(q,pageable);
        List<Map<String,Object>> formSchemaList = formDefinitionRepositoryPage.stream()
                .map(this::convertEntityToMap).collect(Collectors.toList());
        return tokenUtils.getPaginationResponsePayload(formDefinitionRepositoryPage, formSchemaList);
        }
        if(StringUtils.isNotBlank(q)&&StringUtils.isNotBlank(type))
        {
            Page<FormDefinition> formDefinitionRepositoryPage = this.formDefinitionRepository.findByTypeAndQPagination(type,q,pageable);
            List<Map<String,Object>> formSchemaList = formDefinitionRepositoryPage.stream()
                    .map(this::convertEntityToMap).collect(Collectors.toList());
            return tokenUtils.getPaginationResponsePayload(formDefinitionRepositoryPage,formSchemaList);
        }
        Page<FormDefinition> formDefinitionRepositoryPage = this.formDefinitionRepository.findByTypePagination(type,pageable);
        List<Map<String,Object>> formSchemaList = formDefinitionRepositoryPage.stream()
                .map(this::convertEntityToMap).collect(Collectors.toList());
        return tokenUtils.getPaginationResponsePayload(formDefinitionRepositoryPage, formSchemaList);
    }

    @Override
    public boolean deleteFormById(String id)
    {
        if (!formDefinitionRepository.existsById(BigInteger.valueOf(Long.parseLong(id))))
        {
            throw new EntityIdNotFoundException(FORM_ID_NOT_FOUND,globalMessageSource.get(FORM_ID_NOT_FOUND,id));
        }
        FormResponseSchema formResponseSchema=getFormById(id);
        this.formDefinitionRepository.deleteById(BigInteger.valueOf(Long.parseLong(id)));
        return StringUtils.equals(formResponseSchema.getType(),COMPONENT);
    }

    @Override
    public Stream<FormResponseSchema> searchFormByIdOrNameLike(String idOrNameLike, String type) throws UnsupportedEncodingException
    {
        Stream<FormDefinition> formStream = null;
        if (StringUtils.isNotEmpty(type))
        {
            formStream = this.formDefinitionRepository.findByNameOrIdAndType(idOrNameLike, type).stream();
        }
        else{
            formStream =this.formDefinitionRepository.findByNameOrId(idOrNameLike).stream();
        }
        return formStream.map(this::convertEntityToDTO)
                .map(formSchema ->
                {
                    formSchema.setComponents(null);
                    return formSchema;
                });

    }

    private FormDefinition setCreatedDetails(String id,FormSchema form, Map<String,Object> loggedInUserDetails)
    {
        FormDefinition formDefinition;
        BigInteger loggedInUserId = BigInteger.valueOf(Long.parseLong(loggedInUserDetails.get(ID).toString()));
        if(StringUtils.isNotBlank(id))
        {
            formDefinition = this.objectMapper.convertValue(form, FormDefinition.class)
                    .withComponents(form.getComponents()).withId(BigInteger.valueOf(Long.parseLong(form.getId())))
                    .withVersion(1);
        }
        else
        {
            BigInteger uniqueId = idGeneratorImpl.nextId();
            formDefinition = this.objectMapper.convertValue(form, FormDefinition.class)
                    .withComponents(form.getComponents()).withId(uniqueId).withVersion(1);
        }
        if(String.valueOf(form.getIsDefault()).equals(NULL))
        {
            formDefinition.setIsDefault(false);
        }
        formDefinition.setCreatedOn(Instant.now());
        formDefinition.setCreatedById(loggedInUserId);
        return formDefinition;
    }

    private Map<String,Object> convertEntityToMap(FormDefinition formDefinition)
    {
        return this.objectMapper.convertValue(convertEntityToDTO(formDefinition),Map.class);
    }

    private FormResponseSchema convertEntityToDTO(FormDefinition formDefinition)
    {
        return this.objectMapper.convertValue(formDefinition,FormResponseSchema.class);
    }
}
