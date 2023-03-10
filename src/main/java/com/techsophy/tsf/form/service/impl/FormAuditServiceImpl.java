package com.techsophy.tsf.form.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.tsf.form.config.GlobalMessageSource;
import com.techsophy.tsf.form.dto.FormAuditResponseSchema;
import com.techsophy.tsf.form.dto.FormAuditSchema;
import com.techsophy.tsf.form.dto.FormResponse;
import com.techsophy.tsf.form.dto.PaginationResponsePayload;
import com.techsophy.tsf.form.entity.FormAuditDefinition;
import com.techsophy.tsf.form.exception.EntityIdNotFoundException;
import com.techsophy.tsf.form.exception.UserDetailsIdNotFoundException;
import com.techsophy.tsf.form.repository.FormDefinitionAuditRepository;
import com.techsophy.tsf.form.service.FormAuditService;
import com.techsophy.tsf.form.utils.TokenUtils;
import com.techsophy.tsf.form.utils.UserDetails;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.ErrorConstants.FORM_ID_NOT_FOUND;
import static com.techsophy.tsf.form.constants.ErrorConstants.LOGGED_IN_USER_ID_NOT_FOUND;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;
import static org.apache.commons.lang3.StringUtils.SPACE;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class FormAuditServiceImpl implements FormAuditService
{
    private final FormDefinitionAuditRepository formDefinitionAuditRepository;
    private final ObjectMapper objectMapper;
    private final GlobalMessageSource globalMessageSource;
    private final TokenUtils tokenUtils;
    private final UserDetails userDetails;

    @Override
    public FormResponse saveForm(FormAuditSchema formAuditSchema) throws JsonProcessingException
    {
        Map<String,Object> loggedInUserDetails =userDetails.getUserDetails().get(0);
        if (StringUtils.isEmpty(loggedInUserDetails.get(ID).toString()))
        {
            throw new UserDetailsIdNotFoundException(LOGGED_IN_USER_ID_NOT_FOUND,globalMessageSource.get(LOGGED_IN_USER_ID_NOT_FOUND,loggedInUserDetails.get(ID).toString()));
        }
        BigInteger loggedInUserId = BigInteger.valueOf(Long.parseLong(loggedInUserDetails.get(ID).toString()));
        FormAuditDefinition formAuditDefinition =this.objectMapper.convertValue(formAuditSchema,FormAuditDefinition.class);
        formAuditDefinition.setCreatedById(loggedInUserId);
        formAuditDefinition.setCreatedOn(Instant.now());
        FormAuditDefinition formAuditDefinitionResponse = formDefinitionAuditRepository.save(formAuditDefinition);
        return this.objectMapper.convertValue(formAuditDefinitionResponse, FormResponse.class);
    }

    @Override
    public Stream<FormAuditResponseSchema> getAllForms(String id, boolean includeProcessContent, Sort sort)
    {
        return this.formDefinitionAuditRepository.findAllById(BigInteger.valueOf(Long.parseLong(id)),sort )
                .map(this::convertEntityToDTO)
                .map(caseAuditSchema ->
                {
                    if (includeProcessContent)
                    {
                        return caseAuditSchema;
                    }
                    caseAuditSchema.setComponents(null);
                    return caseAuditSchema;
                });
    }

    @Override
    public PaginationResponsePayload getAllForms(String id, boolean includeProcessContent, Pageable pageable)
    {
        Page<FormAuditDefinition> formAuditDefinitionPage= this.formDefinitionAuditRepository.findAllById(BigInteger.valueOf(Long.parseLong(id)),pageable );
        List<Map<String,Object>> formAuditSchema=   formAuditDefinitionPage.stream().map(this::convertEntityToMap).collect(Collectors.toList());
        return tokenUtils.getPaginationResponsePayload(formAuditDefinitionPage,formAuditSchema);
    }

    @Override
    public FormAuditResponseSchema getFormsById(String id, Integer version)
    {
        FormAuditDefinition forms =
                this.formDefinitionAuditRepository.findById(BigInteger.valueOf(Long.parseLong(id)),version)
                        .orElseThrow(() -> new EntityIdNotFoundException(FORM_ID_NOT_FOUND,globalMessageSource.get(FORM_ID_NOT_FOUND,id)));
        return this.objectMapper.convertValue(forms, FormAuditResponseSchema.class);
    }

    private Map<String,Object> convertEntityToMap(FormAuditDefinition formAuditDefinition)
    {
        FormAuditResponseSchema formAuditSchema=convertEntityToDTO(formAuditDefinition);
        return this.objectMapper.convertValue(formAuditSchema,Map.class);
    }

    private FormAuditResponseSchema convertEntityToDTO(FormAuditDefinition formAuditDefinition)
    {
        return this.objectMapper.convertValue(formAuditDefinition, FormAuditResponseSchema.class);
    }
}
