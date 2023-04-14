package com.azkafadhli.belajarspringdata.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViolationDTO implements Serializable {
    private String field;
    private String message;
    public ViolationDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }
    public ViolationDTO(String message) {
        this.message = message;
    }
}
