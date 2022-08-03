package com.techsophy.tsf.form.dto;

import lombok.Value;
import lombok.With;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@With
@Value
public class FormResponse
{
    @NotBlank String id;
    @NotNull Integer version;
}

