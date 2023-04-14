package com.azkafadhli.belajarspringdata.exceptions;

import com.azkafadhli.belajarspringdata.dtos.responses.ValidationErrorDTO;
import com.azkafadhli.belajarspringdata.dtos.responses.ViolationDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDTO onConstraintValidationException(ConstraintViolationException e) {
        ValidationErrorDTO errors = new ValidationErrorDTO();
        errors.setViolations(
                e.getConstraintViolations()
                        .stream()
                        .map(violation -> new ViolationDTO(violation.getPropertyPath().toString(), violation.getMessage()))
                        .collect(Collectors.toList())
        );
        return errors;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ValidationErrorDTO onDataIntegrityViolationException(DataIntegrityViolationException e) {
        ValidationErrorDTO errors = new ValidationErrorDTO();
        ViolationDTO violationDTO = new ViolationDTO(e.getMessage());
        errors.getViolations().add(violationDTO);
        return errors;
    }
}
