package com.azkafadhli.belajarspringdata.dtos.responses;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UserListDTO extends ResponseDTO{
    public UserListDTO(List<UserDTO> users, PaginationDTO paginationDTO) {
        super(HttpStatus.OK.value(), "success", users, paginationDTO);
    }
}
