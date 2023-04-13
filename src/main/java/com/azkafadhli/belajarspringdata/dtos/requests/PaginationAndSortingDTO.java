package com.azkafadhli.belajarspringdata.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaginationAndSortingDTO implements Serializable {
    private int page;
    private int limit;
    private String[] sort;
}
