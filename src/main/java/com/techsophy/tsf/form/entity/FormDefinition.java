package com.techsophy.tsf.form.entity;

import com.techsophy.tsf.form.dto.AccessControlListDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import static com.techsophy.tsf.form.constants.FormModelerConstants.TP_FORM_DEFINITION_COLLECTION;

@EqualsAndHashCode(callSuper = true)
@With
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = TP_FORM_DEFINITION_COLLECTION)
public class FormDefinition extends Auditable
{
    private static final long serialVersionUID = 1L;
    @Id
    private BigInteger id;
    private String name;
    private Integer version;
    private Map<String,Object> components;
    private  List<AccessControlListDTO> acls;
    private Map<String,Object> properties;
    private String type;
    private Boolean isDefault;
    private Boolean pushToElastic;
}
