package com.techsophy.tsf.form.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techsophy.tsf.form.dto.FormAuditResponseSchema;
import com.techsophy.tsf.form.dto.FormAuditSchema;
import com.techsophy.tsf.form.dto.FormResponse;
import com.techsophy.tsf.form.dto.PaginationResponsePayload;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Stream;

public interface FormAuditService
{
    @Transactional(rollbackFor = Exception.class)
    FormResponse saveForm(FormAuditSchema formAuditSchema) throws JsonProcessingException;

    Stream<FormAuditResponseSchema> getAllForms(String id, boolean includeProcessContent, Sort sort);

    PaginationResponsePayload getAllForms(String id, boolean includeProcessContent, Pageable pageable);

    FormAuditResponseSchema getFormsById(String id, Integer version);
}
