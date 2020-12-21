package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Floor;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.FloorServiceModel;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final BuildingService buildingService;
    private final FloorService floorService;
    private final ResidentService residentService;
    private final ChildService childService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository,
                                @Lazy BuildingService buildingService,
                                FloorService floorService,
                                @Lazy ResidentService residentService,
                                @Lazy ChildService childService,
                                ModelMapper modelMapper) {
        this.apartmentRepository = apartmentRepository;
        this.buildingService = buildingService;
        this.floorService = floorService;
        this.residentService = residentService;
        this.childService = childService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApartmentServiceModel add(ApartmentServiceModel apartmentServiceModel, String buildingId) {
        // TODO: validation

        int floorNumber = apartmentServiceModel.getFloorNumber();
        FloorServiceModel floorServiceModel =
                this.floorService.getByNumberAndBuildingId(floorNumber, buildingId);
        BuildingServiceModel buildingServiceModel = this.buildingService.getById(buildingId);

        Apartment apartment = this.modelMapper.map(apartmentServiceModel, Apartment.class);
        apartment.setAddedOn(LocalDate.now());
        apartment.setFloor(this.modelMapper.map(floorServiceModel, Floor.class));
        apartment.setBuilding(this.modelMapper.map(buildingServiceModel, Building.class));

        this.apartmentRepository.saveAndFlush(apartment);

        return this.modelMapper.map(apartment, ApartmentServiceModel.class);
    }

    @Override
    @Transactional
    public void deleteAllByBuildingId(String buildingId) {
        Set<Apartment> apartments =
                this.apartmentRepository.findAllByBuilding_IdOrderByNumber(buildingId);

        for (Apartment apartment : apartments) {
            this.residentService.deleteAllByApartmentId(apartment.getId());
        }

        this.apartmentRepository.deleteAll(apartments);
    }

    @Override
    public Set<ApartmentServiceModel> getAllApartmentsByBuildingId(String buildingId) {
        Set<ApartmentServiceModel> apartmentServiceModels =
                this.apartmentRepository.findAllByBuilding_IdOrderByNumber(buildingId).stream()
                .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(apartmentServiceModels);
    }

    @Override
    public Set<String> getAllApartmentNumbersByBuildingId(String buildingId) {
        Set<String> apartmentNumbers = this.getAllApartmentsByBuildingId(buildingId).stream()
                .map(ApartmentServiceModel::getNumber)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(apartmentNumbers);
    }

    @Override
    public boolean alreadyExists(String apartmentNumber, String buildingId) {
        return this.apartmentRepository.findByNumberAndBuilding_Id(apartmentNumber, buildingId).isPresent();

    }

    @Override
    public ApartmentServiceModel getByNumberAndBuildingId(String apartmentNumber, String buildingId) {
        // TODO: ApartmentNotFoundException
        return this.apartmentRepository.findByNumberAndBuilding_Id(apartmentNumber, buildingId)
                .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Apartment not found");
                });
    }

    @Override
    public ApartmentServiceModel getById(String apartmentId) {
        // TODO: ApartmentNotFoundException
        return this.apartmentRepository.findById(apartmentId)
                .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Apartment not found");
                });
    }

    @Override
    public boolean hasResidents(String apartmentId) {
        return this.getById(apartmentId).getResidents().size() > 0;
    }

    @Override
    public void archiveAllByBuildingId(String buildingId) {
        this.apartmentRepository.findAllByBuilding_IdOrderByNumber(buildingId)
                .forEach(apartment -> {
                    apartment.setArchivedOn(LocalDate.now());
                    this.apartmentRepository.saveAndFlush(apartment);
                    this.residentService.archiveAllByApartmentId(apartment.getId());
                    this.childService.removeAllByApartmentId(apartment.getId());
                });
    }


}
