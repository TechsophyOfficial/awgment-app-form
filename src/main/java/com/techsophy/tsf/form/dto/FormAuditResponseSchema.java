package com.techsophy.tsf.form.dto;

import com.techsophy.tsf.form.service.impl.Status;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;

@Data
public class FormAuditResponseSchema
{
    String id;
    @NotBlank(message = FORM_ID_NOT_BLANK)
    String formId;
    @NotBlank(message =NAME_NOT_BLANK )
    @Pattern(regexp = REGEX_CONSTANT) String name;
    Map<String,Object> components;
    List<AccessControlListDTO> acls;
    Map<String,Object> properties;
    String type;
    Integer version;
    Boolean isDefault;
    String createdById;
    Instant createdOn;
    String updatedById;
    Instant updatedOn;
    Status elasticPush = Status.DISABLED;

}
