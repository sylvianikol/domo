package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ApartmentViewModel;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.service.ApartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Apartment apartment = this.modelMapper.map(apartmentServiceModel, Apartment.class);

        if (this.apartmentRepository.findByNumber(apartmentServiceModel.getNumber()) != null) {
            return apartmentServiceModel;
        }

        this.apartmentRepository.saveAndFlush(apartment);

        return this.modelMapper.map(apartment, ApartmentServiceModel.class);
    }

    @Override
    public Set<ApartmentServiceModel> getAllApartments() {
        return this.apartmentRepository.findAll().stream()
                .map(apartment -> {
                    ApartmentServiceModel apartmentServiceModel =
                            this.modelMapper.map(apartment, ApartmentServiceModel.class);
                    apartmentServiceModel.setResidents(apartment.getResidents().size());
                    return apartmentServiceModel;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Apartment getByNumber(String apartmentNumber) {
        return this.apartmentRepository.findByNumber(apartmentNumber);
    }
}
