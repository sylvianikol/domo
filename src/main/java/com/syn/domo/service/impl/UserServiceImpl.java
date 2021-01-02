package com.syn.domo.service.impl;

import com.syn.domo.exception.*;
import com.syn.domo.model.entity.*;
import com.syn.domo.model.service.*;
import com.syn.domo.repository.UserRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.RoleService;
import com.syn.domo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BuildingService buildingService, ApartmentService apartmentService, RoleService roleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserServiceModel add(UserServiceModel userServiceModel, String buildingId, String apartmentId) {
        // TODO: validation

        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);

        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        if (this.userRepository.findByEmail(userServiceModel.getEmail()).isPresent()) {
            throw new UnprocessableEntityException(
                    String.format("Email '%s' is already used by another user!",
                            userServiceModel.getEmail()));
        }


        UserEntity user = this.modelMapper.map(userServiceModel, UserEntity.class);

        Optional<RoleServiceModel> roleServiceModel =
                this.roleService.getByName(UserRole.RESIDENT);

        if (roleServiceModel.isEmpty()) {
            throw new RoleNotFoundException("Role not found!");
        }

        Role role = this.modelMapper.map(roleServiceModel.get(), Role.class);
        user.setRoles(Set.of(role));

        user.setAddedOn(LocalDate.now());
        user.setApartment(this.modelMapper.map(apartment.get(), Apartment.class));

        this.userRepository.saveAndFlush(user);

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel edit(UserServiceModel userServiceModel, String buildingId, String apartmentId) {
        // TODO: validation
        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        if (this.notUniqueEmail(userServiceModel.getEmail(), userServiceModel.getId())) {
            throw new UnprocessableEntityException(
                    String.format("Email '%s' is already used by another user!",
                            userServiceModel.getEmail()));
        }

        Set<RoleServiceModel> newRoles = userServiceModel.getRoles();
        for (RoleServiceModel newRole : newRoles) {
            if (this.roleService.getByName(newRole.getName()).isEmpty()) {
                throw new RoleNotFoundException(
                        String.format("Role '%s' not found!", newRole.getName().toString()));
            }
        }

        UserEntity user = this.userRepository.findById(userServiceModel.getId()).orElse(null);

        if (user != null && user.getApartment().getId().equals(apartment.get().getId())) {

            user.setFirstName(userServiceModel.getFirstName());
            user.setLastName(userServiceModel.getLastName());
            user.setEmail(userServiceModel.getEmail());
            user.setPhoneNumber(userServiceModel.getPhoneNumber());
            user.setAddedOn(userServiceModel.getAddedOn());

            Set<Role> roles = newRoles.stream()
                    .map(roleServiceModel -> this.modelMapper.map(roleServiceModel, Role.class))
                    .collect(Collectors.toUnmodifiableSet());

            user.setRoles(roles);

            this.userRepository.saveAndFlush(user);

        } else {
            throw new UserNotFoundException("User not found!");
        }

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public void delete(String residentId, String buildingId, String apartmentId) {
        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        UserEntity user = this.userRepository.findById(residentId).orElse(null);

        if (user != null && user.getApartment().getId().equals(apartment.get().getId())) {
            this.userRepository.delete(user);
        } else {
            throw new UserNotFoundException("User not found!");
        }
    }

    @Override
    public void initAdmin() {
        if (this.userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail("admin@domo.bg");
            admin.setPassword("123");
            admin.setPhoneNumber("0888147384573");
            admin.setAddedOn(LocalDate.now());

            Set<Role> roles = this.roleService.getAll();
            roles.forEach(role -> role.getUsers().add(admin));
            admin.setRoles(roles);

            this.userRepository.saveAndFlush(admin);
        }
    }

    @Override
    public void deleteAllByApartmentId(String buildingId, String apartmentId) {
        Set<UserEntity> users = this.userRepository
                .getAllByApartmentIdAndBuildingId(buildingId, apartmentId);
        this.userRepository.deleteAll(users);
    }

    @Override
    public Set<UserServiceModel> getAllByApartmentIdAndBuildingId(String buildingId, String apartmentId) {
        Set<UserServiceModel> userServiceModels =
                this.userRepository
                        .getAllByApartmentIdAndBuildingId(buildingId, apartmentId)
                        .stream()
                        .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(userServiceModels);
    }

    @Override
    public Set<UserServiceModel> getAllById(Set<String> ids) {
        Set<UserServiceModel> userServiceModels =
                this.userRepository.findAllByIdIn(ids).stream()
                        .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(userServiceModels);
    }

    @Override
    public Optional<UserServiceModel> getById(String residentId) {
        Optional<UserEntity> user = this.userRepository.findById(residentId);
        return user.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(user.get(), UserServiceModel.class));
    }


    private boolean notUniqueEmail(String email, String residentId) {
        Optional<UserEntity> user = this.userRepository.findByEmail(email);
        return user.isPresent() && !user.get().getId().equals(residentId);
    }

}
