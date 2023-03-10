package com.techsophy.tsf.form.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("enabled")
    ENABLED,
    @JsonProperty("disabled")
    DISABLED;
}
