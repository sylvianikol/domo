package com.syn.domo.service.impl;

import com.syn.domo.model.binding.BuildingConstructModel;
import com.syn.domo.model.entity.Floor;
import com.syn.domo.model.service.FloorServiceModel;
import com.syn.domo.model.view.BuildingViewModel;
import com.syn.domo.repository.FloorRepository;
import com.syn.domo.service.FloorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public FloorServiceImpl(FloorRepository floorRepository, ModelMapper modelMapper) {
        this.floorRepository = floorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public FloorServiceModel getByNumber(int number) {
        return this.floorRepository.findByNumber(number)
                .map(floor -> this.modelMapper.map(floor, FloorServiceModel.class))
                .orElse(null);
    }

    @Override
    public BuildingViewModel constructBuilding(BuildingConstructModel buildingConstructModel) {
        // TODO: validation
        for (int number = 1; number <= buildingConstructModel.getFloorsNumber(); number++) {
            Floor floor = new Floor();
            floor.setNumber(number);
            floor.setApartmentsPerFloor(buildingConstructModel.getApartmentsPerFloor());
            this.floorRepository.saveAndFlush(floor);
        }
        int apartmentsCount = buildingConstructModel.getApartmentsPerFloor() * buildingConstructModel.getFloorsNumber();
        BuildingViewModel buildingViewModel = new BuildingViewModel();
        buildingViewModel.setFloors(buildingConstructModel.getFloorsNumber());
        buildingViewModel.setTotalApartments(apartmentsCount);
        return buildingViewModel;
    }

    @Override
    public BuildingViewModel getBuildingDetails() {
        BuildingViewModel buildingViewModel = new BuildingViewModel();
        buildingViewModel.setFloors((int) this.floorRepository.count());
        buildingViewModel.setTotalApartments(this.floorRepository.sumTotalApartments());
        return buildingViewModel;
    }

    @Override
    public boolean isBuilt() {
        return this.floorRepository.count() > 0;
    }
}
