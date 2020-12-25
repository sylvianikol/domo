package com.syn.domo.service.impl;

import com.syn.domo.exception.BuildingExistsException;
import com.syn.domo.exception.BuildingNotFoundException;
import com.syn.domo.exception.FloorNotValidException;
import com.syn.domo.exception.SameDataException;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public BuildingServiceImpl(BuildingRepository buildingRepository, ApartmentService apartmentService, ModelMapper modelMapper) {
        this.buildingRepository = buildingRepository;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public BuildingServiceModel add(BuildingServiceModel buildingServiceModel) {
        // TODO: validation

        Optional<BuildingServiceModel> duplicate = this.getOne(
                buildingServiceModel.getName().trim(),
                buildingServiceModel.getAddress().trim(),
                buildingServiceModel.getNeighbourhood().trim());

        if (duplicate.isPresent()) {
            throw new BuildingExistsException("Building already exists!");
        }

        Building building = this.modelMapper.map(buildingServiceModel, Building.class);
        building.setAddedOn(LocalDate.now());
        building.setApartments(new LinkedHashSet<>());
        this.buildingRepository.saveAndFlush(building);

        return this.modelMapper.map(building, BuildingServiceModel.class);
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
    @Transactional
    public BuildingServiceModel delete(String buildingId) {
        Optional<Building> building = this.buildingRepository.findById(buildingId);

        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        this.apartmentService.deleteAllByBuildingId(buildingId);
        BuildingServiceModel buildingServiceModel
                = this.modelMapper.map(building.get(), BuildingServiceModel.class);
        this.buildingRepository.delete(building.get());
        return buildingServiceModel;
    }

    @Override
    public BuildingServiceModel edit(BuildingServiceModel buildingServiceModel, String buildingId) {
        // TODO: validation

        Optional<BuildingServiceModel> duplicate = this.getOne(
                buildingServiceModel.getName().trim(),
                buildingServiceModel.getAddress().trim(),
                buildingServiceModel.getNeighbourhood().trim());

        if (duplicate.isPresent() && !duplicate.get().getId().equals(buildingId)) {
            throw new BuildingExistsException("Building already exists!");
        }

        Optional<Building> building = this.buildingRepository.findById(buildingId);

        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        if (this.isSameData(building.get(), buildingServiceModel)) {
            throw new SameDataException("Same data!");
        }

        if (buildingServiceModel.getFloors() > building.get().getFloors()) {
            throw new FloorNotValidException("Floor number not valid!");
        }

        building.get().setName(buildingServiceModel.getName());
        building.get().setNeighbourhood(buildingServiceModel.getNeighbourhood());
        building.get().setAddress(buildingServiceModel.getAddress());
        building.get().setAddedOn(buildingServiceModel.getAddedOn());
        this.buildingRepository.saveAndFlush(building.get());

        return this.modelMapper.map(building.get(), BuildingServiceModel.class);
    }


    @Override
    public Optional<BuildingServiceModel> getOne(String buildingName,
                                                 String buildingAddress, String neighbourhood) {
        Optional<Building> optionalBuilding = this.buildingRepository
                .findByNameAndAddressAndNeighbourhood(buildingName, buildingAddress, neighbourhood);

        return optionalBuilding.map(building ->
                this.modelMapper.map(building, BuildingServiceModel.class));
    }

    @Override
    public Optional<BuildingServiceModel> getById(String id) {
        Optional<Building> building = this.buildingRepository.findById(id);
        return building.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(building.get(), BuildingServiceModel.class));
    }

    private boolean isSameData(Building building, BuildingServiceModel buildingServiceModel) {
        return building.getName().equals(buildingServiceModel.getName())
                && building.getNeighbourhood().equals(buildingServiceModel.getNeighbourhood())
                && building.getAddress().equals(buildingServiceModel.getAddress())
                && building.getFloors() == buildingServiceModel.getFloors()
                && building.getAddedOn().equals(buildingServiceModel.getAddedOn());
    }
}
