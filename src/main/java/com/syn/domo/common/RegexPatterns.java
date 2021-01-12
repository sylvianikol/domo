package com.syn.domo.common;

public class RegexPatterns {

    public static final String APARTMENT_NUMBER_REGEX =
            "[0-9]+[0-9a-zA-Z]{0,10}";

    public static final String PHONE_REGEX =
            "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[\\s\\./0-9]*$";
}
