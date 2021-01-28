package com.syn.domo.service.impl;

import com.syn.domo.error.ErrorContainer;
import com.syn.domo.exception.DomoEntityNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.*;
import com.syn.domo.model.service.*;
import com.syn.domo.repository.UserRepository;
import com.syn.domo.service.RoleService;
import com.syn.domo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.syn.domo.common.ExceptionErrorMessages.UNPROCESSABLE_ENTITY;
import static com.syn.domo.common.ExceptionErrorMessages.USER_NOT_FOUND;
import static com.syn.domo.common.ValidationErrorMessages.PASSWORDS_DONT_MATCH;

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
                : Optional.of(this.modelMapper.map(user.get(), UserServiceModel.class));
    }

    @Override
    public void createPassword(String userId, UserActivateServiceModel userActivateServiceModel) {
        String password = userActivateServiceModel.getPassword().trim();
        String confirmPassword = userActivateServiceModel.getConfirmPassword().trim();

        if (!password.equals(confirmPassword)) {
            throw new UnprocessableEntityException(UNPROCESSABLE_ENTITY,
                    new ErrorContainer(Map.of("password", Set.of(PASSWORDS_DONT_MATCH))));
        }

        UserEntity user = this.userRepository.findById(userId).orElseThrow(() -> {
            throw new DomoEntityNotFoundException(USER_NOT_FOUND);
        });

        // TODO: encode password with BCryptPasswordEncoder
        user.setPassword(password);
        user.setActive(true);
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public Optional<UserServiceModel> getByEmail(String email) {
        Optional<UserEntity> user = this.userRepository.findByEmail(email);
        return user.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(user.get(), UserServiceModel.class));
    }

    @Override
    public Optional<UserServiceModel> getByPhoneNumber(String phoneNumber) {
        Optional<UserEntity> user = this.userRepository.findByPhoneNumber(phoneNumber);
        return user.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(user.get(), UserServiceModel.class));
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
