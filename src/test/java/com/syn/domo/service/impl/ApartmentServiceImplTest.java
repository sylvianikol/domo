package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.ChildService;
import com.syn.domo.service.ResidentService;
import com.syn.domo.utils.ValidationUtil;
import com.syn.domo.utils.ValidationUtilImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApartmentServiceImplTest {

    private ApartmentService apartmentService;
    private BuildingService buildingService;
    private ResidentService residentService;
    private ChildService childService;

    @Mock
    private ApartmentRepository apartmentRepository;

    @BeforeEach
    void setUp() {
        this.apartmentService = new ApartmentServiceImpl(
                apartmentRepository, buildingService,
                residentService, childService,
                new ModelMapper(), new ValidationUtilImpl()
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_getAll_returnsAll() {

        Building building = new Building("TestBuilding 1", "Test neighbourhood",
                        "TestAddress",3, BigDecimal.valueOf(5),
                        BigDecimal.valueOf(100), LocalDate.now());
        building.setId("1");
        Apartment apartment = new Apartment("1", 1, building, 0, LocalDate.now());

        when(apartmentRepository.findAll()).thenReturn(List.of(apartment));

        Set<ApartmentServiceModel> apartmentServiceModels = apartmentService.getAll();

        assertEquals(1, apartmentServiceModels.size());
        ApartmentServiceModel actual = apartmentServiceModels.iterator().next();
        assertEquals(apartment.getNumber(), actual.getNumber());
        assertEquals(apartment.getFloor(), actual.getFloor());
        assertEquals(apartment.getBuilding().getId(), actual.getBuilding().getId());
        assertEquals(apartment.getAddedOn(), actual.getAddedOn());
    }

    @Test
    void test_getAll_returnsEmpty() {
        when(apartmentRepository.findAll()).thenReturn(List.of());
        Set<ApartmentServiceModel> apartmentServiceModels = apartmentService.getAll();
        assertEquals(0, apartmentServiceModels.size());
    }

    @Test
    void get() {
    }

    @Test
    void add() {
    }

    @Test
    void edit() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void delete() {
    }

    @Test
    void evacuateApartments() {
    }

    @Test
    void getByIdAndBuildingId() {
    }
}