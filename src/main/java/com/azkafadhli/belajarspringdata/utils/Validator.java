package com.azkafadhli.belajarspringdata.utils;

public class Validator {

    public static Boolean isStringEmptyNullBlank(String s) {
        return (s == null || s.isEmpty() || s.isBlank());
    }

    public static Boolean isInteger(String s) {
        if (Validator.isStringEmptyNullBlank(s)) {
            return true;
        }
        return s.matches("[0-9]+");
    }

}
