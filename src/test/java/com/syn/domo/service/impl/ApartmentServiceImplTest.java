package com.syn.domo.service.impl;

import com.syn.domo.exception.DomoEntityNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.*;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.service.ApartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ApartmentServiceImplTest {

    @Autowired
    private ApartmentService apartmentService;

    @MockBean
    private ApartmentRepository apartmentRepository;

    @Test
    void test_getAll_returnsAll() {

        Building building = new Building("TestBuilding 1", "Test neighbourhood",
                        "TestAddress",3, BigDecimal.valueOf(5),
                        BigDecimal.valueOf(100), LocalDate.now());
        building.setId("1");
        Apartment apartment = new Apartment("1", 1, building, 0, LocalDate.now());

        when(this.apartmentRepository.findAll()).thenReturn(List.of(apartment));

        Set<ApartmentServiceModel> apartmentServiceModels = this.apartmentService.getAll();

        assertEquals(1, apartmentServiceModels.size());
        ApartmentServiceModel actual = apartmentServiceModels.iterator().next();
        assertEquals(apartment.getNumber(), actual.getNumber());
        assertEquals(apartment.getFloor(), actual.getFloor());
        assertEquals(apartment.getBuilding().getId(), actual.getBuilding().getId());
        assertEquals(apartment.getAddedOn(), actual.getAddedOn());
    }

    @Test
    void test_getAll_returnsEmpty() {

        Set<ApartmentServiceModel> apartmentServiceModels = apartmentService.getAll();
        assertEquals(0, apartmentServiceModels.size());
    }

    @Test
    void test_add_serviceModelNotValid() {
        ApartmentServiceModel apartmentServiceModel =
                new ApartmentServiceModel("invalid", -1, null, -1, null);

        assertThrows(UnprocessableEntityException.class, () ->
                this.apartmentService.add(apartmentServiceModel, "1"));
    }

    @Test
    void test_add_throwsWhenInvalidBuildingId() {
        ApartmentServiceModel apartmentServiceModel =
                new ApartmentServiceModel("1", 1, null, 1, null);

        assertThrows(DomoEntityNotFoundException.class, () ->
                this.apartmentService.add(apartmentServiceModel, "1"));
    }

    @Test
    void test_edit_serviceModelNotValid() {
        ApartmentServiceModel apartmentServiceModel =
                new ApartmentServiceModel("invalid", -1, null, -1, null);

        assertThrows(UnprocessableEntityException.class, () ->
                this.apartmentService.edit(apartmentServiceModel, "1"));
    }

    @Test
    void test_edit_throwsWhenInvalidBuildingId() {
        ApartmentServiceModel apartmentServiceModel =
                new ApartmentServiceModel("1", 1, null, 1, null);

        assertThrows(DomoEntityNotFoundException.class, () ->
                this.apartmentService.edit(apartmentServiceModel, "1"));
    }
}