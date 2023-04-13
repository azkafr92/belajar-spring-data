package com.azkafadhli.belajarspringdata.utils;

import com.azkafadhli.belajarspringdata.constants.Constant;
import com.azkafadhli.belajarspringdata.dtos.requests.PaginationAndSortingDTO;

public class PageAndSortMapper {
    public static void map(String inputPage, String inputLimit, String[] sort, PaginationAndSortingDTO output) {
        if (Validator.isInteger(inputPage)) {
            output.setPage(Integer.parseInt(inputPage) - 1);
        } else {
            output.setPage(Integer.parseInt(Constant.DEFAULT_PAGE_NUMBER) - 1);
        }
        if (Validator.isInteger(inputLimit)) {
            output.setLimit(Integer.parseInt(inputLimit));
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
