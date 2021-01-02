package com.syn.domo.service.impl;

import com.syn.domo.exception.*;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
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
    private final ResidentService residentService;
    private final ChildService childService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository,
                                @Lazy BuildingService buildingService,
                                @Lazy ResidentService residentService,
                                @Lazy ChildService childService, ModelMapper modelMapper) {
        this.apartmentRepository = apartmentRepository;
        this.buildingService = buildingService;
        this.residentService = residentService;
        this.childService = childService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApartmentServiceModel add(ApartmentServiceModel apartmentServiceModel, String buildingId) {
        // TODO: validation

        Optional<BuildingServiceModel> buildingOpt =
                this.buildingService.getById(buildingId);

        if (buildingOpt.isEmpty()) {
            throw new BuildingNotFoundException("Building does not exists!");
        }

        String apartmentNumber = apartmentServiceModel.getNumber();
        if (this.alreadyExists(apartmentNumber, buildingId)) {
            throw new ApartmentAlreadyExistsException(
                    String.format("Apartment No.%s already exists in this building!", apartmentNumber));
        }

        if (apartmentServiceModel.getFloor() > buildingOpt.get().getFloors()) {
            throw new UnprocessableEntityException("Invalid floor number!");
        }

        Apartment apartment = this.modelMapper.map(apartmentServiceModel, Apartment.class);
        apartment.setAddedOn(LocalDate.now());
        apartment.setBuilding(this.modelMapper.map(buildingOpt.get(), Building.class));

        this.apartmentRepository.saveAndFlush(apartment);

        return this.modelMapper.map(apartment, ApartmentServiceModel.class);
    }

    @Override
    public ApartmentServiceModel edit(ApartmentServiceModel apartmentServiceModel, String buildingId) {
        // TODO: validation

        Optional<BuildingServiceModel> buildingOpt = this.buildingService.getById(buildingId);
        if (buildingOpt.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        if (apartmentServiceModel.getFloor() > buildingOpt.get().getFloors()) {
            throw new UnprocessableEntityException("Invalid floor number!");
        }

        Apartment apartment =
                this.apartmentRepository.findById(apartmentServiceModel.getId()).orElse(null);

        if (apartment == null || !apartment.getBuilding().getId().equals(buildingId)) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        String newNumber = apartmentServiceModel.getNumber();
        Optional<Apartment> existingApartment = this.apartmentRepository
                .findByNumberAndBuilding_Id(newNumber, buildingId);

        if (!apartment.getNumber().equals(newNumber) && existingApartment.isPresent()) {
            throw new ApartmentAlreadyExistsException(
                    String.format("Apartment No:%s already exists in this building!",
                            apartmentServiceModel.getNumber()));
        }

        apartment.setNumber(apartmentServiceModel.getNumber());
        apartment.setFloor(apartmentServiceModel.getFloor());
        apartment.setPets(apartmentServiceModel.getPets());
        apartment.setAddedOn(apartmentServiceModel.getAddedOn());

        this.apartmentRepository.saveAndFlush(apartment);
        return this.modelMapper.map(apartment, ApartmentServiceModel.class);
    }

    @Override
    public void delete(String apartmentId, String buildingId) {

        if (this.buildingService.getById(buildingId).isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Apartment apartment =
                this.apartmentRepository.findById(apartmentId).orElse(null);

        if (apartment == null || !apartment.getBuilding().getId().equals(buildingId)) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        this.residentService.deleteAllByApartmentId(buildingId, apartment.getId());
        this.childService.deleteAllByApartmentId(buildingId, apartment.getId());
        this.apartmentRepository.delete(apartment);
    }

    @Override
    @Transactional
    public void deleteAllByBuildingId(String buildingId) {
        Set<Apartment> apartments =
                this.apartmentRepository.findAllByBuilding_IdOrderByNumber(buildingId);

        for (Apartment apartment : apartments) {
            this.residentService.deleteAllByApartmentId(buildingId, apartment.getId());
        }

        this.apartmentRepository.deleteAll(apartments);
    }

    @Override
    public Set<ApartmentServiceModel> getAllByBuildingId(String buildingId) {
        Set<ApartmentServiceModel> apartmentServiceModels =
                this.apartmentRepository.findAllByBuilding_IdOrderByNumber(buildingId).stream()
                .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(apartmentServiceModels);
    }

    @Override
    public ApartmentServiceModel getByNumberAndBuildingId(String apartmentNumber, String buildingId) {
        return this.apartmentRepository.findByNumberAndBuilding_Id(apartmentNumber, buildingId)
                .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Apartment not found");
                });
    }

    @Override
    public Optional<ApartmentServiceModel> getById(String apartmentId) {
        Optional<Apartment> apartment = this.apartmentRepository.findById(apartmentId);
        return apartment.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(apartment.get(), ApartmentServiceModel.class));
    }

    private boolean alreadyExists(String apartmentNumber, String buildingId) {
        return this.apartmentRepository
                .findByNumberAndBuilding_Id(apartmentNumber, buildingId).isPresent();

    }
}
