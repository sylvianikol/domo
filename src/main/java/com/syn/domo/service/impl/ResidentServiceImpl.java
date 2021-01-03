package com.syn.domo.service.impl;

import com.syn.domo.exception.ApartmentNotFoundException;
import com.syn.domo.exception.BuildingNotFoundException;
import com.syn.domo.exception.RoleNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Resident;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.*;
import com.syn.domo.repository.ResidentRepository;
import com.syn.domo.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResidentServiceImpl implements ResidentService  {

    private final ResidentRepository residentRepository;
    private final UserService userService;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    public ResidentServiceImpl(ResidentRepository residentRepository, UserService userService, BuildingService buildingService, ApartmentService apartmentService, RoleService roleService, ModelMapper modelMapper) {
        this.residentRepository = residentRepository;
        this.userService = userService;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResidentServiceModel add(UserServiceModel userServiceModel, String buildingId, String apartmentId) {

        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);

        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        if (this.userService.getByEmail(userServiceModel.getEmail()).isPresent()) {
            throw new UnprocessableEntityException(
                    String.format("Email '%s' is already used by another user!",
                            userServiceModel.getEmail()));
        }

        Optional<RoleServiceModel> roleServiceModel =
                this.roleService.getByName(UserRole.RESIDENT);

        if (roleServiceModel.isEmpty()) {
            throw new RoleNotFoundException("Role not found");
        }

        Resident resident = this.modelMapper.map(userServiceModel, Resident.class);

        resident.setRoles(Set.of(this.modelMapper.map(roleServiceModel.get(), Role.class)));

        resident.setAddedOn(LocalDate.now());

        resident.setApartments(Set.of(this.modelMapper.map(apartment.get(), Apartment.class)));

        this.residentRepository.saveAndFlush(resident);

        return this.modelMapper.map(resident, ResidentServiceModel.class);
    }
}
