package com.techsophy.tsf.form.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigInteger;
import java.util.Map;
import static com.techsophy.tsf.form.constants.FormModelerConstants.TP_FORM_DEFINITION_AUDIT_COLLECTION;

@EqualsAndHashCode(callSuper = true)
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = TP_FORM_DEFINITION_AUDIT_COLLECTION)
public class FormAuditDefinition extends Auditable
{
    private static final long serialVersionUID = 1L;
    @Id
    private BigInteger id;
    private BigInteger formId;
    private String name;
    private Integer version;
    private Map<String,Object> components;
    private Map<String,Object> properties;
    private String type;
    private Boolean isDefault;
    private Boolean pushToElastic;
}
