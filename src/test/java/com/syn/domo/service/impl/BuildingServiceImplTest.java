package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.StaffService;
import com.syn.domo.utils.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.syn.domo.common.ValidationErrorMessages.BUILDING_NAME_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
class BuildingServiceImplTest {

    @Autowired
    private BuildingService buildingService;

    @MockBean
    private BuildingRepository buildingRepository;

    @Test
    void test_getAll() {

        Building building = new Building("TestBuilding 1", "Test neighbourhood",
                "TestAddress",3, BigDecimal.valueOf(5),
                BigDecimal.valueOf(100), LocalDate.now());
        doReturn(List.of(building)).when(this.buildingRepository).findAll();

        Set<BuildingServiceModel> all = this.buildingService.getAll();

        assertEquals(1, all.size(), "getAll should return 1 building");
    }

    @Test
    void test_add_withInvalidData() {
        BuildingServiceModel buildingServiceModel = new BuildingServiceModel(
                "", "Test neighbourhood",
                "TestAddress",3, BigDecimal.valueOf(5),
                BigDecimal.valueOf(100), null);

        ResponseModel<BuildingServiceModel> responseModel =
                this.buildingService.add(buildingServiceModel);

        Map<String, Set<String>> errors = responseModel.getErrorContainer().getErrors();

        assertEquals(errors.get("name"), Set.of(BUILDING_NAME_NOT_EMPTY));
    }

    @Test
    void test_edit_withInvalidData() {
        BuildingServiceModel buildingServiceModel = new BuildingServiceModel(
                "", "Test neighbourhood",
                "TestAddress",3, BigDecimal.valueOf(5),
                BigDecimal.valueOf(100), null);

        ResponseModel<BuildingServiceModel> responseModel =
                this.buildingService.edit(buildingServiceModel, "");

        Map<String, Set<String>> errors = responseModel.getErrorContainer().getErrors();

        assertEquals(errors.get("name"), Set.of(BUILDING_NAME_NOT_EMPTY));
    }
}
