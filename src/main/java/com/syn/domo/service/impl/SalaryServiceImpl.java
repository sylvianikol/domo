package com.syn.domo.service.impl;

import com.syn.domo.repository.SalaryRepository;
import com.syn.domo.service.SalaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SalaryServiceImpl(SalaryRepository salaryRepository, ModelMapper modelMapper) {
        this.salaryRepository = salaryRepository;
        this.modelMapper = modelMapper;
    }
}
