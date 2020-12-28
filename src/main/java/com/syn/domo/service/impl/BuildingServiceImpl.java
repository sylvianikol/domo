package com.syn.domo.service.impl;

import com.syn.domo.exception.*;
import com.syn.domo.model.entity.Building;
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
            throw new BuildingAlreadyExistsException(String.format(
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
    public Set<BuildingServiceModel> getAll() {

        Set<BuildingServiceModel> buildingServiceModels =
                this.buildingRepository.findAll().stream()
                .map(building -> this.modelMapper.map(building, BuildingServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(buildingServiceModels);
    }

    @Override
    @Transactional
    public void delete(String buildingId) {
        Optional<Building> building = this.buildingRepository.findById(buildingId);

        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        this.apartmentService.deleteAllByBuildingId(buildingId);

        this.buildingRepository.delete(building.get());
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
