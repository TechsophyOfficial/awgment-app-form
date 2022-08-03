package com.techsophy.tsf.form.repository.impl;

import com.techsophy.tsf.form.entity.FormDefinition;
import com.techsophy.tsf.form.repository.FormDefinitionCustomRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;

@AllArgsConstructor
public class FormDefinitionCustomRepositoryImpl implements FormDefinitionCustomRepository
{
    private final MongoTemplate mongoTemplate;

    @Override
    public List<FormDefinition> findByNameOrId(String idOrNameLike)
    {
        Query query = new Query();
        String searchString = URLDecoder.decode(idOrNameLike, StandardCharsets.UTF_8);
        query.addCriteria(new Criteria().orOperator(Criteria.where(FORM_ID).regex(searchString), Criteria.where(FORM_NAME).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))));
        return mongoTemplate.find(query, FormDefinition.class);
    }

    @Override
    public Page<FormDefinition> findByTypePagination(String type, Pageable pageable)
    {
        Query query = new Query();
        Query countQuery = new Query();
        String typeString = URLDecoder.decode(type, StandardCharsets.UTF_8);
        query.addCriteria(Criteria.where(TYPE).regex(Pattern.compile
                (typeString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))).with(pageable);
        List<FormDefinition> formDefinitions = mongoTemplate.find(query, FormDefinition.class);
        query.with(Sort.by(Sort.Direction.ASC, FORM_NAME));
        countQuery.addCriteria(Criteria.where(TYPE).regex(Pattern.compile
                (typeString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
        long count=mongoTemplate.count(countQuery,FormDefinition.class);
        return new PageImpl<>(formDefinitions, pageable,count );
    }

    @Override
    public Page<FormDefinition> findByTypeAndQPagination(String type,String q, Pageable pageable)
    {
        Query query = new Query();
        Query countQuery = new Query();
        String typeString = URLDecoder.decode(type, StandardCharsets.UTF_8);
        query.addCriteria(Criteria.where(TYPE).regex(Pattern.compile
                (typeString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))).with(pageable);
        String searchString = URLDecoder.decode(q, StandardCharsets.UTF_8);
        query.addCriteria(new Criteria().orOperator(Criteria.where(ID).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(FORM_NAME).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(VERSION).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(TYPE).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(COMPONENTS).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
        ));
        List<FormDefinition> formDefinitions = mongoTemplate.find(query, FormDefinition.class);
        query.with(Sort.by(Sort.Direction.ASC, FORM_NAME));
        countQuery.addCriteria(Criteria.where(TYPE).regex(Pattern.compile
                (typeString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
        long count=mongoTemplate.count(countQuery,FormDefinition.class);
        return new PageImpl<>(formDefinitions, pageable,count );
    }

    @Override
    public List<FormDefinition> findByTypeAndQSorting(String type, String q, Sort sort)
    {
        Query query = new Query();
        String typeString = URLDecoder.decode(type, StandardCharsets.UTF_8);
        query.addCriteria(Criteria.where(TYPE).regex(Pattern.compile
                (typeString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))).with(sort);
        String searchString = URLDecoder.decode(q, StandardCharsets.UTF_8);
        query.addCriteria(new Criteria().orOperator(Criteria.where(ID).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(FORM_NAME).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(VERSION).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(TYPE).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(COMPONENTS).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
        ));
        query.with(Sort.by(Sort.Direction.ASC, FORM_NAME));
        return mongoTemplate.find(query, FormDefinition.class);
    }

    @Override
    public List<FormDefinition> findByTypeSorting(String type, Sort sort)
    {
        Query query = new Query();
        String typeString = URLDecoder.decode(type, StandardCharsets.UTF_8);
        query.addCriteria(Criteria.where(TYPE).regex(Pattern.compile
                (typeString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))).with(sort);
        return mongoTemplate.find(query, FormDefinition.class);
    }

    @Override
    public Stream<FormDefinition> findFormsByQSorting(String q, Sort sort)
    {
        Query query = new Query();
        String searchString = URLDecoder.decode(q, StandardCharsets.UTF_8);
        query.addCriteria(new Criteria().orOperator(Criteria.where(ID).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(FORM_NAME).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(VERSION).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(TYPE).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(COMPONENTS).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
        ));
        query.with(Sort.by(Sort.Direction.ASC, FORM_NAME));
        return mongoTemplate.find(query, FormDefinition.class).stream();
    }

    @Override
    public Page<FormDefinition> findFormsByQPageable(String q, Pageable pageable)
    {
        Query query = new Query();
        Query countQuery = new Query();
        String searchString = URLDecoder.decode(q, StandardCharsets.UTF_8);
        query.addCriteria(new Criteria().orOperator(Criteria.where(ID).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(FORM_NAME).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(VERSION).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(TYPE).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(COMPONENTS).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
        )).with(pageable);
        List<FormDefinition> formDefinitions = mongoTemplate.find(query, FormDefinition.class);
        query.with(Sort.by(Sort.Direction.ASC, FORM_NAME));
        countQuery.addCriteria(new Criteria().orOperator(Criteria.where(FORM_ID).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(FORM_NAME).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(VERSION).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(TYPE).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
                Criteria.where(COMPONENTS).regex(Pattern.compile(searchString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
        ));
        long count=mongoTemplate.count(countQuery,FormDefinition.class);
        return new PageImpl<>(formDefinitions, pageable,count );
    }

    @Override
    public List<FormDefinition> findByIdIn(List<String> idList)
    {
        Query query = new Query(Criteria.where(FORM_ID).in(idList));
        return mongoTemplate.find(query, FormDefinition.class);
    }

    @Override
    public List<FormDefinition> findByNameOrIdAndType(String idOrNameLike, String type)
    {
        Query query = new Query();
        String searchString1 = URLDecoder.decode(idOrNameLike, StandardCharsets.UTF_8);
        String searchString2 = URLDecoder.decode(type, StandardCharsets.UTF_8);
        query.addCriteria(new Criteria().andOperator(new Criteria().orOperator(
                        Criteria.where(FORM_ID).regex(searchString1),
                        Criteria.where(FORM_NAME).regex(Pattern.compile
                                (searchString1, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))),
                Criteria.where(TYPE).regex(Pattern.compile
                        (searchString2, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
        ));
        query.with(Sort.by(Sort.Direction.ASC, FORM_NAME));
        return mongoTemplate.find(query, FormDefinition.class);
    }
}
