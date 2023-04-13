package com.azkafadhli.belajarspringdata.utils;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


// reference: https://www.bezkoder.com/spring-data-sort-multiple-columns/
public class Sorter {
    private static Sort.Direction getSortDirection(String direction) {
        if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
    public static List<Sort.Order> getSortOrder(String[] sorts) {
        List<Sort.Order> sortOrder = new ArrayList<>();
        if (sorts[0].contains(",")) {
            for (String sort: sorts) {
                String[] _sort = sort.split(",");
                Sort.Order order = new Sort.Order(Sorter.getSortDirection(_sort[1]), _sort[0]);
                sortOrder.add(order);
            }
        } else {
            sortOrder.add(new Sort.Order(getSortDirection(sorts[1]), sorts[0]));
        }
        return sortOrder;
    }
}
