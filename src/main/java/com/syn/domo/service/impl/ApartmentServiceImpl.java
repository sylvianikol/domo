package com.syn.domo.service.impl;

import com.syn.domo.exception.error.ErrorContainer;
import com.syn.domo.exception.DataConflictException;
import com.syn.domo.exception.DomoEntityNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.service.*;
import com.syn.domo.web.filter.ApartmentFilter;
import com.syn.domo.utils.ValidationUtil;
import com.syn.domo.web.filter.ChildFilter;
import com.syn.domo.web.filter.ResidentFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.DefaultParamValues.NULL;
import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.*;

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
    public Set<ApartmentServiceModel> getAll(ApartmentFilter apartmentFilter, Pageable pageable) {

        LinkedHashSet<ApartmentServiceModel> apartments = this.apartmentRepository
                .findAll(apartmentFilter, pageable)
                .getContent().stream()
                .map(a -> this.modelMapper.map(a, ApartmentServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(apartments);
    }

    @Override
    public Set<ApartmentServiceModel> getAll() {
        return this.apartmentRepository.findAll().stream()
                .map(a -> this.modelMapper.map(a, ApartmentServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Optional<ApartmentServiceModel> get(String apartmentId) {
        Optional<Apartment> apartment = this.apartmentRepository.findById(apartmentId);
        return apartment.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(apartment.get(), ApartmentServiceModel.class));
    }

    @Override
    public ApartmentServiceModel add(ApartmentServiceModel apartmentToAdd,
                                     String buildingId) {

        if (!this.validationUtil.isValid(apartmentToAdd)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED,
                    this.validationUtil.getViolations(apartmentToAdd));
        }

        BuildingServiceModel buildingServiceModel = this.buildingService.get(buildingId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(BUILDING_NOT_FOUND); });

        String apartmentNumber = apartmentToAdd.getNumber();
        if (this.alreadyExistsApartmentNumber(apartmentNumber, buildingId)) {
            throw new DataConflictException(DATA_CONFLICT,
                    new ErrorContainer(Map.of("number", Set.of(
                            String.format(APARTMENT_EXISTS, apartmentNumber, buildingServiceModel.getName())))));

        }

        if (apartmentToAdd.getFloor() > buildingServiceModel.getFloors()) {
            throw new UnprocessableEntityException(UNPROCESSABLE_ENTITY,
                    new ErrorContainer(Map.of("floor", Set.of(FLOOR_INVALID))));
        }

        Apartment apartment = this.modelMapper.map(apartmentToAdd, Apartment.class);
        apartment.setAddedOn(LocalDate.now());
        apartment.setBuilding(this.modelMapper.map(buildingServiceModel, Building.class));

        this.apartmentRepository.saveAndFlush(apartment);

       return this.modelMapper.map(apartment, ApartmentServiceModel.class);
    }

    @Override
    public ApartmentServiceModel edit(ApartmentServiceModel apartmentToEdit, String apartmentId) {

        if (!this.validationUtil.isValid(apartmentToEdit)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED,
                    this.validationUtil.getViolations(apartmentToEdit));
        }

        Apartment apartment = this.apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(APARTMENT_NOT_FOUND); });

        Building building = apartment.getBuilding();

        if (apartmentToEdit.getFloor() > building.getFloors()) {
            throw new UnprocessableEntityException(UNPROCESSABLE_ENTITY,
                    new ErrorContainer(Map.of("floor", Set.of(FLOOR_INVALID))));
        }

        String newNumber = apartmentToEdit.getNumber();
        Optional<Apartment> duplicateApartment = this.apartmentRepository
                .getDuplicateApartment(newNumber, building.getId(), apartmentId);

        if (duplicateApartment.isPresent()) {
            throw new DataConflictException(DATA_CONFLICT, new ErrorContainer(Map.of("duplicate",
                    Set.of(String.format(APARTMENT_EXISTS, newNumber, building.getName())))));
        }

        apartment.setNumber(apartmentToEdit.getNumber());
        apartment.setFloor(apartmentToEdit.getFloor());
        apartment.setPets(apartmentToEdit.getPets());

        this.apartmentRepository.saveAndFlush(apartment);

        return this.modelMapper.map(apartment, ApartmentServiceModel.class);
    }

    @Override
    @Transactional
    public int deleteAll(ApartmentFilter apartmentFilter) {

        List<Apartment> apartments = this.apartmentRepository
                .findAll(apartmentFilter);

        for (Apartment apartment : apartments) {
            String buildingId = apartment.getBuilding().getId();
            this.childService.deleteAll(new ChildFilter(buildingId, apartment.getId(), NULL));
            this.residentService.deleteAll(new ResidentFilter(buildingId, apartment.getId()));
        }

        this.apartmentRepository.deleteAll(apartments);
        return apartments.size();
    }

    @Override
    public void delete(String apartmentId) {

        Apartment apartment = this.apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(APARTMENT_NOT_FOUND); });

        String buildingId = apartment.getBuilding().getId();

        this.residentService.deleteAll(new ResidentFilter(buildingId, apartment.getId()));
        this.childService.deleteAll(new ChildFilter(buildingId, apartment.getId(), NULL));
        this.apartmentRepository.delete(apartment);
    }

    @Override
    public void evacuateApartments(String buildingId) {
        Set<Apartment> apartments =
                this.apartmentRepository.findAllByBuildingId(buildingId);

        for (Apartment apartment : apartments) {
            this.residentService.deleteAll(new ResidentFilter(buildingId, apartment.getId()));
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

    private boolean alreadyExistsApartmentNumber(String apartmentNumber, String buildingId) {
        return this.apartmentRepository
                .findByNumberAndBuildingId(apartmentNumber, buildingId).isPresent();

    }
}
