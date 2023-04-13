package com.azkafadhli.belajarspringdata.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class HelloDTO implements Serializable {

    String message;

}
