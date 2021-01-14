package com.syn.domo.service.impl;

import com.syn.domo.error.ErrorContainer;
import com.syn.domo.model.view.ResponseModel;
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

import static com.syn.domo.common.DefaultParamValues.EMPTY_URL;
import static com.syn.domo.common.ExceptionErrorMessages.*;
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
    public Set<ApartmentServiceModel> getAll(String buildingId) {

        Set<ApartmentServiceModel> apartmentServiceModels;

        if (buildingId.equals(EMPTY_URL)) {

            apartmentServiceModels = this.apartmentRepository.findAll().stream()
                    .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                    .collect(Collectors.toUnmodifiableSet());
        } else {

            if (this.buildingService.get(buildingId).isEmpty()) {
                throw new EntityNotFoundException(BUILDING_NOT_FOUND);
            }

            apartmentServiceModels =
                    this.apartmentRepository.getAllByBuildingId(buildingId).stream()
                            .map(apartment -> this.modelMapper.map(apartment, ApartmentServiceModel.class))
                            .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return Collections.unmodifiableSet(apartmentServiceModels);
    }

    @Override
    public Optional<ApartmentServiceModel> get(String apartmentId) {
        Optional<Apartment> apartment = this.apartmentRepository.findById(apartmentId);
        return apartment.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(apartment.get(), ApartmentServiceModel.class));
    }

    @Override
    public ResponseModel<ApartmentServiceModel> add(ApartmentServiceModel apartmentServiceModel,
                                     String buildingId) {

        if (!this.validationUtil.isValid(apartmentServiceModel)) {
            return new ResponseModel<>(apartmentServiceModel,
                    this.validationUtil.violations(apartmentServiceModel));
        }

        BuildingServiceModel buildingServiceModel = this.buildingService.get(buildingId)
                .orElseThrow(() -> { throw new EntityNotFoundException(BUILDING_NOT_FOUND); });

        String apartmentNumber = apartmentServiceModel.getNumber();
        if (this.alreadyExists(apartmentNumber, buildingId)) {
            throw new EntityExistsException(String.format(APARTMENT_EXISTS,
                     apartmentNumber, buildingServiceModel.getName()));
        }

        if (apartmentServiceModel.getFloor() > buildingServiceModel.getFloors()) {
            return new ResponseModel<>(apartmentServiceModel, new ErrorContainer(
                    Map.of("floor", Set.of(FLOOR_INVALID))));
        }

        Apartment apartment = this.modelMapper.map(apartmentServiceModel, Apartment.class);
        apartment.setAddedOn(LocalDate.now());
        apartment.setBuilding(this.modelMapper.map(buildingServiceModel, Building.class));

        this.apartmentRepository.saveAndFlush(apartment);

       return new ResponseModel<>(apartment.getId(),
               this.modelMapper.map(apartment, ApartmentServiceModel.class));
    }

    @Override
    public ResponseModel<ApartmentServiceModel> edit(ApartmentServiceModel apartmentServiceModel,
                                                     String apartmentId) {

        if (!this.validationUtil.isValid(apartmentServiceModel)) {
            return new ResponseModel<>(apartmentServiceModel,
                    this.validationUtil.violations(apartmentServiceModel));
        }

        Apartment apartment =
                this.apartmentRepository.findById(apartmentId)
                        .orElseThrow(() -> { throw new EntityNotFoundException(APARTMENT_NOT_FOUND); });

        Building building = apartment.getBuilding();

        if (apartmentServiceModel.getFloor() > building.getFloors()) {
            return new ResponseModel<>(apartmentServiceModel, new ErrorContainer(
                    Map.of("floor", Set.of(FLOOR_INVALID))));
        }

        String newNumber = apartmentServiceModel.getNumber();
        Optional<Apartment> duplicateApartment = this.apartmentRepository
                .getDuplicateApartment(newNumber, building.getId(), apartmentId);

        if (duplicateApartment.isPresent()) {
            throw new EntityExistsException(String.format(APARTMENT_EXISTS,
                    newNumber, building.getName()));
        }

        apartment.setNumber(apartmentServiceModel.getNumber());
        apartment.setFloor(apartmentServiceModel.getFloor());
        apartment.setPets(apartmentServiceModel.getPets());

        this.apartmentRepository.saveAndFlush(apartment);

        return new ResponseModel<>(apartment.getId(),
                this.modelMapper.map(apartment, ApartmentServiceModel.class));
    }

    @Override
    @Transactional
    public void deleteAll(String buildingId) {

        Set<Apartment> apartments;

        if (buildingId.equals(EMPTY_URL)) {
            apartments = new HashSet<>(this.apartmentRepository.findAll());
        } else {

            if (this.buildingService.get(buildingId).isEmpty()) {
                throw new EntityNotFoundException(BUILDING_NOT_FOUND);
            }

            apartments = this.apartmentRepository.getAllByBuildingId(buildingId);
        }

        for (Apartment apartment : apartments) {
            this.childService.deleteAll(buildingId, apartment.getId(), EMPTY_URL);
            this.residentService.deleteAll(buildingId, apartment.getId());
        }

        this.apartmentRepository.deleteAll(apartments);
    }

    @Override
    public void delete(String apartmentId) {

        Apartment apartment = this.apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> { throw new EntityNotFoundException(APARTMENT_NOT_FOUND); });

        String buildingId = apartment.getBuilding().getId();

        this.residentService.deleteAll(buildingId, apartment.getId());
        this.childService.deleteAll(buildingId, apartment.getId(), EMPTY_URL);
        this.apartmentRepository.delete(apartment);
    }

    @Override
    public void emptyApartments(String buildingId) {
        Set<Apartment> apartments =
                this.apartmentRepository.getAllByBuildingId(buildingId);

        for (Apartment apartment : apartments) {
            this.residentService.deleteAll(buildingId, apartment.getId());
        }
    }

    @Override
    public Optional<ApartmentServiceModel> getByIdAndBuildingId(String apartmentId, String buildingId1) {

        Optional<Apartment> apartment = this.apartmentRepository
                .findByIdAndBuildingId(apartmentId, buildingId1);

        return apartment.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(apartment.get(), ApartmentServiceModel.class));
    }

    private boolean alreadyExists(String apartmentNumber, String buildingId) {
        return this.apartmentRepository
                .findByNumberAndBuildingId(apartmentNumber, buildingId).isPresent();

    }
}
