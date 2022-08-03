package com.techsophy.tsf.form.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;

@With
@Value
@AllArgsConstructor
public class AccessControlListDTO
{
    String type;
    String value;
    boolean read;
    boolean create;
    boolean update;
    boolean delete;
    boolean all;

}
