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

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
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
    public BuildingServiceModel add(BuildingServiceModel buildingServiceModel) {
        // TODO: validation

        if (this.alreadyExists(buildingServiceModel.getName().trim(),
                buildingServiceModel.getAddress().trim(),
                buildingServiceModel.getNeighbourhood().trim())) {
            throw new BuildingExistsException("Building already exists!");
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
    public boolean hasActiveBuildings() {
        return this.getAllBuildings().size() > 0;
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
    public int getCount() {
        return this.getAllBuildings().size();
    }

    @Override
    public String getBuildingName(String id) {
        return this.getById(id).getName();
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

        Optional<BuildingServiceModel> toBeEdited = this.getBuilding(
                buildingServiceModel.getName().trim(),
                buildingServiceModel.getAddress().trim(),
                buildingServiceModel.getNeighbourhood().trim());

        if (toBeEdited.isPresent() && !toBeEdited.get().getId().equals(buildingId)) {
            throw new BuildingExistsException("Building already exists!");
        }

        Building building = this.getBuildingByIdOrThrow(buildingId);
        building.setName(buildingServiceModel.getName());
        building.setNeighbourhood(buildingServiceModel.getNeighbourhood());
        building.setAddress(buildingServiceModel.getAddress());
        building.setAddedOn(buildingServiceModel.getAddedOn());
        building.setArchivedOn(buildingServiceModel.getArchivedOn());
        this.buildingRepository.saveAndFlush(building);

        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    private Building getBuildingByIdOrThrow(String buildingId) {
        return this.buildingRepository.findById(buildingId)
                .orElseThrow(() -> {
                    throw new BuildingNotFoundException("Building not found");
                });
    }

    private boolean alreadyExists(String name, String address, String neighbourhood) {
        return this.buildingRepository
                .findByNameAndAddressAndNeighbourhood(name, address, neighbourhood).isPresent();
    }

    @Override
    public Optional<BuildingServiceModel> getBuilding(String buildingName,
                                            String buildingAddress, String neighbourhood) {
        Optional<Building> optionalBuilding = this.buildingRepository
                .findByNameAndAddressAndNeighbourhood(buildingName, buildingAddress, neighbourhood);

        return optionalBuilding.map(building ->
                this.modelMapper.map(building, BuildingServiceModel.class));
    }

    @Override
    public BuildingServiceModel getById(String buildingId) {
        Building building = getBuildingByIdOrThrow(buildingId);

        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public Optional<BuildingServiceModel> getOptById(String id) {
        Optional<Building> building = this.buildingRepository.findById(id);
        return building.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(building.get(), BuildingServiceModel.class));
    }
}
