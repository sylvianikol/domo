package com.syn.domo.utils;

import java.util.Arrays;

import static com.syn.domo.common.DefaultParamValues.NULL;

public class UrlCheckerUtilImpl implements UrlCheckerUtil {

    public boolean areEmpty(String ...urls) {
        return Arrays.stream(urls).filter(url -> !url.equals(NULL)).findFirst().isEmpty();
    }
}
