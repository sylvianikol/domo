package com.syn.domo.service.impl;

import com.syn.domo.exception.*;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Child;
import com.syn.domo.model.entity.Resident;
import com.syn.domo.model.entity.UserEntity;
import com.syn.domo.model.service.*;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.repository.ChildRepository;
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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
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
                            ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.childRepository = childRepository;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.residentService = residentService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Set<ChildServiceModel> getAll(String buildingId, String apartmentId) {
        Set<ChildServiceModel> childServiceModels = this.childRepository
                .getAllByApartmentIdAndBuildingId(buildingId, apartmentId)
                .stream()
                .map(c -> this.modelMapper.map(c, ChildServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(childServiceModels);
    }

    @Override
    public Optional<ChildServiceModel> get(String buildingId, String apartmentId, String childId) {
        Set<String> parentsIds =
                this.residentService.getAll(buildingId, apartmentId)
                        .stream().map(BaseServiceModel::getId)
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        Optional<Child> child = this.childRepository
                .getOneByIdAndParentsIds(childId, parentsIds);

        return child.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(child.get(), ChildServiceModel.class));
    }

    @Override
    @Transactional
    public ResponseModel<ChildServiceModel> add(ChildServiceModel childServiceModel,
                                                String buildingId, String apartmentId) {

        if (!this.validationUtil.isValid(childServiceModel)) {
            return new ResponseModel<>(childServiceModel,
                    this.validationUtil.violations(childServiceModel));
        }

        BuildingServiceModel building = this.buildingService.get(buildingId)
                .orElseThrow(() -> { throw new EntityNotFoundException(BUILDING_NOT_FOUND); });

        ApartmentServiceModel apartmentServiceModel =
                this.apartmentService.getByIdAndBuildingId(apartmentId, building.getId())
                .orElseThrow(() -> { throw new EntityNotFoundException(APARTMENT_NOT_FOUND); });

        Set<ResidentServiceModel> parentServiceModels =
                this.residentService.getAllByIdIn(this.getParentsIds(childServiceModel));

        if (parentServiceModels.isEmpty()) {
            throw new UnprocessableEntityException(PARENTS_NOT_FOUND);
        }

        String firstName = childServiceModel.getFirstName().trim();
        String lastName = childServiceModel.getLastName().trim();

        if (this.childExistsInApartment(firstName, lastName, apartmentId, childServiceModel)) {
            throw new EntityExistsException(
                    String.format(CHILD_EXISTS, firstName, lastName, apartmentServiceModel.getNumber()));
        }

        Child child = this.modelMapper.map(childServiceModel, Child.class);
        child.setAddedOn(LocalDate.now());
        child.setApartment(this.modelMapper.map(apartmentServiceModel, Apartment.class));

        child.setParents(this.getParents(parentServiceModels));

        childRepository.saveAndFlush(child);

        return new ResponseModel<>(child.getId(),
                this.modelMapper.map(child, ChildServiceModel.class));
    }

    @Override
    public ResponseModel<ChildServiceModel> edit(ChildServiceModel childServiceModel,
                                  String buildingId, String apartmentId, String childId) {

        if (!this.validationUtil.isValid(childServiceModel)) {
            return new ResponseModel<>(childServiceModel,
                    this.validationUtil.violations(childServiceModel));
        }

        BuildingServiceModel building = this.buildingService.get(buildingId)
                .orElseThrow(() -> { throw new EntityNotFoundException(BUILDING_NOT_FOUND); });

        if (this.apartmentService.getByIdAndBuildingId(apartmentId, building.getId()).isEmpty()) {
            throw new EntityNotFoundException(APARTMENT_NOT_FOUND);
        }

        Child child = this.childRepository
                .findByIdAndApartmentId(childId, apartmentId)
                .orElse(null);

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
    public void deleteAll(String buildingId, String apartmentId) {

        if (this.buildingService.get(buildingId).isEmpty()) {
            throw new EntityNotFoundException(BUILDING_NOT_FOUND);
        }

        if (this.apartmentService.getByIdAndBuildingId(apartmentId, buildingId).isEmpty()) {
            throw new EntityNotFoundException(APARTMENT_NOT_FOUND);
        }

        Set<Child> children = this.childRepository
                .getAllByApartmentIdAndBuildingId(buildingId, apartmentId);

        this.childRepository.deleteAll(children);
    }

    @Override
    public void delete(String childId, String buildingId, String apartmentId) {
        Optional<BuildingServiceModel> building = this.buildingService.get(buildingId);
        if (building.isEmpty()) {
            throw new EntityNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.get(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new EntityNotFoundException("Apartment not found!");
        }

        Child child = this.childRepository.findById(childId).orElse(null);

        if (child != null && child.getApartment().getId().equals(apartmentId)) {
            this.childRepository.delete(child);
        } else {
            throw new EntityNotFoundException("Child not found!");
        }
    }

    private boolean hasSameParents(ChildServiceModel newChild, Child existingChild) {
        int sameParentsCount = 0;
        Set<Resident> existingParents = existingChild.getParents();
        Set<ResidentServiceModel> newParents = newChild.getParents();

        for (Resident parent : existingParents) {
            for (ResidentServiceModel newChildParent : newParents) {
                if (parent.getId().equals(newChildParent.getId())) {
                    ++sameParentsCount;
                }
            }
        }
        return sameParentsCount == existingParents.size() && sameParentsCount == newParents.size();
    }

    private boolean childExistsInApartment(String firstName, String lastName, String apartmentId,
                                           ChildServiceModel newChild) {
        Optional<Child> child = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId(firstName, lastName, apartmentId);
        return child.isPresent() && this.hasSameParents(newChild, child.get());
    }

    private Set<String> getParentsIds(ChildServiceModel childServiceModel) {
        return childServiceModel.getParents().stream()
                .map(UserServiceModel::getId)
                .collect(Collectors.toUnmodifiableSet());
    }

    private Set<Resident> getParents(Set<ResidentServiceModel> parentServiceModels) {
        return parentServiceModels.stream()
                .map(p -> this.modelMapper.map(p, Resident.class))
                .collect(Collectors.toUnmodifiableSet());
    }
 }
