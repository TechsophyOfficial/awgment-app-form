package com.techsophy.tsf.form.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techsophy.tsf.form.config.GlobalMessageSource;
import com.techsophy.tsf.form.controller.FormController;
import com.techsophy.tsf.form.dto.FormResponse;
import com.techsophy.tsf.form.dto.FormResponseSchema;
import com.techsophy.tsf.form.dto.FormSchema;
import com.techsophy.tsf.form.dto.PaginationResponsePayload;
import com.techsophy.tsf.form.model.ApiResponse;
import com.techsophy.tsf.form.service.FormService;
import com.techsophy.tsf.form.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;

@RestController
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class FormControllerImpl implements FormController
{
    private final FormService formService;
    private final GlobalMessageSource globalMessageSource;
    private final TokenUtils tokenUtils;

    @Override
    public ApiResponse<FormResponse> saveForm(FormSchema formSchema) throws JsonProcessingException
    {
        FormResponse formResponse= formService.saveForm(formSchema);
        if(StringUtils.equals(formSchema.getType(),COMPONENT))
        {
            return new ApiResponse<>(formResponse, true, globalMessageSource.get(SAVE_COMPONENT_SUCCESS));
        }
        return new ApiResponse<>(formResponse,true, globalMessageSource.get(SAVE_FORM_SUCCESS));
    }

    @Override
    public ApiResponse<FormResponseSchema> getFormById(String id)
    {
        FormResponseSchema formResponseSchema=formService.getFormById(id);
        if(StringUtils.equals(formResponseSchema.getType(),COMPONENT))
        {
            return new ApiResponse<>(formResponseSchema, true, globalMessageSource.get(GET_COMPONENT_SUCCESS));
        }
        return new ApiResponse<>(formResponseSchema, true, globalMessageSource.get(GET_FORM_SUCCESS));
    }

    @Override
    public ApiResponse getAllForms(boolean includeContent,String type,String deploymentIdList,String q,Integer page,Integer pageSize,String[] sortBy)
    {
        if (page == null)
        {
            Stream<FormResponseSchema> response=formService.getAllForms(includeContent,type, deploymentIdList,q,tokenUtils.getSortBy(sortBy));
            if(StringUtils.equals(type,COMPONENT))
            {
                return new ApiResponse<>(response, true, globalMessageSource.get(GET_COMPONENT_SUCCESS));
            }
            return new ApiResponse<>(response, true, globalMessageSource.get(GET_FORM_SUCCESS));
        }
        PaginationResponsePayload formPaginationResponse = formService.getAllForms(includeContent,type,q,tokenUtils.getPageRequest(page,pageSize,sortBy));
        if(StringUtils.equals(type,COMPONENT))
        {
            return new ApiResponse<>(formPaginationResponse, true, globalMessageSource.get(GET_COMPONENT_SUCCESS));
        }
        return new ApiResponse<>(formPaginationResponse, true, globalMessageSource.get(GET_FORM_SUCCESS));
    }

    @Override
    public ApiResponse<Void> deleteFormById(String id)
    {
        boolean response=formService.deleteFormById(id);
        if(response)
        {
            return new ApiResponse<>(null, true, globalMessageSource.get(DELETE_COMPONENT_SUCCESS));
        }
        return new ApiResponse<>(null, true, globalMessageSource.get(DELETE_FORM_SUCCESS));
    }

    @Override
    public ApiResponse<Stream<FormResponseSchema>> searchFormByIdOrNameLike(String idOrNameLike, String type) throws UnsupportedEncodingException
    {
        if(StringUtils.equals(type,COMPONENT))
        {
            return new ApiResponse<>(this.formService.searchFormByIdOrNameLike(idOrNameLike,type), true, globalMessageSource.get(GET_COMPONENT_SUCCESS));
        }
        return new ApiResponse<>(this.formService.searchFormByIdOrNameLike(idOrNameLike,type), true, globalMessageSource.get(GET_FORM_SUCCESS));
    }
}
