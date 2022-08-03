package com.techsophy.tsf.form.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techsophy.tsf.form.dto.FormAuditResponseSchema;
import com.techsophy.tsf.form.dto.FormAuditSchema;
import com.techsophy.tsf.form.dto.FormResponse;
import com.techsophy.tsf.form.model.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;

@RequestMapping(BASE_URL+VERSION_V1+HISTORY)
@Validated
public interface FormAuditController
{
    @PostMapping(FORMS_URL)
    @PreAuthorize(CREATE_OR_ALL_ACCESS)
    ApiResponse<FormResponse> saveForm(FormAuditSchema formAuditSchema) throws JsonProcessingException;

    @GetMapping(FORM_BY_ID_URL)
    @PreAuthorize(READ_OR_ALL_ACCESS)
    ApiResponse<Stream<FormAuditResponseSchema>> getAllForms(@PathVariable(ID) String id,
                                                             @RequestParam(INCLUDE_CONTENT) boolean includeContent,
                                                             @RequestParam(value = PAGE, required = false) Integer page,
                                                             @RequestParam(value =SIZE, required = false) Integer pageSize,
                                                             @RequestParam(value = SORT_BY, required = false) String[] sortBy);

    @GetMapping(FORM_VERSION_BY_ID_URL)
    @PreAuthorize(READ_OR_ALL_ACCESS)
    ApiResponse<FormAuditResponseSchema> getFormsById(@PathVariable(ID) String id, @PathVariable(VERSION) Integer version);
}
