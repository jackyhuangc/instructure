package com.jacky.strive.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KvOrderConfig {

    @JsonProperty("auto_pay")
    private boolean autoPay;

    @JsonProperty("auto_confirm")
    private boolean autoConfirm;

    @JsonProperty("auto_revoked")
    private boolean autoRevoked;
}
