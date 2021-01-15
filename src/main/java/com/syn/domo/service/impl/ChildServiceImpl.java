package com.syn.domo.service.impl;

import com.syn.domo.exception.*;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Child;
import com.syn.domo.model.entity.Resident;
import com.syn.domo.model.service.*;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.repository.ChildRepository;
import com.syn.domo.service.*;
import com.syn.domo.utils.UrlCheckerUtil;
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

import static com.syn.domo.common.ExceptionErrorMessages.*;

@Service
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final ResidentService residentService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UrlCheckerUtil urlCheckerUtil;

    @Autowired
    public ChildServiceImpl(ChildRepository childRepository,
                            BuildingService buildingService,
                            ApartmentService apartmentService,
                            @Lazy ResidentService residentService,
                            ModelMapper modelMapper,
                            ValidationUtil validationUtil,
                            UrlCheckerUtil urlCheckerUtil) {
        this.childRepository = childRepository;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.residentService = residentService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.urlCheckerUtil = urlCheckerUtil;
    }

    @Override
    public Set<ChildServiceModel> getAll(String buildingId, String apartmentId, String parentId) {

        return getChildrenBy(buildingId, apartmentId, parentId).stream()
                .map(c -> this.modelMapper.map(c, ChildServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
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
    public void deleteAll(String buildingId, String apartmentId, String parentId) {

        Set<Child> children = this.getChildrenBy(buildingId, apartmentId, parentId);

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


    private boolean hasSameParents(Set<Resident> existingParents,
                                   Set<Resident> newParents) {
        int sameParentsCount = 0;

        for (Resident parent : existingParents) {
            for (Resident newChildParent : newParents) {
                if (parent.getId().equals(newChildParent.getId())) {
                    ++sameParentsCount;
                }
            }
        }


        return sameParentsCount == existingParents.size()
                && sameParentsCount == newParents.size();
    }

    private boolean childExistsInApartment(String firstName, String lastName, String apartmentId,
                                           Set<Resident> parents) {
        Optional<Child> child = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId(firstName, lastName, apartmentId);
//        return child.isPresent() && this.hasSameParents(child.get().getParents(), parents);
        return child.isPresent() && parents.equals(child.get().getParents());
    }

    private boolean notValidRelations(String buildingId, String apartmentId, String parentId) {
        return this.residentService.getOneByIdAndBuildingIdAndApartmentId(buildingId, apartmentId, parentId).isEmpty();
    }

    private boolean parentNotFoundIn(String buildingId, String apartmentId, String parentId) {

        boolean result = false;

        if (!this.urlCheckerUtil.areEmpty(buildingId, apartmentId)) {
            result = notValidRelations(buildingId, apartmentId, parentId);
        } else if (this.urlCheckerUtil.areEmpty(buildingId, apartmentId)) {
            result = this.residentService.get(parentId).isEmpty();
        } else if (this.urlCheckerUtil.areEmpty(buildingId)) {
            result = this.residentService.getOneByIdAndApartmentId(parentId, apartmentId).isEmpty();
        } else if (this.urlCheckerUtil.areEmpty(apartmentId)) {
            result = this.residentService.getOneByIdAndBuildingId(parentId, buildingId).isEmpty();
        }

        return result;
    }

    private Set<Child> getChildrenBy(String buildingId, String apartmentId, String parentId) {

        if (this.urlCheckerUtil.areEmpty(buildingId, apartmentId, parentId)) {
            return this.childRepository.findAll().stream()
                    .collect(Collectors.toUnmodifiableSet());
        }

        if (this.urlCheckerUtil.areEmpty(apartmentId, parentId)) {
            if (this.buildingService.get(buildingId).isEmpty()) {
                throw new EntityNotFoundException(BUILDING_NOT_FOUND);
            }

            return this.childRepository.getAllByBuildingId(buildingId);
        }

        if (this.urlCheckerUtil.areEmpty(parentId)) {
            if (!this.urlCheckerUtil.areEmpty(buildingId)) {
                if (this.buildingService.get(buildingId).isEmpty()) {
                    throw new EntityNotFoundException(BUILDING_NOT_FOUND);
                }

                if (this.apartmentService.getByIdAndBuildingId(apartmentId, buildingId).isEmpty()) {
                    throw new EntityNotFoundException(APARTMENT_NOT_FOUND);
                }
            }

            return this.childRepository.findAllByApartmentId(apartmentId);
        }

        if (this.parentNotFoundIn(buildingId, apartmentId, parentId)) {
            throw new EntityNotFoundException(PARENT_NOT_FOUND);
        }

        return this.childRepository.getAllByParentId(parentId);
    }
 }
