package com.techsophy.tsf.form.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techsophy.tsf.form.service.impl.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.techsophy.tsf.form.constants.FormModelerConstants.*;

@Data
public class FormResponseSchema {
    String id;
    @NotBlank(message = NAME_NOT_BLANK)
    @Pattern(regexp = REGEX_CONSTANT)
    String name;
    Map<String, Object> components;
    List<AccessControlListDTO> acls;
    Map<String, Object> properties;
    String type;
    Integer version;
    Boolean isDefault;
    String createdById;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN, timezone = TIME_ZONE)
    Instant createdOn;
    String updatedById;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN, timezone = TIME_ZONE)
    Instant updatedOn;
    private Status elasticPush = Status.DISABLED;

}
