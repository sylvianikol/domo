package com.syn.domo.service.impl;

import com.syn.domo.repository.ChildRepository;
import com.syn.domo.service.ChildService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ChildServiceImpl(ChildRepository childRepository, ModelMapper modelMapper) {
        this.childRepository = childRepository;
        this.modelMapper = modelMapper;
    }
}
