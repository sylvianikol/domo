package com.syn.domo.service.impl;

import com.syn.domo.exception.RoleNotFoundException;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.RoleServiceModel;
import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.repository.StaffRepository;
import com.syn.domo.service.RoleService;
import com.syn.domo.service.StaffService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository,
                            RoleService roleService,
                            ModelMapper modelMapper) {
        this.staffRepository = staffRepository;
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

        this.staffRepository.saveAndFlush(staff);

        return this.modelMapper.map(staff, StaffServiceModel.class);
    }

    @Override
    public StaffServiceModel edit(StaffServiceModel staffServiceModel) {
        return null;
    }

    @Override
    public void delete(String staffId) {

    }
}
