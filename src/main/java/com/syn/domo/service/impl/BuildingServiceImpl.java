package com.syn.domo.service.impl;

import com.syn.domo.exception.*;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final ApartmentService apartmentService;
    private final ResidentService residentService;
    private final StaffService staffService;
    private final ModelMapper modelMapper;

    @Autowired
    public BuildingServiceImpl(BuildingRepository buildingRepository,
                               ApartmentService apartmentService,
                               @Lazy ResidentService residentService,
                               @Lazy StaffService staffService,
                               ModelMapper modelMapper) {
        this.buildingRepository = buildingRepository;
        this.apartmentService = apartmentService;
        this.residentService = residentService;
        this.staffService = staffService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<BuildingServiceModel> getAll() {

        Set<BuildingServiceModel> buildingServiceModels =
                this.buildingRepository.findAll().stream()
                        .map(building -> this.modelMapper.map(building, BuildingServiceModel.class))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(buildingServiceModels);
    }

    @Override
    public Optional<BuildingServiceModel> get(String id) {
        Optional<Building> building = this.buildingRepository.findById(id);
        return building.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(building.get(), BuildingServiceModel.class));
    }

    @Override
    public BuildingServiceModel add(BuildingServiceModel buildingServiceModel) {
        // TODO: validation

        Optional<Building> duplicateBuilding = this.buildingRepository
                .findByNameAndAddressAndNeighbourhood(
                buildingServiceModel.getName().trim(),
                buildingServiceModel.getAddress().trim(),
                buildingServiceModel.getNeighbourhood().trim());

        if (duplicateBuilding.isPresent()) {
            throw new EntityExistsException(String.format(
                    "Building with name \"%s\" already exists in \"%s\"!",
                    buildingServiceModel.getName(), buildingServiceModel.getNeighbourhood()));
        }

        Building building = this.modelMapper.map(buildingServiceModel, Building.class);
        building.setAddedOn(LocalDate.now());
        building.setApartments(new LinkedHashSet<>());
        this.buildingRepository.saveAndFlush(building);

        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public BuildingServiceModel edit(BuildingServiceModel buildingServiceModel, String buildingId) {
        // TODO: validation

        Optional<Building> building = this.buildingRepository.findById(buildingId);

        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        if (this.hasSameData(building.get(), buildingServiceModel)) {
            throw new UnprocessableEntityException("New data is same as old data!");
        }

        if (buildingServiceModel.getFloors() > building.get().getFloors()) {
            throw new UnprocessableEntityException("Floor number is not valid!");
        }

        Optional<Building> duplicate =
                this.buildingRepository.findByAddress(buildingServiceModel.getAddress());

        if (this.hasSameAddress(duplicate, buildingId)) {
            throw new UnprocessableEntityException("This address is already used by another building!");
        }

        duplicate = this.buildingRepository.findByNameAndNeighbourhood(
                buildingServiceModel.getName().trim(),
                buildingServiceModel.getNeighbourhood().trim());

        if (duplicate.isPresent()
                && !duplicate.get().getId().equals(buildingId)) {
            throw new BuildingAlreadyExistsException(
                    String.format("Building with name \"%s\" already exists in \"%s\"!",
                            buildingServiceModel.getName(), buildingServiceModel.getNeighbourhood())
            );
        }

        building.get().setName(buildingServiceModel.getName());
        building.get().setNeighbourhood(buildingServiceModel.getNeighbourhood());
        building.get().setAddress(buildingServiceModel.getAddress());
        building.get().setAddedOn(buildingServiceModel.getAddedOn());
        this.buildingRepository.saveAndFlush(building.get());

        return this.modelMapper.map(building.get(), BuildingServiceModel.class);
    }

    @Override
    public void deleteAll() {
        List<Building> buildings = this.buildingRepository.findAll();

        for (Building building : buildings) {
            String buildingId = building.getId();

            Set<String> staffIds = building.getStaff().stream()
                    .map(Staff::getId)
                    .collect(Collectors.toUnmodifiableSet());

            if (!staffIds.isEmpty()) {
                this.staffService.cancelBuildingAssignments(staffIds, buildingId);
            }

            for (Apartment apartment : building.getApartments()) {
                this.residentService.deleteAllByApartmentId(buildingId, apartment.getId());
            }
        }

        this.buildingRepository.deleteAll(buildings);
    }

    @Override
    @Transactional
    public void delete(String buildingId) {
        Building building = this.buildingRepository.findById(buildingId).orElse(null);

        if (building == null) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Set<String> staffIds = building.getStaff().stream()
                .map(Staff::getId)
                .collect(Collectors.toUnmodifiableSet());

        if (!staffIds.isEmpty()) {
            this.staffService.cancelBuildingAssignments(staffIds, buildingId);
        }

        this.apartmentService.deleteAllByBuildingId(buildingId);

        this.buildingRepository.delete(building);
    }

    @Override
    public void assignStaff(String buildingId, Set<String> staffIds) {
        Building building = this.buildingRepository.findById(buildingId).orElse(null);

        if (building == null) {
            throw new EntityNotFoundException("Building not found!");
        }

        Set<Staff> staff = this.staffService.getAllByIdIn(staffIds).stream()
                .map(s -> this.modelMapper.map(s, Staff.class))
                .collect(Collectors.toUnmodifiableSet());

        for (Staff employee : staff) {
            this.staffService.assignBuildings(employee.getId(), Set.of(buildingId));
        }
    }

    @Override
    public Set<BuildingServiceModel> getAllByIdIn(Set<String> ids) {
        Set<BuildingServiceModel> buildingServiceModels =
                this.buildingRepository.findAllByIdIn(ids).stream()
                        .map(building -> this.modelMapper.map(building, BuildingServiceModel.class))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(buildingServiceModels);
    }

    private boolean hasSameData(Building building, BuildingServiceModel buildingServiceModel) {
        return building.getName().equals(buildingServiceModel.getName())
                && building.getNeighbourhood().equals(buildingServiceModel.getNeighbourhood())
                && building.getAddress().equals(buildingServiceModel.getAddress())
                && building.getFloors() == buildingServiceModel.getFloors()
                && building.getAddedOn().equals(buildingServiceModel.getAddedOn());
    }

    private boolean hasSameAddress(Optional<Building> duplicate, String buildingId) {
        return duplicate.isPresent() && !duplicate.get().getId().equals(buildingId);
    }

}
