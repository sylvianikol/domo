package com.syn.domo.service.impl;

import com.syn.domo.common.DefaultParamValues;
import com.syn.domo.error.ErrorContainer;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.RoleServiceModel;
import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.repository.StaffRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.RoleService;
import com.syn.domo.service.StaffService;
import com.syn.domo.service.UserService;
import com.syn.domo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.DefaultParamValues.EMPTY_URL;
import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.EMAIL_ALREADY_USED;
import static com.syn.domo.common.ValidationErrorMessages.PHONE_ALREADY_USED;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final UserService userService;
    private final BuildingService buildingService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository,
                            UserService userService,
                            BuildingService buildingService,
                            RoleService roleService,
                            ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.staffRepository = staffRepository;
        this.userService = userService;
        this.buildingService = buildingService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Set<StaffServiceModel> getAll(String buildingId) {

        Set<StaffServiceModel> staff;

        if (buildingId.equals(EMPTY_URL)) {
            staff = this.staffRepository.findAll().stream()
                    .map(s -> this.modelMapper.map(s, StaffServiceModel.class))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            if (this.buildingService.get(buildingId).isEmpty()) {
                throw new EntityNotFoundException("Building not found!");
            }

            staff = this.staffRepository.getAllByBuildingId(buildingId).stream()
                    .map(s -> this.modelMapper.map(s, StaffServiceModel.class))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return Collections.unmodifiableSet(staff);
    }

    @Override
    public Optional<StaffServiceModel> get(String staffId) {
        Optional<Staff> staff = this.staffRepository.findById(staffId);
        return staff.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(staff.get(), StaffServiceModel.class));
    }

    @Override
    public ResponseModel<StaffServiceModel> add(StaffServiceModel staffServiceModel) {

        if (!this.validationUtil.isValid(staffServiceModel)) {
            return new ResponseModel<>(staffServiceModel,
                    this.validationUtil.violations(staffServiceModel));
        }

        String email = staffServiceModel.getEmail();
        if (this.userService.getByEmail(email).isPresent()) {
            return new ResponseModel<>(staffServiceModel,
                    new ErrorContainer(Map.of("email",
                            Set.of(String.format(EMAIL_ALREADY_USED,
                                    staffServiceModel.getEmail())))
                    ));
        }

        String phoneNumber = staffServiceModel.getPhoneNumber();
        if (this.userService.getByPhoneNumber(phoneNumber).isPresent()) {
            return new ResponseModel<>(staffServiceModel,
                    new ErrorContainer(Map.of("phoneNumber",
                            Set.of(String.format(PHONE_ALREADY_USED,
                                    staffServiceModel.getPhoneNumber())))
                    ));
        }

        RoleServiceModel roleServiceModel = this.roleService
                .getByName(UserRole.STAFF)
                .orElseThrow(() -> { throw new EntityNotFoundException(ROLE_NOT_FOUND); });

        Staff staff = this.modelMapper.map(staffServiceModel, Staff.class);

        staff.setRoles(Set.of(this.modelMapper.map(roleServiceModel, Role.class)));
        staff.setAddedOn(LocalDate.now());
        staff.setSalary(staffServiceModel.getSalary());
        staff.setJob(staffServiceModel.getJob());

        this.staffRepository.saveAndFlush(staff);

        // TODO: Send email with link to create password

        return new ResponseModel<>(staff.getId(),
                this.modelMapper.map(staff, StaffServiceModel.class));
    }

    @Override
    public ResponseModel<StaffServiceModel> edit(StaffServiceModel staffServiceModel, String staffId) {

        if (!this.validationUtil.isValid(staffServiceModel)) {
            return new ResponseModel<>(staffServiceModel,
                    this.validationUtil.violations(staffServiceModel));
        }

        String email = staffServiceModel.getEmail();
        if (this.userService.notUniqueEmail(email, staffId)) {
            return new ResponseModel<>(staffServiceModel,
                    new ErrorContainer(Map.of("email",
                            Set.of(String.format(EMAIL_ALREADY_USED, email)))
                    ));
        }

        String phoneNumber = staffServiceModel.getPhoneNumber();
        if (this.userService.notUniquePhoneNumber(phoneNumber, staffId)) {
            return new ResponseModel<>(staffServiceModel,
                    new ErrorContainer(Map.of("phoneNumber",
                            Set.of(String.format(PHONE_ALREADY_USED, phoneNumber)))
                    ));
        }

        Staff staff = this.staffRepository.findById(staffId)
                .orElseThrow(() -> { throw new EntityNotFoundException(STAFF_NOT_FOUND); });


        staff.setFirstName(staffServiceModel.getFirstName());
        staff.setLastName(staffServiceModel.getLastName());
        staff.setEmail(staffServiceModel.getEmail());
        staff.setPhoneNumber(staffServiceModel.getPhoneNumber());
        staff.setSalary(staffServiceModel.getSalary());
        staff.setJob(staffServiceModel.getJob());

        this.staffRepository.saveAndFlush(staff);

        return new ResponseModel<>(staff.getId(),
                this.modelMapper.map(staff, StaffServiceModel.class));
    }

    @Override
    @Transactional
    public void deleteAll(String buildingId) {

        List<Staff> staff;

        if (buildingId.equals(EMPTY_URL)) {
            staff = this.staffRepository.findAll();
        } else {
            staff = this.staffRepository.getAllByBuildingId(buildingId);
        }

        for (Staff employee : staff) {
            this.staffRepository.cancelBuildingAssignments(employee.getId());
        }

        this.staffRepository.deleteAll(staff);
    }

    @Override
    @Transactional
    public void delete(String staffId) {

        Staff staff = this.staffRepository.findById(staffId)
                .orElseThrow(() -> { throw new EntityNotFoundException(STAFF_NOT_FOUND); });

        this.staffRepository.cancelBuildingAssignments(staff.getId());

       this.staffRepository.delete(staff);
    }

    @Override
    public void assignBuildings(String staffId, Set<String> buildingIds) {

        Staff staff = this.staffRepository.findById(staffId)
                .orElseThrow(() -> { throw new EntityNotFoundException(STAFF_NOT_FOUND); });

        Set<Building> buildings = this.buildingService.getAllByIdIn(buildingIds)
                .stream()
                .map(b -> this.modelMapper.map(b, Building.class))
                .collect(Collectors.toUnmodifiableSet());

        staff.getBuildings().addAll(buildings);

        this.staffRepository.saveAndFlush(staff);
    }

    @Override
    public void cancelBuildingAssignments(Set<String> staffIds, String buildingId) {

        BuildingServiceModel buildingServiceModel = this.buildingService.get(buildingId)
                .orElseThrow(() -> { throw new EntityNotFoundException(BUILDING_NOT_FOUND); });

        Building building = this.modelMapper.map(buildingServiceModel, Building.class);
        Set<Staff> staff = this.staffRepository.findAllByIdIn(staffIds);

        for (Staff employee : staff) {
            employee.getBuildings().remove(building);
            this.staffRepository.saveAndFlush(employee);
        }
    }

    @Override
    public Set<StaffServiceModel> getAllByIdIn(Set<String> staffIds) {
        Set<StaffServiceModel> staffServiceModels = this.staffRepository.findAllByIdIn(staffIds).stream()
                .map(staff -> this.modelMapper.map(staff, StaffServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(staffServiceModels);
    }
}
