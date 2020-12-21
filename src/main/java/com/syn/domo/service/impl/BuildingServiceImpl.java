package com.syn.domo.service.impl;

import com.syn.domo.exception.BuildingArchivedExistsException;
import com.syn.domo.exception.BuildingExistsException;
import com.syn.domo.exception.BuildingNotFoundException;
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

        String buildingName = buildingServiceModel.getName().trim();
        String buildingAddress = buildingServiceModel.getAddress().trim();

        if (this.alreadyExists(buildingName, buildingAddress)) {
            throw new BuildingExistsException(String.format(
                    "Building %s (%s) already exists",
                    buildingName, buildingAddress));
        }

        if (this.isArchived(buildingName, buildingAddress)) {
            throw new BuildingArchivedExistsException(String.format(
                    "Building '%s' (%s) already exists but is currently inactive. Do you wish to activate it?",
                    buildingName, buildingAddress));
        }

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
    public boolean hasBuildings() {
        return this.getAllBuildings().size() > 0;
    }

    @Override
    public Set<BuildingServiceModel> getAllBuildings() {
        Set<BuildingServiceModel> buildingServiceModels =
                this.buildingRepository.findAllByArchivedOnNullOrderByName().stream()
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
                    throw new BuildingNotFoundException(String.format(
                            "Building %s (%s) not found!", buildingName, buildingAddress));
                });
        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    private Building getBuildingByIdOrThrow(String buildingId) {
        return this.buildingRepository.findById(buildingId)
                .orElseThrow(() -> {
                    throw new BuildingNotFoundException("Building not found");
                });
    }

}
