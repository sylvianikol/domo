package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.StaffService;
import com.syn.domo.utils.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.syn.domo.common.ValidationErrorMessages.BUILDING_NAME_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BuildingServiceImplTest {

    private BuildingServiceImpl buildingService;

    @Mock
    private BuildingRepository buildingRepository;
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ValidationUtil validationUtil;

    @BeforeEach
    void setUp() {
        this.buildingService = new BuildingServiceImpl(
                buildingRepository, apartmentService, staffService,
                modelMapper, validationUtil);
    }

    @Test
    void test_getAll() {
        Building building = new Building("TestBuilding 1", "Test neighbourhood",
                "TestAddress",3, BigDecimal.valueOf(5),
                BigDecimal.valueOf(100), LocalDate.now());
        when(this.buildingRepository.findAll()).thenReturn(List.of(building));

        Set<BuildingServiceModel> all = this.buildingService.getAll();

        assertThat(all).isNotEmpty();
        assertEquals(all.size(), 1);
    }

    @Test
    void test_add_serviceModelNotValid() {
        BuildingServiceModel buildingServiceModel = new BuildingServiceModel(
                "", "Test neighbourhood",
                "TestAddress",3, BigDecimal.valueOf(5),
                BigDecimal.valueOf(100), null);

        ResponseModel<BuildingServiceModel> responseModel = this.buildingService.add(buildingServiceModel);

        Map<String, Set<String>> errors = responseModel.getErrorContainer().getErrors();

        assertEquals(errors.get("name"), Set.of(BUILDING_NAME_NOT_EMPTY));
    }

}