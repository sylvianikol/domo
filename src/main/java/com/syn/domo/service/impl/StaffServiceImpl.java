package com.syn.domo.service.impl;

import com.syn.domo.exception.RoleNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.binding.StaffAssignBuildingsBindingModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.RoleServiceModel;
import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.repository.StaffRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.RoleService;
import com.syn.domo.service.StaffService;
import com.syn.domo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final UserService userService;
    private final BuildingService buildingService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository,
                            UserService userService,
                            BuildingService buildingService,
                            RoleService roleService,
                            ModelMapper modelMapper) {
        this.staffRepository = staffRepository;
        this.userService = userService;
        this.buildingService = buildingService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public StaffServiceModel add(StaffServiceModel staffServiceModel) {
        // TODO: validation
        String email = staffServiceModel.getEmail();
        Optional<Staff> byEmail =
                this.staffRepository.findByEmail(email);

        if (byEmail.isPresent()) {
            throw new EntityExistsException(
                    String.format("Email \"%s\" is already used by another user", email));
        }

        String phoneNumber = staffServiceModel.getPhoneNumber();
        Optional<Staff> byPhoneNumber =
                this.staffRepository.findByPhoneNumber(phoneNumber);

        if (byPhoneNumber.isPresent()) {
            throw new EntityExistsException(
                String.format("Phone number \"%s\" is already used by another user", phoneNumber));
        }

        Optional<RoleServiceModel> roleServiceModel =
                this.roleService.getByName(UserRole.STAFF);

        if (roleServiceModel.isEmpty()) {
            throw new RoleNotFoundException("Role not found");
        }

        Staff staff = this.modelMapper.map(staffServiceModel, Staff.class);
        staff.setRoles(new LinkedHashSet<>());
        staff.getRoles().add(this.modelMapper.map(roleServiceModel.get(), Role.class));
        staff.setAddedOn(LocalDate.now());
        staff.setSalary(staffServiceModel.getSalary());
        staff.setJob(staffServiceModel.getJob());

        this.staffRepository.saveAndFlush(staff);

        return this.modelMapper.map(staff, StaffServiceModel.class);
    }

    @Override
    public StaffServiceModel edit(StaffServiceModel staffServiceModel) {
        // TODO: validation

        if (this.userService.notUniqueEmail(staffServiceModel.getEmail(), staffServiceModel.getId())) {
            throw new UnprocessableEntityException(
                    String.format("Email '%s' is already used by another user!",
                            staffServiceModel.getEmail()));
        }

        if (this.userService.notUniquePhoneNumber(staffServiceModel.getPhoneNumber(), staffServiceModel.getId())) {
            throw new UnprocessableEntityException(
                    String.format("Phone number '%s' is already used by another user!",
                            staffServiceModel.getPhoneNumber()));
        }

        Staff staff = this.staffRepository.findById(staffServiceModel.getId()).orElse(null);

        if (staff != null) {
            staff.setFirstName(staffServiceModel.getFirstName());
            staff.setLastName(staffServiceModel.getLastName());
            staff.setEmail(staffServiceModel.getEmail());
            staff.setPhoneNumber(staffServiceModel.getPhoneNumber());
            staff.setAddedOn(staffServiceModel.getAddedOn());
            staff.setSalary(staffServiceModel.getSalary());
            staff.setJob(staffServiceModel.getJob());

            this.staffRepository.saveAndFlush(staff);
        } else {
            throw new EntityNotFoundException("Staff not found!");
        }

        return this.modelMapper.map(staff, StaffServiceModel.class);
    }

    @Override
    public void delete(String staffId) {
        Staff staff = this.staffRepository.findById(staffId).orElse(null);

        if (staff != null) {

            this.buildingService.releaseStaff(staffId);

            this.staffRepository.delete(staff);
        } else {
            throw new EntityNotFoundException("Staff member not found!");
        }
    }

    @Override
    public void assignBuildings(String staffId, Set<String> buildingIds) {
        Staff staff = this.staffRepository.findById(staffId).orElse(null);

        if (staff != null) {
            Set<BuildingServiceModel> buildingServiceModels =
                    this.buildingService.getAllByIdIn(buildingIds);

            Set<Building> buildings = buildingServiceModels.stream()
                    .map(b -> this.modelMapper.map(b, Building.class))
                    .collect(Collectors.toSet());

            staff.getBuildings().addAll(buildings);

            this.staffRepository.saveAndFlush(staff);
        } else {
            throw new EntityNotFoundException("Staff not found!");
        }
    }

    @Override
    public void releaseBuilding(Set<String> staffIds, String buildingId) {
        Set<Staff> staff = this.staffRepository.findAllByIdIn(staffIds);

        Optional<BuildingServiceModel> buildingServiceModel =
                this.buildingService.getById(buildingId);

        if (buildingServiceModel.isEmpty()) {
            throw new EntityNotFoundException("Building not found!");
        }

        Building building = this.modelMapper.map(buildingServiceModel.get(), Building.class);

        for (Staff employee : staff) {
            employee.getBuildings().remove(building);
            this.staffRepository.saveAndFlush(employee);
        }
    }

    @Override
    public Optional<StaffServiceModel> getOne(String staffId) {
        Optional<Staff> staff = this.staffRepository.findById(staffId);
        return staff.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(staff.get(), StaffServiceModel.class));
    }

    @Override
    public Set<StaffServiceModel> getAll() {
        Set<StaffServiceModel> staff = this.staffRepository.findAll().stream()
                .map(s -> this.modelMapper.map(s, StaffServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(staff);
    }
}
