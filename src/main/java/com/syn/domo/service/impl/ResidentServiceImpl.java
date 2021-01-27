package com.syn.domo.service.impl;

import com.syn.domo.error.ErrorContainer;
import com.syn.domo.exception.DomoEntityExistsException;
import com.syn.domo.exception.DomoEntityNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.*;
import com.syn.domo.model.service.*;
import com.syn.domo.notification.service.NotificationService;
import com.syn.domo.repository.ResidentRepository;
import com.syn.domo.service.*;
import com.syn.domo.web.filter.ResidentFilter;
import com.syn.domo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.*;

@Service
public class ResidentServiceImpl implements ResidentService  {

    private final ResidentRepository residentRepository;
    private final UserService userService;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final NotificationService notificationService;

    public ResidentServiceImpl(ResidentRepository residentRepository,
                               UserService userService,
                               BuildingService buildingService,
                               ApartmentService apartmentService,
                               RoleService roleService,
                               ModelMapper modelMapper,
                               ValidationUtil validationUtil,
                               NotificationService notificationService) {
        this.residentRepository = residentRepository;
        this.userService = userService;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.notificationService = notificationService;
    }

    @Override
    public Set<ResidentServiceModel> getAll(ResidentFilter residentFilter, Pageable pageable) {

        Set<ResidentServiceModel> residents = this.residentRepository
                .findAll(residentFilter, pageable)
                .getContent().stream()
                .map(r -> this.modelMapper.map(r, ResidentServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(residents);
    }

    @Override
    public Optional<ResidentServiceModel> get(String residentId) {

        Optional<Resident> resident = this.residentRepository.findById(residentId);

        return resident.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(resident.get(), ResidentServiceModel.class));
    }

    @Override
    public ResidentServiceModel add(ResidentServiceModel residentToAdd,
                                                   String buildingId, String apartmentId) throws MessagingException, InterruptedException {

        if (!this.validationUtil.isValid(residentToAdd)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED,
                    this.validationUtil.getViolations(residentToAdd));
        }

        if (this.buildingService.get(buildingId).isEmpty()) {
            throw new DomoEntityNotFoundException(BUILDING_NOT_FOUND);
        }

        ApartmentServiceModel apartmentServiceModel = this.apartmentService
                .getByIdAndBuildingId(apartmentId, buildingId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(APARTMENT_NOT_FOUND); });

        String email = residentToAdd.getEmail();
        if (this.userService.getByEmail(email).isPresent()) {
            throw new DomoEntityExistsException(ENTITY_EXISTS, new ErrorContainer(Map.of("email",
                    Set.of(String.format(EMAIL_ALREADY_USED, email)))));
        }

        String phoneNumber = residentToAdd.getPhoneNumber();
        if (this.userService.getByPhoneNumber(phoneNumber).isPresent()) {
            throw new DomoEntityExistsException(ENTITY_EXISTS, new ErrorContainer(Map.of("phoneNumber",
                    Set.of(String.format(PHONE_ALREADY_USED, phoneNumber)))));
        }

        RoleServiceModel roleServiceModel = this.roleService
                .getByName(UserRole.RESIDENT)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(ROLE_NOT_FOUND); });

        Resident resident = this.modelMapper.map(residentToAdd, Resident.class);

        resident.setRoles(Set.of(this.modelMapper.map(roleServiceModel, Role.class)));
        resident.setAddedOn(LocalDate.now());
        resident.setApartments(Set.of(this.modelMapper.map(apartmentServiceModel, Apartment.class)));

        this.residentRepository.saveAndFlush(resident);

        ResidentServiceModel addedResident =
                this.modelMapper.map(resident, ResidentServiceModel.class);

        this.notificationService.sendActivationEmail(addedResident);

        return addedResident;
    }

    @Override
    public ResidentServiceModel edit(ResidentServiceModel residentToEdit,
                                                    String residentId) {

        if (!this.validationUtil.isValid(residentToEdit)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED,
                    this.validationUtil.getViolations(residentToEdit));
        }

        String email = residentToEdit.getEmail();
        if (this.userService.notUniqueEmail(email, residentId)) {
            throw new DomoEntityExistsException(ENTITY_EXISTS, new ErrorContainer(Map.of("email",
                    Set.of(String.format(EMAIL_ALREADY_USED, email)))));
        }

        String phoneNumber = residentToEdit.getPhoneNumber();
        if (this.userService.notUniquePhoneNumber(phoneNumber, residentId)) {
            throw new DomoEntityExistsException(ENTITY_EXISTS, new ErrorContainer(Map.of("phoneNumber",
                    Set.of(String.format(PHONE_ALREADY_USED, phoneNumber)))));
        }

        Resident resident = this.residentRepository.findById(residentId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(RESIDENT_NOT_FOUND); });

        resident.setFirstName(residentToEdit.getFirstName());
        resident.setLastName(residentToEdit.getLastName());
        resident.setEmail(residentToEdit.getEmail());
        resident.setPhoneNumber(residentToEdit.getPhoneNumber());

        this.residentRepository.saveAndFlush(resident);

        return this.modelMapper.map(resident, ResidentServiceModel.class);
    }

    @Override
    public int deleteAll(ResidentFilter residentFilter) {

        List<Resident> residents = this.residentRepository.findAll(residentFilter);

        this.residentRepository.deleteAll(residents);

        return residents.size();
    }

    @Override
    public void delete(String residentId) {

        Resident resident = this.residentRepository.findById(residentId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(RESIDENT_NOT_FOUND); });

        this.residentRepository.delete(resident);
    }

    @Override
    public Set<ResidentServiceModel> getAllByIdIn(Set<String> ids) {

        return this.residentRepository.findAllByIdIn(ids).stream()
                        .map(r -> this.modelMapper.map(r, ResidentServiceModel.class))
                        .collect(Collectors.toUnmodifiableSet());
    }
}
