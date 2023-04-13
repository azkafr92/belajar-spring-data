package com.azkafadhli.belajarspringdata.dtos.responses;

import lombok.Getter;

import java.io.Serializable;


@Getter
public class ResponseDTO <T> implements Serializable {
    private final int code;
    private final String message;
    private final  T data;
    private final PaginationDTO paginationDTO;

    public ResponseDTO(int code, String message, T data, PaginationDTO paginationDTO) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.paginationDTO = paginationDTO;
    }
}
