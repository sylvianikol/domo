package com.syn.domo.service.impl;

import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Floor;
import com.syn.domo.model.service.BaseServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.FloorServiceModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FloorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
        return this.buildingRepository.findById(id)
                .map(building -> this.modelMapper.map(building, BuildingServiceModel.class))
                .orElseThrow(() -> {
            throw new EntityNotFoundException("Building not found");
        });
    }

    @Override
    public BuildingServiceModel addBuilding(BuildingServiceModel buildingServiceModel) {
        // TODO: validation
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

    @Override
    public String getBuildingName(String id) {
        return this.getById(id).getName();
    }

}
