package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Floor;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.FloorServiceModel;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.FloorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final FloorService floorService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, FloorService floorService, ModelMapper modelMapper) {
        this.apartmentRepository = apartmentRepository;
        this.floorService = floorService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApartmentServiceModel add(ApartmentServiceModel apartmentServiceModel) {
        // TODO: validation

        int floorNumber = apartmentServiceModel.getFloorNumber();
        FloorServiceModel floorServiceModel = this.floorService.getByNumber(floorNumber);
        Apartment apartment = this.modelMapper.map(apartmentServiceModel, Apartment.class);

        apartment.setFloor(this.modelMapper.map(floorServiceModel, Floor.class));
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
