package com.azkafadhli.belajarspringdata.utils;

import com.azkafadhli.belajarspringdata.constants.Constant;
import com.azkafadhli.belajarspringdata.dtos.requests.PaginationAndSortingDTO;

public class PageAndSortMapper {
    public static void map(String inputPage, String inputLimit, String[] sort, PaginationAndSortingDTO output) {
        if (Validator.isInteger(inputPage)) {
            int pageNumber = Integer.parseInt(inputPage) - 1;
            output.setPage(Math.max(pageNumber, Integer.parseInt(Constant.DEFAULT_PAGE_NUMBER) - 1));
        } else {
            output.setPage(Integer.parseInt(Constant.DEFAULT_PAGE_NUMBER) - 1);
        }
        if (Validator.isInteger(inputLimit)) {
            int limitNumber = Integer.parseInt(inputLimit);

            output.setLimit(limitNumber < 1 ? Integer.parseInt(Constant.DEFAULT_PAGE_SIZE) : limitNumber);
        } else {
            output.setLimit(Integer.parseInt(Constant.DEFAULT_PAGE_SIZE));
        }
        if (sort == null || sort.length == 0) {
            output.setSort(new String[]{Constant.DEFAULT_SORT});
        } else {
            output.setSort(sort);
        }
    }
}
