package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.ChildService;
import com.syn.domo.service.ResidentService;
import com.syn.domo.utils.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.syn.domo.common.ValidationErrorMessages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ApartmentServiceImplTest {

    ApartmentServiceImpl apartmentService;

    @Mock
    ApartmentRepository apartmentRepository;
    @Mock
    BuildingService buildingService;
    @Autowired
    ResidentService residentService;
    @Autowired
    ChildService childService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ValidationUtil validationUtil;

    @BeforeEach
    void setUp() {
        this.apartmentService = new ApartmentServiceImpl(
                apartmentRepository, buildingService,
                residentService, childService,
                modelMapper, validationUtil
        );
    }

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

        ResponseModel<ApartmentServiceModel> responseModel =
                this.apartmentService.add(apartmentServiceModel, "1");

        Map<String, Set<String>> errors = responseModel.getErrorContainer().getErrors();

        assertEquals(errors.get("pets"), Set.of(PETS_MIN));
        assertEquals(errors.get("floor"), Set.of(FLOOR_MIN));
        assertEquals(errors.get("number"), Set.of(APARTMENT_INVALID_NUMBER));
    }

    @Test
    void test_add_throwsWhenInvalidBuildingId() {
        ApartmentServiceModel apartmentServiceModel =
                new ApartmentServiceModel("1", 1, null, 1, null);

        assertThrows(EntityNotFoundException.class, () ->
                this.apartmentService.add(apartmentServiceModel, "1"));
    }

    @Test
    void test_edit_serviceModelNotValid() {
        ApartmentServiceModel apartmentServiceModel =
                new ApartmentServiceModel("invalid", -1, null, -1, null);

        ResponseModel<ApartmentServiceModel> responseModel =
                this.apartmentService.edit(apartmentServiceModel, "1");

        Map<String, Set<String>> errors = responseModel.getErrorContainer().getErrors();

        assertEquals(errors.get("pets"), Set.of(PETS_MIN));
        assertEquals(errors.get("floor"), Set.of(FLOOR_MIN));
        assertEquals(errors.get("number"), Set.of(APARTMENT_INVALID_NUMBER));
    }

    @Test
    void test_edit_throwsWhenInvalidBuildingId() {
        ApartmentServiceModel apartmentServiceModel =
                new ApartmentServiceModel("1", 1, null, 1, null);

        assertThrows(EntityNotFoundException.class, () ->
                this.apartmentService.edit(apartmentServiceModel, "1"));
    }

}