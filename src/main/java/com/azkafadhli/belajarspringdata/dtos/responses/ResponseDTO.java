package com.azkafadhli.belajarspringdata.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO <T> implements Serializable {
    private final int code;
    private final String message;
    private T data;
    private PaginationDTO paginationDTO;

    public ResponseDTO(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseDTO(int code, String message, T data, PaginationDTO paginationDTO) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.paginationDTO = paginationDTO;
    }
}
