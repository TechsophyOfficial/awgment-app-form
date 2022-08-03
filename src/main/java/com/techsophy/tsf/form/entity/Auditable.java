package com.techsophy.tsf.form.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import java.math.BigInteger;
import java.time.Instant;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class Auditable
{
    private BigInteger createdById;
    private BigInteger updatedById;
    private Instant createdOn;
    private Instant updatedOn;
    private String createdByName;
    private String updatedByName;
}

