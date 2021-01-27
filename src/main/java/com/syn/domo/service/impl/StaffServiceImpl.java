package com.syn.domo.service.impl;

import com.syn.domo.error.ErrorContainer;
import com.syn.domo.exception.DataConflictException;
import com.syn.domo.exception.DomoEntityNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.RoleServiceModel;
import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.notification.service.NotificationService;
import com.syn.domo.repository.StaffRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.RoleService;
import com.syn.domo.service.StaffService;
import com.syn.domo.service.UserService;
import com.syn.domo.web.filter.StaffFilter;
import com.syn.domo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.*;

@Service
public class StaffServiceImpl implements StaffService {

    private static final Logger log = LoggerFactory.getLogger(StaffServiceImpl.class);

    private final StaffRepository staffRepository;
    private final UserService userService;
    private final BuildingService buildingService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final NotificationService notificationService;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository,
                            UserService userService,
                            BuildingService buildingService,
                            RoleService roleService,
                            ModelMapper modelMapper,
                            ValidationUtil validationUtil,
                            NotificationService notificationService) {
        this.staffRepository = staffRepository;
        this.userService = userService;
        this.buildingService = buildingService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.notificationService = notificationService;
    }

    @Override
    public Set<StaffServiceModel> getAll(StaffFilter staffFilter, Pageable pageable) {

        Set<StaffServiceModel> staff = this.staffRepository
                .findAll(staffFilter, pageable).getContent().stream()
                .map(s -> this.modelMapper.map(s, StaffServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

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
    public StaffServiceModel add(StaffServiceModel staffToAdd) throws MessagingException, InterruptedException {

        if (!this.validationUtil.isValid(staffToAdd)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED,
                    this.validationUtil.getViolations(staffToAdd));
        }

        String email = staffToAdd.getEmail();
        if (this.userService.getByEmail(email).isPresent()) {
            throw new DataConflictException(EMAIL_ALREADY_USED, new ErrorContainer(Map.of("email",
                    Set.of(String.format(EMAIL_ALREADY_USED, email)))));
        }

        String phoneNumber = staffToAdd.getPhoneNumber();
        if (this.userService.getByPhoneNumber(phoneNumber).isPresent()) {
            throw new DataConflictException(PHONE_ALREADY_USED, new ErrorContainer(Map.of("phoneNumber",
                    Set.of(String.format(PHONE_ALREADY_USED, phoneNumber)))));
        }

        RoleServiceModel roleServiceModel = this.roleService
                .getByName(UserRole.STAFF)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(ROLE_NOT_FOUND); });

        Staff staff = this.modelMapper.map(staffToAdd, Staff.class);

        staff.setRoles(Set.of(this.modelMapper.map(roleServiceModel, Role.class)));
        staff.setAddedOn(LocalDate.now());
        staff.setSalary(staffToAdd.getSalary());
        staff.setJob(staffToAdd.getJob());

        this.staffRepository.saveAndFlush(staff);

        StaffServiceModel addedStaff = this.modelMapper.map(staff, StaffServiceModel.class);

        this.notificationService.sendActivationEmail(addedStaff);

        return addedStaff;
    }

    @Override
    public StaffServiceModel edit(StaffServiceModel staffToEdit, String staffId) {

        if (!this.validationUtil.isValid(staffToEdit)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED,
                    this.validationUtil.getViolations(staffToEdit));
        }

        String email = staffToEdit.getEmail();
        if (this.userService.notUniqueEmail(email, staffId)) {
            throw new DataConflictException(EMAIL_ALREADY_USED, new ErrorContainer(Map.of("email",
                    Set.of(String.format(EMAIL_ALREADY_USED, email)))));
        }

        String phoneNumber = staffToEdit.getPhoneNumber();
        if (this.userService.notUniquePhoneNumber(phoneNumber, staffId)) {
            throw new DataConflictException(PHONE_ALREADY_USED, new ErrorContainer(Map.of("phoneNumber",
                    Set.of(String.format(PHONE_ALREADY_USED, phoneNumber)))));
        }

        Staff staff = this.staffRepository.findById(staffId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(STAFF_NOT_FOUND); });


        staff.setFirstName(staffToEdit.getFirstName());
        staff.setLastName(staffToEdit.getLastName());
        staff.setEmail(staffToEdit.getEmail());
        staff.setPhoneNumber(staffToEdit.getPhoneNumber());
        staff.setSalary(staffToEdit.getSalary());
        staff.setJob(staffToEdit.getJob());

        this.staffRepository.saveAndFlush(staff);

        return this.modelMapper.map(staff, StaffServiceModel.class);
    }

    @Override
    @Transactional
    public int deleteAll(StaffFilter staffFilter) {

        List<Staff> staff = this.staffRepository.findAll(staffFilter);

        for (Staff employee : staff) {
            this.staffRepository.cancelBuildingAssignments(employee.getId());
        }

        this.staffRepository.deleteAll(staff);

        return staff.size();
    }

    @Override
    @Transactional
    public void delete(String staffId) {

        Staff staff = this.staffRepository.findById(staffId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(STAFF_NOT_FOUND); });

        this.staffRepository.cancelBuildingAssignments(staff.getId());

       this.staffRepository.delete(staff);
    }

    @Override
    public void assignBuildings(String staffId, Set<String> buildingIds) {

        Staff staff = this.staffRepository.findById(staffId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(STAFF_NOT_FOUND); });

        Set<Building> buildings = this.buildingService.getAllByIdIn(buildingIds)
                .stream()
                .map(b -> this.modelMapper.map(b, Building.class))
                .collect(Collectors.toUnmodifiableSet());

        if (buildings.isEmpty()) {
            throw new EntityNotFoundException(BUILDING_NOT_FOUND);
        }

        staff.getBuildings().addAll(buildings);

        this.staffRepository.saveAndFlush(staff);
    }

    @Override
    public void cancelBuildingAssignments(Set<String> staffIds, String buildingId) {

        BuildingServiceModel buildingServiceModel = this.buildingService.get(buildingId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(BUILDING_NOT_FOUND); });

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
