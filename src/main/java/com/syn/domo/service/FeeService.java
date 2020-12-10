package com.syn.domo.service;

import com.syn.domo.model.binding.FeeAddBindingModel;
import com.syn.domo.model.service.FeeServiceModel;
import com.syn.domo.model.view.FeeViewModel;

import java.util.List;

public interface FeeService {
    List<FeeServiceModel> generateMonthlyFees(FeeAddBindingModel feeAddBindingModel);
}
