package com.syn.domo.service.impl;

import com.syn.domo.model.entity.*;
import com.syn.domo.model.service.*;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.repository.UserRepository;
import com.syn.domo.service.RoleService;
import com.syn.domo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initAdmin() {
        if (this.userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setFirstName(System.getenv("ADMIN_FNAME"));
            admin.setLastName(System.getenv("ADMIN_LNAME"));
            admin.setEmail(System.getenv("ADMIN_EMAIL"));
            admin.setPassword(System.getenv("ADMIN_PASS"));
            admin.setPhoneNumber(System.getenv("ADMIN_PHONE"));
            admin.setActive(true);
            admin.setAddedOn(LocalDate.now());

            Set<Role> roles = this.roleService.getAll();
            admin.setRoles(roles);

            this.userRepository.saveAndFlush(admin);
        }
    }

    @Override
    public Optional<UserServiceModel> get(String userId) {
        Optional<UserEntity> user = this.userRepository.findById(userId);
        return user.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(user, UserServiceModel.class));
    }

    @Override
    public ResponseModel<UserServiceModel> createPassword(String userId, String password, String confirmPassword) {
        return null;
    }

    @Override
    public Optional<UserServiceModel> getByEmail(String email) {
        Optional<UserEntity> user = this.userRepository.findByEmail(email);
        return user.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(user, UserServiceModel.class));
    }

    @Override
    public Optional<UserServiceModel> getByPhoneNumber(String phoneNumber) {
        Optional<UserEntity> user = this.userRepository.findByPhoneNumber(phoneNumber);
        return user.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(user, UserServiceModel.class));
    }

    @Override
    public boolean notUniqueEmail(String email, String id) {
        Optional<UserServiceModel> user = this.getByEmail(email);
        return user.isPresent() && !user.get().getId().equals(id);
    }

    @Override
    public boolean notUniquePhoneNumber(String phoneNumber, String id) {
        Optional<UserServiceModel> user = this.getByPhoneNumber(phoneNumber);
        return user.isPresent() && !user.get().getId().equals(id);
    }

}
