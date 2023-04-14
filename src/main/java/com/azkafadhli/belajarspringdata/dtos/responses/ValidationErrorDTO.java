package com.azkafadhli.belajarspringdata.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationErrorDTO extends ResponseDTO {
    private List<ViolationDTO> violations;
    public ValidationErrorDTO()  {
        super(HttpStatus.BAD_REQUEST.value(), "failed");
        this.violations = new ArrayList<>();
    }
}
