package com.syn.domo.service.impl;

import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.BaseServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.repository.BuildingRepository;
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
    private final ModelMapper modelMapper;

    @Autowired
    public BuildingServiceImpl(BuildingRepository buildingRepository, FloorService floorService, ModelMapper modelMapper) {
        this.buildingRepository = buildingRepository;
        this.floorService = floorService;
        this.modelMapper = modelMapper;
    }

    @Override
    public BuildingServiceModel getById(String id) {
        // TODO: BuildingNotFoundException
        Building building = this.buildingRepository.findById(id).orElse(null);
        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public BuildingServiceModel addBuilding(BuildingAddBindingModel buildingAddBindingModel) {
        // TODO: validation
        Building building = this.modelMapper.map(buildingAddBindingModel, Building.class);
        building.setAddedOn(LocalDate.now());
        this.buildingRepository.saveAndFlush(building);
        String buildingId = building.getId();

        this.floorService.createFloors(buildingAddBindingModel.getFloorsNumber(), buildingId);

        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public void saveBuilding(BuildingServiceModel buildingServiceModel) {
        Building building = this.modelMapper.map(buildingServiceModel, Building.class);
        this.buildingRepository.saveAndFlush(building);
    }

    @Override
    public boolean hasBuildings() {
        return this.buildingRepository.count() > 0;
    }

    @Override
    public Set<BuildingServiceModel> getAllBuildings() {
        Set<BuildingServiceModel> buildingServiceModels = this.buildingRepository.findAllByOrderByName().stream()
                .map(building -> this.modelMapper.map(building, BuildingServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(buildingServiceModels);
    }

    @Override
    public int getCount() {
        return (int) this.buildingRepository.count();
    }

}
