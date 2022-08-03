package com.techsophy.tsf.form.repository;

import com.techsophy.tsf.form.entity.FormDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Stream;

public interface FormDefinitionCustomRepository
{
    List<FormDefinition> findByNameOrId(String idOrNameLike) throws UnsupportedEncodingException;
    Page<FormDefinition> findByTypePagination(String type,Pageable pageable);
    Page<FormDefinition> findByTypeAndQPagination(String type,String q,Pageable pageable);
    List<FormDefinition> findByTypeAndQSorting(String type,String q,Sort sort);
    List<FormDefinition> findByTypeSorting(String type,Sort sort);
    Stream<FormDefinition> findFormsByQSorting(String q, Sort sort);
    Page<FormDefinition> findFormsByQPageable(String q, Pageable pageable);
    List<FormDefinition> findByIdIn(List<String> idList);
    List<FormDefinition> findByNameOrIdAndType(String idOrNameLike,String type);
}
