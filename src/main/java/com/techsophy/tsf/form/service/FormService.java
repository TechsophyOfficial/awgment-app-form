package com.techsophy.tsf.form.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techsophy.tsf.form.dto.FormResponse;
import com.techsophy.tsf.form.dto.FormResponseSchema;
import com.techsophy.tsf.form.dto.FormSchema;
import com.techsophy.tsf.form.dto.PaginationResponsePayload;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;

public interface FormService
{
    FormResponse saveForm(FormSchema formSchema) throws JsonProcessingException;

    FormResponseSchema getFormById(String id);

    Stream<FormResponseSchema> getAllForms(boolean includeContent,String type, String deploymentIdList,String q, Sort sort);

    PaginationResponsePayload getAllForms(boolean includeContent, String type, String q, Pageable pageable);

    boolean deleteFormById(String id);

    Stream<FormResponseSchema> searchFormByIdOrNameLike(String idOrNameLike, String type) throws UnsupportedEncodingException;
}
