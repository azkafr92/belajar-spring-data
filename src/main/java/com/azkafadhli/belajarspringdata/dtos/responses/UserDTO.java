package com.azkafadhli.belajarspringdata.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDTO implements Serializable {
    private String id;
    private String email;
    private String username;
}
