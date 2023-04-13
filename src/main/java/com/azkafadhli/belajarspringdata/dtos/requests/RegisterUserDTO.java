package com.azkafadhli.belajarspringdata.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RegisterUserDTO implements Serializable {

    @JsonProperty(value = "email", required = true)
    private String email;

    @JsonProperty(value = "username", required = true)
    private String username;

    @JsonProperty(required = true)
    private String password;

    @JsonProperty(value = "is_enabled")
    private Boolean isEnabled = false;

}
