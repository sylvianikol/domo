package com.syn.domo.service.impl;

import com.syn.domo.exception.ApartmentNotFoundException;
import com.syn.domo.exception.BuildingNotFoundException;
import com.syn.domo.exception.ResidentNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Resident;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.repository.ResidentRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.ResidentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResidentServiceImpl implements ResidentService {

    private final ResidentRepository residentRepository;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ResidentServiceImpl(ResidentRepository residentRepository,
                               BuildingService buildingService,
                               ApartmentService apartmentService,
                               ModelMapper modelMapper) {
        this.residentRepository = residentRepository;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResidentServiceModel add(ResidentServiceModel residentServiceModel, String buildingId, String apartmentId) {
        // TODO: validation

        if (this.buildingService.getById(buildingId).isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);
        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(buildingId)) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        Resident resident = this.modelMapper.map(residentServiceModel, Resident.class);
        resident.setAddedOn(LocalDate.now());
        resident.setUserRole(UserRole.RESIDENT);

        resident.setApartment(this.modelMapper.map(apartment.get(), Apartment.class));

        this.residentRepository.saveAndFlush(resident);

        return this.modelMapper.map(resident, ResidentServiceModel.class);
    }

    @Override
    public ResidentServiceModel edit(ResidentServiceModel residentServiceModel, String buildingId, String apartmentId) {
        // TODO: validation

        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);
        Resident resident = this.residentRepository.findById(residentServiceModel.getId()).orElse(null);
        Optional<Resident> anotherResident = this.residentRepository.findByEmail(residentServiceModel.getEmail());

        if (anotherResident.isPresent() && !anotherResident.get().getId().equals(residentServiceModel.getId())) {
            throw new UnprocessableEntityException(
                    String.format("Email '%s' is already used by another resident.",
                            residentServiceModel.getEmail()));
        }

        if (resident != null && this.existAndRelated(building, apartment, resident)) {


            resident.setFirstName(residentServiceModel.getFirstName());
            resident.setLastName(residentServiceModel.getLastName());
            resident.setEmail(residentServiceModel.getEmail());
            resident.setPhoneNumber(residentServiceModel.getPhoneNumber());
            resident.setAddedOn(residentServiceModel.getAddedOn());
            resident.setUserRole(UserRole.valueOf(residentServiceModel.getUserRole()));

            this.residentRepository.saveAndFlush(resident);
        } else {
            throw new ResidentNotFoundException("Resident not found!");
        }

        return this.modelMapper.map(resident, ResidentServiceModel.class);
    }

    @Override
    public void delete(String residentId, String buildingId, String apartmentId) {
        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        Optional<ApartmentServiceModel> apartment = this.apartmentService.getById(apartmentId);
        Resident resident = this.residentRepository.findById(residentId).orElse(null);

        if (resident != null && this.existAndRelated(building, apartment, resident)) {
            this.residentRepository.delete(resident);
        }
    }

    @Override
    @Transactional
    public void deleteAllByApartmentId(String apartmentId) {
        Set<Resident> residents = this.residentRepository.findAllByApartment_Id(apartmentId);
        this.residentRepository.deleteAll(residents);
    }

    @Override
    public Set<ResidentServiceModel> getAllByApartmentId(String apartmentId) {
        Set<ResidentServiceModel> residentServiceModels =
                this.residentRepository.findAllByApartment_Id(apartmentId)
                        .stream()
                        .map(resident -> this.modelMapper.map(resident, ResidentServiceModel.class))
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(residentServiceModels);
    }

    @Override
    public Optional<ResidentServiceModel> getById(String residentId) {

        Optional<Resident> resident = this.residentRepository.findById(residentId);

        return resident.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(resident.get(), ResidentServiceModel.class));
    }

    private boolean existAndRelated(Optional<BuildingServiceModel> building,
                                    Optional<ApartmentServiceModel> apartment,
                                    Resident resident) {
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        if (apartment.isEmpty() || !apartment.get().getBuilding().getId().equals(building.get().getId())) {
            throw new ApartmentNotFoundException("Apartment not found!");
        }

        if (!resident.getApartment().getId().equals(apartment.get().getId())) {
            throw new ResidentNotFoundException("Resident not found!");
        }

        return true;
    }
}
