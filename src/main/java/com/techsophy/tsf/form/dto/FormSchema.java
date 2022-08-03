package com.techsophy.tsf.form.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;
import static com.techsophy.tsf.form.constants.FormModelerConstants.NAME_NOT_BLANK;
import static com.techsophy.tsf.form.constants.FormModelerConstants.REGEX_CONSTANT;

@With
@Value
@AllArgsConstructor
public class FormSchema
{
    String id;
    @NotBlank(message = NAME_NOT_BLANK)
    @Pattern(regexp = REGEX_CONSTANT) String name;
    Map<String,Object> components;
    List<AccessControlListDTO> acls;
    Map<String,Object> properties;
    String type;
    Integer version;
    Boolean isDefault;
}
