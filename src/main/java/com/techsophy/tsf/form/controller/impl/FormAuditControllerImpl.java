package com.techsophy.tsf.form.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techsophy.tsf.form.config.GlobalMessageSource;
import com.techsophy.tsf.form.controller.FormAuditController;
import com.techsophy.tsf.form.dto.FormAuditResponseSchema;
import com.techsophy.tsf.form.dto.FormAuditSchema;
import com.techsophy.tsf.form.dto.FormResponse;
import com.techsophy.tsf.form.model.ApiResponse;
import com.techsophy.tsf.form.service.FormAuditService;
import com.techsophy.tsf.form.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;

@RestController
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class FormAuditControllerImpl implements FormAuditController
{
    private final FormAuditService formAuditService;
    private final GlobalMessageSource globalMessageSource;
    private final TokenUtils tokenUtils;

    public ApiResponse<FormResponse> saveForm(FormAuditSchema formAuditSchema) throws JsonProcessingException
    {
        FormResponse formResponse=formAuditService.saveForm(formAuditSchema);
        if(StringUtils.equals(formAuditSchema.getType(),COMPONENT))
        {
            return new ApiResponse<>(formResponse, true, globalMessageSource.get(SAVE_COMPONENT_SUCCESS));
        }
        return new ApiResponse<>(formResponse, true, globalMessageSource.get(SAVE_FORM_SUCCESS));
    }

    public ApiResponse getAllForms(String id, boolean includeContent, Integer page, Integer pageSize, String[] sortBy)
    {
        if(page==null)
        {
            return new ApiResponse<>(formAuditService.getAllForms(id,includeContent,tokenUtils.getSortBy(sortBy) ), true, globalMessageSource.get(GET_FORM_SUCCESS));
        }
        return new ApiResponse<>(formAuditService.getAllForms(id,includeContent, tokenUtils.getPageRequest(page,pageSize,sortBy)), true, globalMessageSource.get(GET_FORM_SUCCESS));
    }

    public ApiResponse<FormAuditResponseSchema> getFormsById(String id, Integer version)
    {
        return new ApiResponse<>(formAuditService.getFormsById(id,version), true, globalMessageSource.get(GET_FORM_SUCCESS));
    }
}
