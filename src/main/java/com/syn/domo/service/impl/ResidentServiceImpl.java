package com.syn.domo.service.impl;

import com.syn.domo.error.ErrorContainer;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.*;
import com.syn.domo.model.service.*;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.notification.service.NotificationService;
import com.syn.domo.repository.ResidentRepository;
import com.syn.domo.service.*;
import com.syn.domo.web.filter.ResidentFilter;
import com.syn.domo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.EMAIL_ALREADY_USED;
import static com.syn.domo.common.ValidationErrorMessages.PHONE_ALREADY_USED;

@Service
public class ResidentServiceImpl implements ResidentService  {

    private static final Logger log = LoggerFactory.getLogger(ResidentServiceImpl.class);

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
    public ResponseModel<ResidentServiceModel> add(ResidentServiceModel residentToAdd,
                                                   String buildingId, String apartmentId) throws MessagingException {

        if (!this.validationUtil.isValid(residentToAdd)) {
            return new ResponseModel<>(residentToAdd,
                    this.validationUtil.violations(residentToAdd));
        }

        if (this.buildingService.get(buildingId).isEmpty()) {
            throw new EntityNotFoundException(BUILDING_NOT_FOUND);
        }

        ApartmentServiceModel apartmentServiceModel = this.apartmentService
                .getByIdAndBuildingId(apartmentId, buildingId)
                .orElseThrow(() -> { throw new EntityNotFoundException(APARTMENT_NOT_FOUND); });

        String email = residentToAdd.getEmail();
        if (this.userService.getByEmail(email).isPresent()) {
            return new ResponseModel<>(residentToAdd,
                    new ErrorContainer(Map.of("email",
                            Set.of(String.format(EMAIL_ALREADY_USED, email)))
            ));
        }

        String phoneNumber = residentToAdd.getPhoneNumber();
        if (this.userService.getByPhoneNumber(phoneNumber).isPresent()) {
            return new ResponseModel<>(residentToAdd,
                    new ErrorContainer(Map.of("phoneNumber",
                            Set.of(String.format(PHONE_ALREADY_USED, phoneNumber)))
                    ));
        }

        RoleServiceModel roleServiceModel = this.roleService
                .getByName(UserRole.RESIDENT)
                .orElseThrow(() -> { throw new EntityNotFoundException(ROLE_NOT_FOUND); });

        Resident resident = this.modelMapper.map(residentToAdd, Resident.class);

        resident.setRoles(Set.of(this.modelMapper.map(roleServiceModel, Role.class)));
        resident.setAddedOn(LocalDate.now());
        resident.setApartments(Set.of(this.modelMapper.map(apartmentServiceModel, Apartment.class)));

        this.residentRepository.saveAndFlush(resident);

        ResidentServiceModel addedResident =
                this.modelMapper.map(resident, ResidentServiceModel.class);

        try {
            this.notificationService.sendActivationEmail(addedResident);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }


        return new ResponseModel<>(resident.getId(), addedResident);
    }

    @Override
    public ResponseModel<ResidentServiceModel> edit(ResidentServiceModel residentServiceModel,
                                                    String residentId) {

        if (!this.validationUtil.isValid(residentServiceModel)) {
            return new ResponseModel<>(residentServiceModel,
                    this.validationUtil.violations(residentServiceModel));
        }

        if (this.userService.notUniqueEmail(residentServiceModel.getEmail(), residentId)) {
            return new ResponseModel<>(residentServiceModel,
                    new ErrorContainer(Map.of("email",
                            Set.of(String.format(EMAIL_ALREADY_USED,
                                    residentServiceModel.getEmail())))
                    ));
        }

        if (this.userService.notUniquePhoneNumber(residentServiceModel.getPhoneNumber(), residentId)) {
            return new ResponseModel<>(residentServiceModel,
                    new ErrorContainer(Map.of("phoneNumber",
                            Set.of(String.format(PHONE_ALREADY_USED,
                                    residentServiceModel.getPhoneNumber())))
                    ));
        }

        Resident resident = this.residentRepository.findById(residentId)
                .orElseThrow(() -> { throw new EntityNotFoundException(RESIDENT_NOT_FOUND); });

        resident.setFirstName(residentServiceModel.getFirstName());
        resident.setLastName(residentServiceModel.getLastName());
        resident.setEmail(residentServiceModel.getEmail());
        resident.setPhoneNumber(residentServiceModel.getPhoneNumber());

        this.residentRepository.saveAndFlush(resident);

        return new ResponseModel<>(resident.getId(),
                this.modelMapper.map(resident, ResidentServiceModel.class));
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
                .orElseThrow(() -> { throw new EntityNotFoundException(RESIDENT_NOT_FOUND); });

        this.residentRepository.delete(resident);
    }

    @Override
    public Set<ResidentServiceModel> getAllByIdIn(Set<String> ids) {

        return this.residentRepository.findAllByIdIn(ids).stream()
                        .map(r -> this.modelMapper.map(r, ResidentServiceModel.class))
                        .collect(Collectors.toUnmodifiableSet());
    }
}
