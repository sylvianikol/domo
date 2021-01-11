package com.syn.domo.service.impl;

import com.syn.domo.error.ErrorContainer;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.exception.*;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.service.*;
import com.syn.domo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.ExceptionErrorMessages.APARTMENT_EXISTS;
import static com.syn.domo.common.ExceptionErrorMessages.BUILDING_NOT_FOUND;
import static com.syn.domo.common.ValidationErrorMessages.FLOOR_INVALID;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final BuildingService buildingService;
    private final ResidentService residentService;
    private final ChildService childService;
    private final ModelMapper modelMapper;
    public final ValidationUtil validationUtil;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository,
                                @Lazy BuildingService buildingService,
                                @Lazy ResidentService residentService,
                                @Lazy ChildService childService,
                                ModelMapper modelMapper,
                                ValidationUtil validationUtil) {
        this.apartmentRepository = apartmentRepository;
        this.buildingService = buildingService;
        this.residentService = residentService;
        this.childService = childService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public ResponseModel<ApartmentServiceModel> add(ApartmentServiceModel apartmentServiceModel,
                                     String buildingId) {

        if (!this.validationUtil.isValid(apartmentServiceModel)) {
            return new ResponseModel<>(apartmentServiceModel,
                    this.validationUtil.violations(apartmentServiceModel));
        }

        Optional<BuildingServiceModel> buildingOpt =
                this.buildingService.get(buildingId);

        if (buildingOpt.isEmpty()) {
            throw new EntityNotFoundException(BUILDING_NOT_FOUND);
        }

        String apartmentNumber = apartmentServiceModel.getNumber();
        if (this.alreadyExists(apartmentNumber, buildingId)) {
            throw new EntityExistsException(String.format(APARTMENT_EXISTS,
                     apartmentNumber, buildingOpt.get().getName()));
        }

        if (apartmentServiceModel.getFloor() > buildingOpt.get().getFloors()) {
            return new ResponseModel<>(apartmentServiceModel, new ErrorContainer(
                    Map.of("floor", Set.of(FLOOR_INVALID))));
        }

        Apartment apartment = this.modelMapper.map(apartmentServiceModel, Apartment.class);
        apartment.setAddedOn(LocalDate.now());
        apartment.setBuilding(this.modelMapper.map(buildingOpt.get(), Building.class));

        this.apartmentRepository.saveAndFlush(apartment);

       return new ResponseModel<>(apartment.getId(), this.modelMapper.map(apartment, ApartmentServiceModel.class));
    }

    @Override
    public ApartmentServiceModel edit(ApartmentServiceModel apartmentServiceModel,
                                      String buildingId, String apartmentId) {
        // TODO: validation

        Optional<BuildingServiceModel> buildingOpt = this.buildingService.get(buildingId);
        if (buildingOpt.isEmpty()) {
            throw new EntityNotFoundException("Building not found!");
        }

        if (apartmentServiceModel.getFloor() > buildingOpt.get().getFloors()) {
            throw new UnprocessableEntityException("Invalid floor number!");
        }

        Apartment apartment =
                this.apartmentRepository.findById(apartmentId).orElse(null);

        if (apartment == null || !apartment.getBuilding().getId().equals(buildingId)) {
            throw new EntityNotFoundException("Apartment not found!");
        }

        String newNumber = apartmentServiceModel.getNumber();
        Optional<Apartment> existingApartment = this.apartmentRepository
                .findByNumberAndBuilding_Id(newNumber, buildingId);

        if (!apartment.getNumber().equals(newNumber) && existingApartment.isPresent()) {
            throw new EntityExistsException(
                    String.format("Apartment No:%s already exists in this building!",
                            apartmentServiceModel.getNumber()));
        }

        apartment.setNumber(apartmentServiceModel.getNumber());
        apartment.setFloor(apartmentServiceModel.getFloor());
        apartment.setPets(apartmentServiceModel.getPets());

        this.apartmentRepository.saveAndFlush(apartment);
        return this.modelMapper.map(apartment, ApartmentServiceModel.class);
    }

    @Override
    @Transactional
    public void deleteAll(String buildingId) {

        if (this.buildingService.get(buildingId).isEmpty()) {
            throw new EntityNotFoundException("Building not found!");
        }

        Set<Apartment> apartments =
                this.apartmentRepository.getAllByBuildingId(buildingId);

        for (Apartment apartment : apartments) {
            this.childService.deleteAll(buildingId, apartment.getId());
            this.residentService.deleteAll(buildingId, apartment.getId());
        }

        this.apartmentRepository.deleteAll(apartments);
    }

    @Override
    public void delete(String apartmentId, String buildingId) {

        if (this.buildingService.get(buildingId).isEmpty()) {
            throw new EntityNotFoundException("Building not found!");
        }

        Apartment apartment =
                this.apartmentRepository.findById(apartmentId).orElse(null);

        if (apartment == null || !apartment.getBuilding().getId().equals(buildingId)) {
            throw new EntityNotFoundException("Apartment not found!");
        }

        this.residentService.deleteAll(buildingId, apartment.getId());
        this.childService.deleteAll(buildingId, apartment.getId());
        this.apartmentRepository.delete(apartment);
    }

    @Override
    public Optional<ApartmentServiceModel> getByIdIn(String id, Set<String> apartmentIds) {
        Optional<Apartment> apartment = this.apartmentRepository.getByIdIn(id, apartmentIds);
        return apartment.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(apartment, ApartmentServiceModel.class));
    }

    @Override
    public void emptyApartments(String buildingId) {
        Set<Apartment> apartments = this.apartmentRepository.getAllByBuildingId(buildingId);

        for (Apartment apartment : apartments) {
            this.residentService.deleteAll(buildingId, apartment.getId());
        }
    }

    @Override
    public Set<ApartmentServiceModel> getAll(String buildingId) {

        Set<ApartmentServiceModel> apartmentServiceModels =
                this.apartmentRepository.getAllByBuildingId(buildingId).stream()
                .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(apartmentServiceModels);
    }

    @Override
    public Optional<ApartmentServiceModel> get(String apartmentId) {
        Optional<Apartment> apartment = this.apartmentRepository.findById(apartmentId);
        return apartment.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(apartment.get(), ApartmentServiceModel.class));
    }

    private boolean alreadyExists(String apartmentNumber, String buildingId) {
        return this.apartmentRepository
                .findByNumberAndBuilding_Id(apartmentNumber, buildingId).isPresent();

    }

//    private ApartmentServiceModel collectErrors(ApartmentServiceModel apartmentServiceModel,
//                               Set<ConstraintViolation<ApartmentServiceModel>> constraintViolations) {
//        for (ConstraintViolation<ApartmentServiceModel> violation : constraintViolations) {
//            Map<String, Set<String>> errors = apartmentServiceModel.getErrorContainer().getErrors();
//            String key = violation.getPropertyPath().toString();
//            String value = violation.getMessage();
//
//            errors.putIfAbsent(key, new HashSet<>());
//            errors.get(key).add(value);
//        }
//
//        return apartmentServiceModel;
//    }

//    private ApartmentServiceModel addError(ApartmentServiceModel apartmentServiceModel,
//                                           String propertyName, String errorMessage) {
//        Map<String, Set<String>> errors = apartmentServiceModel.getErrorContainer().getErrors();
//        errors.putIfAbsent(propertyName, new HashSet<>());
//        errors.get(propertyName).add(errorMessage);
//        return apartmentServiceModel;
//    }
}
