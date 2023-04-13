package com.azkafadhli.belajarspringdata.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUsersDTO extends PaginationAndSortingDTO {
    private String email;
    private String username;
}
