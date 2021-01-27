package com.syn.domo.service.impl;

import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.BuildingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

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
        BuildingServiceModel buildingToAdd = new BuildingServiceModel(
                "", "Test neighbourhood",
                "TestAddress",3, BigDecimal.valueOf(5),
                BigDecimal.valueOf(100), null);

        assertThrows(UnprocessableEntityException.class, () -> this.buildingService.add(buildingToAdd));
    }

    @Test
    void test_edit_withInvalidData() {
        BuildingServiceModel buildingServiceModel = new BuildingServiceModel(
                "", "Test neighbourhood",
                "TestAddress",3, BigDecimal.valueOf(5),
                BigDecimal.valueOf(100), null);

        assertThrows(UnprocessableEntityException.class, () ->
                this.buildingService.edit(buildingServiceModel, ""));

    }
}
