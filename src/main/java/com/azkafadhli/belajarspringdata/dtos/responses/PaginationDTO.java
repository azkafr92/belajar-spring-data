package com.azkafadhli.belajarspringdata.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.io.Serializable;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaginationDTO implements Serializable {
    private final int currentPage;
    private final int totalPages;
    private final long totalItems;

    public <T> PaginationDTO(Page<T> page) {
        this.currentPage = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
    }
}
