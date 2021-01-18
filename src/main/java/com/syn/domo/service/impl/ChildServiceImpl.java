package com.syn.domo.service.impl;

import com.syn.domo.exception.*;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Child;
import com.syn.domo.model.entity.Resident;
import com.syn.domo.model.service.*;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.repository.ChildRepository;
import com.syn.domo.service.*;
import com.syn.domo.web.filter.ChildFilter;
import com.syn.domo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.ExceptionErrorMessages.*;

@Service
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final ResidentService residentService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public ChildServiceImpl(ChildRepository childRepository,
                            BuildingService buildingService,
                            ApartmentService apartmentService,
                            @Lazy ResidentService residentService,
                            ModelMapper modelMapper,
                            ValidationUtil validationUtil) {
        this.childRepository = childRepository;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.residentService = residentService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Set<ChildServiceModel> getAll(ChildFilter childFilter, Pageable pageable) {

        Set<ChildServiceModel> children = this.childRepository
                .findAll(childFilter, pageable).getContent().stream()
                .map(c -> this.modelMapper.map(c, ChildServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(children);
    }

    @Override
    public Optional<ChildServiceModel> get(String childId) {

        Optional<Child> child = this.childRepository.findById(childId);

        return child.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(child.get(), ChildServiceModel.class));
    }

    @Override
    @Transactional
    public ResponseModel<ChildServiceModel> add(ChildServiceModel childServiceModel,
                                                String buildingId, String apartmentId,
                                                Set<String> parentIds) {

        if (!this.validationUtil.isValid(childServiceModel)) {
            return new ResponseModel<>(childServiceModel,
                    this.validationUtil.violations(childServiceModel));
        }

        if (this.buildingService.get(buildingId).isEmpty()) {
            throw new EntityNotFoundException(BUILDING_NOT_FOUND);
        }

        ApartmentServiceModel apartmentServiceModel =
                this.apartmentService.getByIdAndBuildingId(apartmentId, buildingId)
                .orElseThrow(() -> { throw new EntityNotFoundException(APARTMENT_NOT_FOUND); });

        Set<Resident> parents = this.residentService.getAllByIdIn(parentIds).stream()
                .map(r -> this.modelMapper.map(r, Resident.class))
                .collect(Collectors.toSet());

        if (parents.isEmpty()) {
            throw new UnprocessableEntityException(PARENTS_NOT_FOUND);
        }

        String firstName = childServiceModel.getFirstName().trim();
        String lastName = childServiceModel.getLastName().trim();

        if (this.childExistsInApartment(firstName, lastName, apartmentId, parents)) {
            throw new EntityExistsException(String.format(CHILD_ALREADY_EXISTS,
                    firstName, lastName, apartmentServiceModel.getNumber()));
        }

        Child child = this.modelMapper.map(childServiceModel, Child.class);
        child.setAddedOn(LocalDate.now());
        child.setApartment(this.modelMapper.map(apartmentServiceModel, Apartment.class));

        child.setParents(parents);

        childRepository.saveAndFlush(child);

        return new ResponseModel<>(child.getId(),
                this.modelMapper.map(child, ChildServiceModel.class));
    }

    @Override
    public ResponseModel<ChildServiceModel> edit(ChildServiceModel childServiceModel, String childId) {

        if (!this.validationUtil.isValid(childServiceModel)) {
            return new ResponseModel<>(childServiceModel,
                    this.validationUtil.violations(childServiceModel));
        }

        Child child = this.childRepository.findById(childId).orElse(null);

        if (child != null) {
            child.setFirstName(childServiceModel.getFirstName());
            child.setLastName(childServiceModel.getLastName());

            this.childRepository.saveAndFlush(child);

        } else {
            throw new EntityNotFoundException(CHILD_NOT_FOUND);
        }

        return new ResponseModel<>(child.getId(),
                this.modelMapper.map(child, ChildServiceModel.class));
    }

    @Override
    @Transactional
    public void deleteAll(ChildFilter childFilter) {

        List<Child> children = this.childRepository.findAll(childFilter);

        for (Child child : children) {
            this.childRepository.severParentRelations(child.getId());
        }

        this.childRepository.deleteAll(children);
    }

    @Override
    public void delete(String childId) {

        Child child = this.childRepository.findById(childId)
                .orElseThrow(() -> { throw new EntityNotFoundException(CHILD_NOT_FOUND); });

        this.childRepository.delete(child);
    }

    private boolean childExistsInApartment(String firstName, String lastName,
                                           String apartmentId, Set<Resident> parents) {
        Optional<Child> child = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId(firstName, lastName, apartmentId);

        return child.isPresent() && parents.equals(child.get().getParents());
    }
 }
