package com.syn.domo.service.impl;

import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FloorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Building getById(String id) {
        return this.buildingRepository.findById(id).orElse(null);
    }

    @Override
    public BuildingServiceModel constructBuilding(BuildingAddBindingModel buildingAddBindingModel) {
        // TODO: validation
        Building building = this.modelMapper.map(buildingAddBindingModel, Building.class);
        this.buildingRepository.saveAndFlush(building);
        String buildingId = building.getId();

        this.floorService.createFloors(buildingAddBindingModel.getFloorsNumber(), buildingId);

        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public void saveBuilding(Building building) {
        this.buildingRepository.saveAndFlush(building);
    }

    @Override
    public boolean hasBuildings() {
        return this.buildingRepository.count() > 0;
    }

    @Override
    public Set<BuildingServiceModel> getAllBuildings() {
        return this.buildingRepository.findAll().stream()
                .map(building -> this.modelMapper.map(building, BuildingServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public int getCount() {
        return (int) this.buildingRepository.count();
    }

}
