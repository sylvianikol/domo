package com.syn.domo.service.impl;

import com.syn.domo.error.ErrorContainer;
import com.syn.domo.exception.DomoEntityExistsException;
import com.syn.domo.exception.DomoEntityNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.*;
import com.syn.domo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;

import static com.syn.domo.common.ExceptionErrorMessages.*;
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
    public Set<BuildingServiceModel> getAll() {
        Set<BuildingServiceModel> buildings = this.buildingRepository.findAll().stream()
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
    public BuildingServiceModel add(BuildingServiceModel buildingToAdd) {

        if (!this.validationUtil.isValid(buildingToAdd)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED,
                    this.validationUtil.getViolations(buildingToAdd));
        }

        String address = buildingToAdd.getAddress().trim();
        if (this.buildingRepository.findByAddress(address).isPresent()) {
            throw new UnprocessableEntityException(UNPROCESSABLE_ENTITY,
                    new ErrorContainer(Map.of("address",
                            Set.of(String.format(ADDRESS_OCCUPIED, address)))));
        }

        String buildingName = buildingToAdd.getName().trim();
        String neighbourhood = buildingToAdd.getNeighbourhood().trim();

        if (this.buildingNameExistsInNeighbourhood(buildingName, neighbourhood, "")) {
            throw new UnprocessableEntityException(UNPROCESSABLE_ENTITY,
                    new ErrorContainer(Map.of("nameExists",
                            Set.of(String.format(BUILDING_NAME_EXISTS,  buildingName, neighbourhood)))));
        }

        Building building = this.modelMapper.map(buildingToAdd, Building.class);
        building.setAddedOn(LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public BuildingServiceModel edit(BuildingServiceModel buildingToEdit, String buildingId) {

        if (!this.validationUtil.isValid(buildingToEdit)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED,
                    this.validationUtil.getViolations(buildingToEdit));
        }

        Building building = this.buildingRepository.findById(buildingId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(BUILDING_NOT_FOUND); });

        String address = buildingToEdit.getAddress().trim();
        if (this.buildingRepository.findByIdIsNotAndAddress(buildingId, address).isPresent()) {
            throw new UnprocessableEntityException(UNPROCESSABLE_ENTITY,
                    new ErrorContainer(Map.of("address", Set.of(String.format(ADDRESS_OCCUPIED, address)))));
        }

        String buildingName = buildingToEdit.getName().trim();
        String neighbourhood = buildingToEdit.getNeighbourhood().trim();

        if (this.buildingNameExistsInNeighbourhood(buildingName, neighbourhood, buildingId)) {
            throw new DomoEntityExistsException(ENTITY_EXISTS, new ErrorContainer(Map.of("nameExists",
                            Set.of(String.format(BUILDING_NAME_EXISTS, buildingName, neighbourhood)))));
        }

        building.setName(buildingToEdit.getName());
        building.setNeighbourhood(buildingToEdit.getNeighbourhood());
        building.setAddress(buildingToEdit.getAddress());
        building.setFloors(buildingToEdit.getFloors());

        this.buildingRepository.saveAndFlush(building);

        return this.modelMapper.map(building, BuildingServiceModel.class);
    }

    @Override
    public int deleteAll() {
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
        return buildings.size();
    }

    @Override
    @Transactional
    public void delete(String buildingId) {

        Building building = this.buildingRepository.findById(buildingId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(BUILDING_NOT_FOUND); });

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
            throw new DomoEntityNotFoundException(BUILDING_NOT_FOUND);
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
                    throw new DomoEntityNotFoundException(BUILDING_NOT_FOUND);
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
