package com.azkafadhli.belajarspringdata.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RegisterUserDTO implements Serializable {

    @JsonProperty(value = "email", required = true)
    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    @JsonProperty(value = "username", required = true)
    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "[a-zA-Z0-9_]*", message = "username can only contains alphabet, number, and underscore")
    private String username;

    @JsonProperty(required = true)
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$",
            message = "password must have at least one digit, one lowercase, one uppercase, one special character, and minimum 8 characters")
    private String password;

    @JsonProperty(value = "is_enabled")
    private Boolean isEnabled = false;

}
