package com.syn.domo.service.impl;

import com.syn.domo.common.ExceptionErrorMessages;
import com.syn.domo.error.ErrorContainer;
import com.syn.domo.exception.*;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Resident;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.*;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.repository.ResidentRepository;
import com.syn.domo.service.*;
import com.syn.domo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.EMAIL_ALREADY_USED;

@Service
public class ResidentServiceImpl implements ResidentService  {

    private final ResidentRepository residentRepository;
    private final UserService userService;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final ChildService childService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public ResidentServiceImpl(ResidentRepository residentRepository,
                               UserService userService,
                               BuildingService buildingService,
                               ApartmentService apartmentService,
                               ChildService childService,
                               RoleService roleService,
                               ModelMapper modelMapper,
                               ValidationUtil validationUtil) {
        this.residentRepository = residentRepository;
        this.userService = userService;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.childService = childService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Set<ResidentServiceModel> getAll(String buildingId, String apartmentId) {
        Set<ResidentServiceModel> residents = this.residentRepository
                .getAllByBuildingIdAndApartmentId(buildingId, apartmentId)
                .stream()
                .map(r -> this.modelMapper.map(r, ResidentServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(residents);
    }

    @Override
    public Optional<ResidentServiceModel> get(String buildingId, String apartmentId, String residentId) {
        Optional<Resident> resident = this.residentRepository
                .getOneByIdAndBuildingIdAndApartmentId(residentId, buildingId, apartmentId);

        return resident.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(resident.get(), ResidentServiceModel.class));
    }

    @Override
    public ResponseModel<ResidentServiceModel> add(ResidentServiceModel residentServiceModel, String buildingId, String apartmentId) {

        if (!this.validationUtil.isValid(residentServiceModel)) {
            return new ResponseModel<>(residentServiceModel,
                    this.validationUtil.violations(residentServiceModel));
        }

        if (this.buildingService.get(buildingId).isEmpty()) {
            throw new EntityNotFoundException(BUILDING_NOT_FOUND);
        }

        ApartmentServiceModel apartmentServiceModel = this.apartmentService
                .getByIdAndBuildingId(apartmentId, buildingId)
                .orElseThrow(() -> { throw new EntityNotFoundException(APARTMENT_NOT_FOUND); });

        if (this.userService.getByEmail(residentServiceModel.getEmail()).isPresent()) {
            return new ResponseModel<>(residentServiceModel,
                    new ErrorContainer(Map.of("email",
                            Set.of(String.format(EMAIL_ALREADY_USED,
                                    residentServiceModel.getEmail())))
            ));
        }

        RoleServiceModel roleServiceModel = this.roleService
                .getByName(UserRole.RESIDENT)
                .orElseThrow(() -> { throw new EntityNotFoundException(ROLE_NOT_FOUND); });

        Resident resident = this.modelMapper.map(residentServiceModel, Resident.class);

        resident.setRoles(Set.of(this.modelMapper.map(roleServiceModel, Role.class)));
        resident.setAddedOn(LocalDate.now());
        resident.setApartments(Set.of(this.modelMapper.map(apartmentServiceModel, Apartment.class)));

        this.residentRepository.saveAndFlush(resident);

        // TODO: Send email with link to create password

        return new ResponseModel<>(resident.getId(),
                this.modelMapper.map(resident, ResidentServiceModel.class));
    }

    @Override
    public ResidentServiceModel edit(ResidentServiceModel residentServiceModel,
                                     String buildingId, String apartmentId, String residentId) {
        // TODO: validation

        Optional<BuildingServiceModel> building = this.buildingService.get(buildingId);
        if (building.isEmpty()) {
            throw new EntityNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.get(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new EntityNotFoundException("Apartment not found!");
        }

        if (this.userService.notUniqueEmail(residentServiceModel.getEmail(), residentId)) {
            throw new UnprocessableEntityException(
                    String.format("Email '%s' is already used by another user!",
                            residentServiceModel.getEmail()));
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

            resident.setFirstName(residentServiceModel.getFirstName());
            resident.setLastName(residentServiceModel.getLastName());
            resident.setEmail(residentServiceModel.getEmail());
            resident.setPhoneNumber(residentServiceModel.getPhoneNumber());

            this.residentRepository.saveAndFlush(resident);

        } else {
            throw new EntityNotFoundException("Resident not found");
        }

        return this.modelMapper.map(resident, ResidentServiceModel.class);
    }

    @Override
    public void deleteAll(String buildingId, String apartmentId) {
        Optional<BuildingServiceModel> building = this.buildingService.get(buildingId);
        if (building.isEmpty()) {
            throw new EntityNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.get(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new EntityNotFoundException("Apartment not found!");
        }

        this.childService.deleteAll(buildingId, apartmentId);
        Set<Resident> residents = this.residentRepository
                .getAllByBuildingIdAndApartmentId(buildingId, apartmentId);
        this.residentRepository.deleteAll(residents);
    }

    @Override
    public void delete(String buildingId, String apartmentId, String residentId) {
        Optional<BuildingServiceModel> building = this.buildingService.get(buildingId);
        if (building.isEmpty()) {
            throw new EntityNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.get(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new EntityNotFoundException("Apartment not found!");
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
    public Set<ResidentServiceModel> getAllByIdIn(Set<String> ids) {
        Set<ResidentServiceModel> residentServiceModels =
                this.residentRepository.findAllByIdIn(ids).stream()
                        .map(r -> this.modelMapper.map(r, ResidentServiceModel.class))
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(residentServiceModels);
    }
}
