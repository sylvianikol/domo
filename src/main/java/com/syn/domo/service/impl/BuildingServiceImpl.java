package com.syn.domo.service.impl;

import com.syn.domo.exception.BuildingArchivedExistsException;
import com.syn.domo.exception.BuildingExistsException;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Floor;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.FloorServiceModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FloorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final FloorService floorService;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public BuildingServiceImpl(BuildingRepository buildingRepository, FloorService floorService, ApartmentService apartmentService, ModelMapper modelMapper) {
        this.buildingRepository = buildingRepository;
        this.floorService = floorService;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public BuildingServiceModel getById(String buildingId) {
        Building building = getBuildingByIdOrThrow(buildingId);

        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public BuildingServiceModel add(BuildingServiceModel buildingServiceModel) {
        // TODO: validation

        this.checkIfExistsOrArchived(buildingServiceModel.getName().trim(),
                buildingServiceModel.getAddress().trim());

        Building building = this.modelMapper.map(buildingServiceModel, Building.class);
        building.setAddedOn(LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        String buildingId = building.getId();
        Set<FloorServiceModel> floorServiceModels =
                this.floorService.createFloors(buildingServiceModel.getFloorsNumber(), buildingId);

        Set<Floor> floors = floorServiceModels.stream()
                .map(floorServiceModel -> this.modelMapper.map(floorServiceModel, Floor.class))
                .collect(Collectors.toSet());

        building.setFloors(floors);
        building.setApartments(new LinkedHashSet<>());
        this.buildingRepository.saveAndFlush(building);

        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public boolean hasActiveBuildings() {
        return this.getAllBuildings().size() > 0;
    }

    @Override
    public boolean hasNonActiveBuildings() {
        return this.getAllNonActiveBuildings().size() > 0;
    }

    @Override
    public Set<BuildingServiceModel> getAllBuildings() {

        Set<BuildingServiceModel> buildingServiceModels =
                this.buildingRepository.findAll().stream()
                .map(building -> this.modelMapper.map(building, BuildingServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(buildingServiceModels);
    }

    @Override
    public Set<BuildingServiceModel> getAllNonActiveBuildings() {
        Set<BuildingServiceModel> buildingServiceModels =
                this.buildingRepository.findAllByArchivedOnNotNullOrderByName().stream()
                        .map(building -> this.modelMapper.map(building, BuildingServiceModel.class))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(buildingServiceModels);
    }

    @Override
    public int getCount() {
        return this.getAllBuildings().size();
    }

    @Override
    public String getBuildingName(String id) {
        return this.getById(id).getName();
    }

    @Override
    public BuildingServiceModel archive(String buildingId) {
        Building building = getBuildingByIdOrThrow(buildingId);
        building.setArchivedOn(LocalDate.now());
        this.buildingRepository.saveAndFlush(building);
        this.floorService.archiveAllByBuildingId(buildingId);
        this.apartmentService.archiveAllByBuildingId(buildingId);
        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public boolean alreadyExists(String buildingName, String buildingAddress) {
        return this.buildingRepository
                .findByNameAndAddressAndArchivedOnNull(buildingName, buildingAddress).isPresent();
    }

    @Override
    public boolean isArchived(String buildingName, String buildingAddress) {
        return this.buildingRepository
                .findByNameAndAddressAndArchivedOnNotNull(buildingName, buildingAddress).isPresent();
    }

    @Override
    public BuildingServiceModel getByNameAndAddress(String buildingName, String buildingAddress) {
        Building building = this.buildingRepository.findByNameAndAddress(buildingName, buildingAddress)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(String.format(
                            "Building %s (%s) not found!", buildingName, buildingAddress));
                });
        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public BuildingServiceModel activate(String buildingId) {

        Building building = this.getBuildingByIdOrThrow(buildingId);
        building.setArchivedOn(null);
        this.buildingRepository.saveAndFlush(building);
        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    @Transactional
    public BuildingServiceModel delete(String buildingId) {
        Building building = this.getBuildingByIdOrThrow(buildingId);
        this.apartmentService.deleteAllByBuildingId(buildingId);
        this.floorService.deleteAllByBuildingId(buildingId);
        BuildingServiceModel buildingServiceModel
                = this.modelMapper.map(building, BuildingServiceModel.class);
        this.buildingRepository.delete(building);
        return buildingServiceModel;
    }

    @Override
    public BuildingServiceModel edit(BuildingServiceModel buildingServiceModel, String buildingId) {
        // TODO: validation
        checkIfExistsOrArchived(buildingServiceModel.getName().trim(),
                buildingServiceModel.getAddress().trim());

        Building building = this.getBuildingByIdOrThrow(buildingId);
        building.setName(buildingServiceModel.getName());
        building.setAddress(buildingServiceModel.getAddress());
        this.buildingRepository.saveAndFlush(building);

        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    private Building getBuildingByIdOrThrow(String buildingId) {
        return this.buildingRepository.findById(buildingId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Building not found");
                });
    }

    private void checkIfExistsOrArchived(String buildingName, String buildingAddress) {
        if (this.alreadyExists(buildingName, buildingAddress)) {
            throw new BuildingExistsException(String.format(
                    "Building %s (%s) already exists",
                    buildingName, buildingAddress));
        }

        if (this.isArchived(buildingName, buildingAddress)) {
            throw new BuildingArchivedExistsException(String.format(
                    "Building '%s' (%s) already exists but is currently inactive.",
                    buildingName, buildingAddress));
        }
    }

}
