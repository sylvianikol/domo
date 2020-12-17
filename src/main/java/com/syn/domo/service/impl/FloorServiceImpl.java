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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
    public Set<FloorServiceModel> createFloors(int floorsNumber, String buildingId) {

        Building building = this.modelMapper.map(
                this.buildingService.getById(buildingId), Building.class);

        Set<FloorServiceModel> floorServiceModels = new LinkedHashSet<>();

        for (int number = 1; number <= floorsNumber; number++) {
            Floor floor = new Floor();
            floor.setNumber(number);
            floor.setBuilding(building);
            this.floorRepository.saveAndFlush(floor);
            floorServiceModels.add(this.modelMapper.map(floor, FloorServiceModel.class));
        }

        return floorServiceModels;
    }

    @Override
    public Set<Integer> getAllFloorNumbersByBuildingId(String buildingId) {
        Set<Integer> floorNumbers =
                this.floorRepository.findAllByBuilding_IdOrderByNumber(buildingId).stream()
                .map(Floor::getNumber)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(floorNumbers);
    }
}
