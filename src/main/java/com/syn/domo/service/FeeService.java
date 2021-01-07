package com.syn.domo.service;

import java.util.Map;

public interface FeeService {

    Map<String, Object> getAll(int page, int size, String[] sort);

    void generateMonthlyFees();
}
