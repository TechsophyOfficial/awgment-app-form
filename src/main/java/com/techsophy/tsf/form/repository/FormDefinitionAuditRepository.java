package com.techsophy.tsf.form.repository;

import com.techsophy.tsf.form.entity.FormAuditDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.math.BigInteger;
import java.util.Optional;
import java.util.stream.Stream;
import static com.techsophy.tsf.form.constants.FormModelerConstants.FIND_ALL_BY_ID_QUERY;
import static com.techsophy.tsf.form.constants.FormModelerConstants.FIND_BY_ID_QUERY;

public interface FormDefinitionAuditRepository extends MongoRepository<FormAuditDefinition, Long>
{
    @Query(FIND_BY_ID_QUERY)
    Optional<FormAuditDefinition> findById(BigInteger id, Integer version);

    @Query(FIND_ALL_BY_ID_QUERY)
    Stream<FormAuditDefinition> findAllById(BigInteger id, Sort sort);

    @Query(FIND_ALL_BY_ID_QUERY)
    Page<FormAuditDefinition> findAllById(BigInteger id, Pageable pageable);
}
