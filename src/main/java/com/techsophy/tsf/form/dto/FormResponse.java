package com.techsophy.tsf.form.dto;

import lombok.Value;
import lombok.With;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@With
@Value
public class FormResponse
{
    @NotBlank String id;
    @NotNull Integer version;
}

