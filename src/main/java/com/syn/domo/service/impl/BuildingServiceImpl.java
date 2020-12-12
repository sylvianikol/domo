package com.syn.domo.service.impl;

import com.syn.domo.model.binding.BuildingConstructModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Floor;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.view.BuildingViewModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FloorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

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
    public boolean isBuilt() {
        return this.floorService.hasFloors();
    }

    @Override
    public Building getById(Long id) {
        return this.buildingRepository.findById(id).orElse(null);
    }

    @Override
    public BuildingViewModel getBuildingDetails(Long buildingId) {
        Building building = this.buildingRepository.findById(buildingId).orElse(null);
        BuildingViewModel buildingViewModel = new BuildingViewModel();

        if (building != null) {
            buildingViewModel.setAddress(building.getAddress());
            buildingViewModel.setFloors(building.getFloorsNumber());
            int totalCapacity = building.getFloorsNumber() * building.getApartmentsPerFloor();
            buildingViewModel.setTotalCapacity(totalCapacity);
            int addedApartments = this.apartmentService.getAllApartments().size();
            buildingViewModel.setAddedApartments(addedApartments);
        }

        return buildingViewModel;
    }

    @Override
    public BuildingViewModel constructBuilding(BuildingConstructModel buildingConstructModel) {
        // TODO: validation
        Building building = this.modelMapper.map(buildingConstructModel, Building.class);
        this.buildingRepository.saveAndFlush(building);
        Long buildingId = building.getId();

        this.floorService.createFloors(buildingConstructModel.getFloorsNumber(),
                        buildingConstructModel.getApartmentsPerFloor(), buildingId);

        return this.getBuildingDetails(buildingId);
    }

    @Override
    public void saveBuilding(Building building) {
        this.buildingRepository.saveAndFlush(building);
    }

}
