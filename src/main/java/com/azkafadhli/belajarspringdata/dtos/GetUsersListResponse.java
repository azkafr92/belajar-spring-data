package com.azkafadhli.belajarspringdata.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GetUsersListResponse implements Serializable {
    private String email;
    private String username;
}
