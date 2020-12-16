package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Floor;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.FloorServiceModel;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FloorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final BuildingService buildingService;
    private final FloorService floorService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, BuildingService buildingService, FloorService floorService, ModelMapper modelMapper) {
        this.apartmentRepository = apartmentRepository;
        this.buildingService = buildingService;
        this.floorService = floorService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApartmentServiceModel add(ApartmentServiceModel apartmentServiceModel, String buildingId) {
        // TODO: validation

        int floorNumber = apartmentServiceModel.getFloorNumber();
        FloorServiceModel floorServiceModel = this.floorService.getByNumber(floorNumber);
        BuildingServiceModel buildingServiceModel = this.buildingService.getById(buildingId);

        Apartment apartment = this.modelMapper.map(apartmentServiceModel, Apartment.class);

        apartment.setFloor(this.modelMapper.map(floorServiceModel, Floor.class));
        apartment.setBuilding(this.modelMapper.map(buildingServiceModel, Building.class));

        this.apartmentRepository.saveAndFlush(apartment);

        return this.modelMapper.map(apartment, ApartmentServiceModel.class);
    }

    @Override
    public Set<ApartmentServiceModel> getAllApartmentsByBuildingId(String buildingId) {
        Set<ApartmentServiceModel> apartmentServiceModels =
                this.apartmentRepository.findAllByBuildingIdOrderByNumber(buildingId).stream()
                .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(apartmentServiceModels);
    }

    @Override
    public boolean alreadyExists(String apartmentNumber, String buildingId) {
        return this.apartmentRepository.findByNumberAndBuildingId(apartmentNumber, buildingId).isPresent();

    }

    @Override
    public ApartmentServiceModel getByNumberAndBuildingId(String apartmentNumber, String buildingId) {
        return this.apartmentRepository.findByNumberAndBuildingId(apartmentNumber, buildingId)
                .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                .orElse(null);

    }
}
