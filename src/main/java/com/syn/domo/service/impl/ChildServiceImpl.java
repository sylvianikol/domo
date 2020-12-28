package com.syn.domo.service.impl;

import com.syn.domo.exception.ApartmentNotFoundException;
import com.syn.domo.exception.BuildingNotFoundException;
import com.syn.domo.exception.ChildAlreadyExists;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Child;
import com.syn.domo.model.entity.Resident;
import com.syn.domo.model.service.*;
import com.syn.domo.repository.ChildRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.ChildService;
import com.syn.domo.service.ResidentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final ResidentService residentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ChildServiceImpl(ChildRepository childRepository, BuildingService buildingService, ApartmentService apartmentService, ResidentService residentService, ModelMapper modelMapper) {
        this.childRepository = childRepository;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.residentService = residentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<ChildServiceModel> getAllChildrenByApartmentId(String apartmentId) {
        Set<ChildServiceModel> childServiceModels = this.childRepository.findAllByApartment_Id(apartmentId).stream()
                .map(child -> this.modelMapper.map(child, ChildServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(childServiceModels);
    }

    @Override
    @Transactional
    public ChildServiceModel add(ChildServiceModel childServiceModel, String buildingId, String apartmentId) {
        // TODO: validation

        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);
        Set<ResidentServiceModel> parentServiceModels =
                this.residentService.getAllById(childServiceModel.getParents().stream()
                        .map(ResidentServiceModel::getId)
                        .collect(Collectors.toSet()));

        Child child;

        if (apartment.isPresent() && this.existAndRelated(building, apartment)
                && !parentServiceModels.isEmpty()) {

            Optional<Child> existingChild =
                    this.childRepository.findByFirstNameAndLastNameAndApartment_Id
                            (childServiceModel.getFirstName(), childServiceModel.getLastName(), apartmentId);

            if (existingChild.isPresent() && this.hasSameParents(childServiceModel, existingChild.get())) {
                throw new ChildAlreadyExists(String.format("Child named %s %s already exists in Apartment No.%s",
                        childServiceModel.getFirstName(),
                        childServiceModel.getLastName(),
                        apartment.get().getNumber()));
            }

            child = this.modelMapper.map(childServiceModel, Child.class);
            child.setAddedOn(LocalDate.now());
            child.setApartment(this.modelMapper.map(apartment.get(), Apartment.class));

            Set<Resident> parents = parentServiceModels.stream()
                    .map(p -> this.modelMapper.map(p, Resident.class))
                    .collect(Collectors.toSet());

            parents.forEach(parent -> parent.getChildren().add(child));

            child.setParents(parents);

            childRepository.saveAndFlush(child);

        } else {
            throw new UnprocessableEntityException("Invalid data! Child could not be added!");
        }

        return null;
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

    @Override
    public ChildServiceModel getById(String childId) {
        // TODO: ChildNotFoundException
        return this.childRepository.findById(childId)
                .map(child -> this.modelMapper.map(child, ChildServiceModel.class))
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Child not found!");
                });
    }

//    @Override
//    public void removeAllByApartmentId(String apartmentId) {
//        this.childRepository.findAllByApartment_Id(apartmentId)
//                .forEach(child -> {
//                    child.setRemovedOn(LocalDate.now());
//                    this.childRepository.saveAndFlush(child);
//                });
//    }

    private boolean existAndRelated(Optional<BuildingServiceModel> building,
                                    Optional<ApartmentServiceModel> apartment) {
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        return true;
    }
}
