package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Floor;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.FloorServiceModel;
import com.syn.domo.repository.FloorRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FloorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepository;
    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public FloorServiceImpl(FloorRepository floorRepository,
                            @Lazy BuildingService buildingService,
                            ModelMapper modelMapper) {
        this.floorRepository = floorRepository;
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @Override
    public FloorServiceModel getByNumber(int number) {
        return this.modelMapper.map(this.floorRepository.findByNumber(number), FloorServiceModel.class);
    }

    @Override
    public void createFloors(int floorsNumber, String buildingId) {

        Building building = this.modelMapper.map(
                this.buildingService.getByName(buildingId), Building.class);

        for (int number = 1; number <= floorsNumber; number++) {
            Floor floor = new Floor();
            floor.setNumber(number);
            floor.setBuilding(building);
            this.floorRepository.saveAndFlush(floor);
            building.getFloors().add(floor);

            this.buildingService.saveBuilding(this.modelMapper.map(
                    building, BuildingServiceModel.class));
        }
    }

    @Override
    public List<Integer> getAllFloorNumbers() {
        return this.floorRepository.findAllByOrderByNumber().stream()
                .map(Floor::getNumber)
                .collect(Collectors.toList());
    }
}
