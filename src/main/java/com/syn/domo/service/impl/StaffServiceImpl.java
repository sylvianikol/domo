package com.syn.domo.service.impl;

import com.syn.domo.exception.RoleNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.RoleServiceModel;
import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.repository.StaffRepository;
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

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository,
                            UserService userService,
                            RoleService roleService,
                            ModelMapper modelMapper) {
        this.staffRepository = staffRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public StaffServiceModel add(UserServiceModel userServiceModel) {
        // TODO: validation
        String email = userServiceModel.getEmail();
        Optional<Staff> byEmail =
                this.staffRepository.findByEmail(email);

        if (byEmail.isPresent()) {
            throw new EntityExistsException(
                    String.format("Email \"%s\" is already used by another user", email));
        }

        String phoneNumber = userServiceModel.getPhoneNumber();
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

        Staff staff = this.modelMapper.map(userServiceModel, Staff.class);
        staff.setRoles(new LinkedHashSet<>());
        staff.getRoles().add(this.modelMapper.map(roleServiceModel.get(), Role.class));
        staff.setAddedOn(LocalDate.now());

        this.staffRepository.saveAndFlush(staff);

        return this.modelMapper.map(staff, StaffServiceModel.class);
    }

    @Override
    public StaffServiceModel edit(UserServiceModel userServiceModel) {
        // TODO: validation

        if (this.userService.notUniqueEmail(userServiceModel.getEmail(), userServiceModel.getId())) {
            throw new UnprocessableEntityException(
                    String.format("Email '%s' is already used by another user!",
                            userServiceModel.getEmail()));
        }

        if (this.userService.notUniquePhoneNumber(userServiceModel.getPhoneNumber(), userServiceModel.getId())) {
            throw new UnprocessableEntityException(
                    String.format("Phone number '%s' is already used by another user!",
                            userServiceModel.getPhoneNumber()));
        }

        Staff staff = this.staffRepository.findById(userServiceModel.getId()).orElse(null);

        if (staff != null) {
            staff.setFirstName(userServiceModel.getFirstName());
            staff.setLastName(userServiceModel.getLastName());
            staff.setEmail(userServiceModel.getEmail());
            staff.setPhoneNumber(userServiceModel.getPhoneNumber());
            staff.setAddedOn(userServiceModel.getAddedOn());

            this.staffRepository.saveAndFlush(staff);
        } else {
            throw new EntityNotFoundException("Staff not found!");
        }

        return this.modelMapper.map(staff, StaffServiceModel.class);
    }

    @Override
    public void delete(String staffId) {

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
