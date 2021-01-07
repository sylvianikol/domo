package com.syn.domo.service;

import com.syn.domo.model.service.FeeServiceModel;

import java.util.List;

public interface FeeService {

    List<FeeServiceModel> getAll();

    void generateMonthlyFees();
}
