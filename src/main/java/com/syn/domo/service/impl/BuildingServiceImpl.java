package com.syn.domo.service.impl;

import com.syn.domo.error.ErrorContainer;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.*;
import com.syn.domo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;

import static com.syn.domo.common.ExceptionErrorMessages.BUILDING_NOT_FOUND;
import static com.syn.domo.common.ValidationErrorMessages.*;

@Service
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final ApartmentService apartmentService;
    private final StaffService staffService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public BuildingServiceImpl(BuildingRepository buildingRepository,
                               ApartmentService apartmentService,
                               @Lazy StaffService staffService,
                               ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.buildingRepository = buildingRepository;
        this.apartmentService = apartmentService;
        this.staffService = staffService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Set<BuildingServiceModel> getAll(Pageable pageable) {

        Set<BuildingServiceModel> buildings = this.buildingRepository
                .findAll(pageable).getContent().stream()
                .map(b -> this.modelMapper.map(b, BuildingServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(buildings);
    }

    @Override
    public Optional<BuildingServiceModel> get(String id) {
        Optional<Building> building = this.buildingRepository.findById(id);
        return building.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(building.get(), BuildingServiceModel.class));
    }

    @Override
    public ResponseModel<BuildingServiceModel> add(BuildingServiceModel buildingServiceModel) {

        if (!this.validationUtil.isValid(buildingServiceModel)) {
            return new ResponseModel<>(buildingServiceModel,
                    this.validationUtil.violations(buildingServiceModel));
        }

        String address = buildingServiceModel.getAddress().trim();
        if (this.buildingRepository.findByAddress(address).isPresent()) {
            new ResponseModel<>(buildingServiceModel, new ErrorContainer(
                    Map.of("address", Set.of(String.format(ADDRESS_OCCUPIED, address)))));
        }

        String buildingName = buildingServiceModel.getName().trim();
        String neighbourhood = buildingServiceModel.getNeighbourhood().trim();

        if (this.buildingNameExistsInNeighbourhood(buildingName, neighbourhood, "")) {
            return new ResponseModel<>(buildingServiceModel, new ErrorContainer(
                    Map.of("name", Set.of(String.format(BUILDING_NAME_EXISTS,
                            buildingName, neighbourhood)))));
        }

        Building building = this.modelMapper.map(buildingServiceModel, Building.class);
        building.setAddedOn(LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        return new ResponseModel<>(building.getId(),
                this.modelMapper.map(building, BuildingServiceModel.class));
    }

    @Override
    public ResponseModel<BuildingServiceModel> edit(BuildingServiceModel buildingServiceModel, String buildingId) {

        if (!this.validationUtil.isValid(buildingServiceModel)) {
            return new ResponseModel<>(buildingServiceModel,
                    this.validationUtil.violations(buildingServiceModel));
        }

        Building building = this.buildingRepository.findById(buildingId)
                .orElseThrow(() -> { throw new EntityNotFoundException(BUILDING_NOT_FOUND); });

        String address = buildingServiceModel.getAddress().trim();
        if (this.buildingRepository.findByIdAndAddress(buildingId, address).isPresent()) {
            return new ResponseModel<>(buildingServiceModel, new ErrorContainer(
                    Map.of("address", Set.of(String.format(ADDRESS_OCCUPIED, address)))));
        }

        String buildingName = buildingServiceModel.getName().trim();
        String neighbourhood = buildingServiceModel.getNeighbourhood().trim();

        if (this.buildingNameExistsInNeighbourhood(buildingName, neighbourhood, buildingId)) {
            return new ResponseModel<>(buildingServiceModel, new ErrorContainer(
                    Map.of("name", Set.of(String.format(BUILDING_NAME_EXISTS,
                            buildingName, neighbourhood)))));
        }

        building.setName(buildingServiceModel.getName());
        building.setNeighbourhood(buildingServiceModel.getNeighbourhood());
        building.setAddress(buildingServiceModel.getAddress());
        building.setFloors(buildingServiceModel.getFloors());

        this.buildingRepository.saveAndFlush(building);

        return new ResponseModel<>(building.getId(),
                this.modelMapper.map(building, BuildingServiceModel.class));
    }

    @Override
    public void deleteAll() {
        List<Building> buildings = this.buildingRepository.findAll();

        for (Building building : buildings) {
            String buildingId = building.getId();

            Set<String> staffIds = getStaffIds(building.getStaff());

            if (!staffIds.isEmpty()) {
                this.staffService.cancelBuildingAssignments(staffIds, buildingId);
            }

            this.apartmentService.evacuateApartments(buildingId);
        }

        this.buildingRepository.deleteAll(buildings);
    }

    @Override
    @Transactional
    public void delete(String buildingId) {

        Building building = this.buildingRepository.findById(buildingId)
                .orElseThrow(() -> { throw new EntityNotFoundException(BUILDING_NOT_FOUND); });

        Set<String> staffIds = getStaffIds(building.getStaff());

        if (!staffIds.isEmpty()) {
            this.staffService.cancelBuildingAssignments(staffIds, buildingId);
        }

        this.apartmentService.evacuateApartments(buildingId);
        this.buildingRepository.delete(building);
    }

    @Override
    public void assignStaff(String buildingId, Set<String> staffIds) {

        if(this.buildingRepository.findById(buildingId).isEmpty()) {
            throw new EntityNotFoundException(BUILDING_NOT_FOUND);
        }

        Set<Staff> staff = this.staffService.getAllByIdIn(staffIds).stream()
                .map(s -> this.modelMapper.map(s, Staff.class))
                .collect(Collectors.toUnmodifiableSet());

        for (Staff employee : staff) {
            this.staffService.assignBuildings(employee.getId(), Set.of(buildingId));
        }
    }

    @Override
    public Set<BuildingServiceModel> getAllByIdIn(Set<String> ids) {

        return this.buildingRepository.findAllByIdIn(ids).stream()
                .map(building -> this.modelMapper.map(building, BuildingServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void addToBudget(BigDecimal total, String buildingId) {
        Building building = this.buildingRepository.findById(buildingId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(BUILDING_NOT_FOUND);
                });

        building.setBudget(building.getBudget().add(total));
        this.buildingRepository.saveAndFlush(building);
    }

    private Set<String> getStaffIds(Set<Staff> staff) {
        return staff.stream().map(Staff::getId).collect(Collectors.toUnmodifiableSet());
    }

    private boolean buildingNameExistsInNeighbourhood(String buildingName,
                                                      String neighbourhood, String buildingId) {
        Optional<Building> building = this.buildingRepository
                .findByNameAndNeighbourhood(buildingName, neighbourhood);
        return building.isPresent() && !building.get().getId().equals(buildingId);
    }
}
