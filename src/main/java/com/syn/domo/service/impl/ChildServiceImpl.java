package com.syn.domo.service.impl;

import com.syn.domo.exception.*;
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
    public Set<ChildServiceModel> getAllByApartmentId(String apartmentId) {
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
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        Set<ResidentServiceModel> parentServiceModels =
                this.residentService.getAllById(childServiceModel.getParents().stream()
                        .map(ResidentServiceModel::getId)
                        .collect(Collectors.toSet()));

        if (parentServiceModels.isEmpty()) {
            throw new UnprocessableEntityException("Child must have parents!");
        }

        Optional<Child> existingChild =
                this.childRepository.findByFirstNameAndLastNameAndApartment_Id
                        (childServiceModel.getFirstName(), childServiceModel.getLastName(), apartmentId);

        if (existingChild.isPresent() && this.hasSameParents(childServiceModel, existingChild.get())) {
            throw new ChildAlreadyExists(String.format("Child named '%s %s' already lives in Apartment No.%s",
                    childServiceModel.getFirstName(),
                    childServiceModel.getLastName(),
                    apartment.get().getNumber()));
        }

        Child child = this.modelMapper.map(childServiceModel, Child.class);
        child.setAddedOn(LocalDate.now());
        child.setApartment(this.modelMapper.map(apartment.get(), Apartment.class));

        Set<Resident> parents = parentServiceModels.stream()
                .map(p -> this.modelMapper.map(p, Resident.class))
                .collect(Collectors.toSet());

        parents.forEach(parent -> parent.getChildren().add(child));

        child.setParents(parents);

        childRepository.saveAndFlush(child);

        return this.modelMapper.map(child, ChildServiceModel.class);
    }

    @Override
    public ChildServiceModel edit(ChildServiceModel childServiceModel, String buildingId, String apartmentId) {
        // TODO: validation

        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        Child child = this.childRepository.findById(childServiceModel.getId()).orElse(null);

        if (child != null && child.getApartment().getId().equals(apartmentId)) {
            child.setFirstName(childServiceModel.getFirstName());
            child.setLastName(childServiceModel.getLastName());
            child.setAddedOn(childServiceModel.getAddedOn());

            this.childRepository.saveAndFlush(child);
        } else {
            throw new ChildNotFoundException("Child not found!");
        }

        return this.modelMapper.map(child, ChildServiceModel.class);
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
    public Optional<ChildServiceModel> getById(String childId) {
        Optional<Child> child = this.childRepository.findById(childId);
        return child.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(child.get(), ChildServiceModel.class));
    }

//    @Override
//    public void removeAllByApartmentId(String apartmentId) {
//        this.childRepository.findAllByApartment_Id(apartmentId)
//                .forEach(child -> {
//                    child.setRemovedOn(LocalDate.now());
//                    this.childRepository.saveAndFlush(child);
//                });
//    }
}
