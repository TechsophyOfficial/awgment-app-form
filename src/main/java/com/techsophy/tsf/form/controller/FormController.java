package com.techsophy.tsf.form.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techsophy.tsf.form.dto.FormResponse;
import com.techsophy.tsf.form.dto.FormResponseSchema;
import com.techsophy.tsf.form.dto.FormSchema;
import com.techsophy.tsf.form.model.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;

@RequestMapping(BASE_URL+ VERSION_V1)
public interface FormController
{
    @PostMapping(FORMS_URL)
    @PreAuthorize(CREATE_OR_ALL_ACCESS)
    ApiResponse<FormResponse> saveForm(@RequestBody @Validated FormSchema formSchema) throws JsonProcessingException;

    @GetMapping(FORM_BY_ID_URL)
    @PreAuthorize(READ_OR_ALL_ACCESS)
    ApiResponse<FormResponseSchema> getFormById(@PathVariable(ID) String id);

    @GetMapping(FORMS_URL)
    @PreAuthorize(READ_OR_ALL_ACCESS)
    ApiResponse<Stream<FormResponseSchema>> getAllForms(@RequestParam(INCLUDE_CONTENT) boolean includeContent,
                                                @RequestParam(value = TYPE, required = false) String type,
                                                @RequestParam(value = DEPLOYMENT, required = false) String deploymentIdList,
                                                @RequestParam(value = QUERY,required = false) String q,
                                                @RequestParam(value = PAGE, required = false) Integer page,
                                                @RequestParam(value =SIZE, required = false) Integer pageSize,
                                                @RequestParam(value = SORT_BY, required = false) String[] sortBy);

    @DeleteMapping(FORM_BY_ID_URL)
    @PreAuthorize(DELETE_OR_ALL_ACCESS)
    ApiResponse<Void> deleteFormById(@PathVariable(ID) String id);

    @GetMapping(SEARCH_FORM_URL)
    @PreAuthorize(READ_OR_ALL_ACCESS)
    ApiResponse<Stream<FormResponseSchema>> searchFormByIdOrNameLike(@RequestParam(value = ID_OR_NAME_LIKE) String idOrNameLike, @RequestParam(value = TYPE, required = false) String type) throws UnsupportedEncodingException;
}
