package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ApartmentViewModel;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.service.ApartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, ModelMapper modelMapper) {
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApartmentServiceModel add(ApartmentServiceModel apartmentServiceModel) {

        if (this.apartmentRepository.findByNumber(apartmentServiceModel.getNumber()).isPresent()) {
            // TODO: Already exists
            return apartmentServiceModel;
        }

        Apartment apartment = this.modelMapper.map(apartmentServiceModel, Apartment.class);

        this.apartmentRepository.saveAndFlush(apartment);

        return this.modelMapper.map(apartment, ApartmentServiceModel.class);
    }

    @Override
    public Set<ApartmentServiceModel> getAllApartments() {
        return this.apartmentRepository.findAll().stream()
                .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public ApartmentServiceModel getByNumber(String apartmentNumber) {
        return this.apartmentRepository.findByNumber(apartmentNumber)
                .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                .orElse(null);

    }
}
