package com.syn.domo.service.impl;

import com.syn.domo.exception.*;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Resident;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.*;
import com.syn.domo.repository.ResidentRepository;
import com.syn.domo.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
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
        // TODO: validation
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

        resident.setRoles(new LinkedHashSet<>());
        resident.getRoles().add(this.modelMapper.map(roleServiceModel.get(), Role.class));

        resident.setAddedOn(LocalDate.now());

        resident.setApartments(Set.of(this.modelMapper.map(apartment.get(), Apartment.class)));

        this.residentRepository.saveAndFlush(resident);

        return this.modelMapper.map(resident, ResidentServiceModel.class);
    }

    @Override
    public ResidentServiceModel edit(UserServiceModel userServiceModel, String buildingId, String apartmentId) {
        // TODO: validation

        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        if (this.userService.notUniqueEmail(userServiceModel.getEmail(), userServiceModel.getId())) {
            throw new UnprocessableEntityException(
                    String.format("Email '%s' is already used by another user!",
                            userServiceModel.getEmail()));
        }

        Resident resident = this.residentRepository.findById(userServiceModel.getId()).orElse(null);

        if (resident != null) {
            Set<String> apartmentIds = resident.getApartments().stream()
                    .map(Apartment::getId)
                    .collect(Collectors.toUnmodifiableSet());

            Optional<ApartmentServiceModel> apartmentServiceModel =
                    this.apartmentService.getByIdIn(apartmentId, apartmentIds);

            if (apartmentServiceModel.isEmpty()) {
                throw new EntityNotFoundException("Apartment not found");
            }

            resident.setFirstName(userServiceModel.getFirstName());
            resident.setLastName(userServiceModel.getLastName());
            resident.setEmail(userServiceModel.getEmail());
            resident.setPhoneNumber(userServiceModel.getPhoneNumber());
            resident.setAddedOn(userServiceModel.getAddedOn());

            this.residentRepository.saveAndFlush(resident);

        } else {
            throw new EntityNotFoundException("Resident not found");
        }

        return this.modelMapper.map(resident, ResidentServiceModel.class);
    }

    @Override
    public void delete(String buildingId, String apartmentId, String residentId) {
        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        Resident resident = this.residentRepository.findById(residentId).orElse(null);

        if (resident != null) {
            Set<String> apartmentIds = resident.getApartments().stream()
                    .map(Apartment::getId)
                    .collect(Collectors.toUnmodifiableSet());

            Optional<ApartmentServiceModel> apartmentServiceModel =
                    this.apartmentService.getByIdIn(apartmentId, apartmentIds);

            if (apartmentServiceModel.isEmpty()) {
                throw new EntityNotFoundException("Apartment not found");
            }

            this.residentRepository.delete(resident);
        } else {
            throw new EntityNotFoundException("Resident not found!");
        }
    }

    @Override
    public Set<ResidentServiceModel> getAllByBuildingIdAndApartmentId(String buildingId, String apartmentId) {
        return this.residentRepository
                .getAllByBuildingIdAndApartmentId(buildingId, apartmentId)
                .stream()
                .map(r -> this.modelMapper.map(r, ResidentServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Optional<ResidentServiceModel> getOne(String buildingId, String apartmentId, String residentId) {
        Optional<Resident> resident = this.residentRepository
                .getOneByIdAndBuildingIdAndApartmentId(residentId, buildingId, apartmentId);

        return resident.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(resident.get(), ResidentServiceModel.class));
    }
}
