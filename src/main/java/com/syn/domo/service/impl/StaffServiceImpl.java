package com.syn.domo.service.impl;

import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.repository.StaffRepository;
import com.syn.domo.service.StaffService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository, ModelMapper modelMapper) {
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StaffServiceModel add(StaffServiceModel staffServiceModel) {
        return null;
    }

    @Override
    public StaffServiceModel edit(StaffServiceModel staffServiceModel) {
        return null;
    }

    @Override
    public void delete(String staffId) {

    }
}
