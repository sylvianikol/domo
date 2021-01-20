package com.syn.domo.web.controller;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.ApartmentService;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApartmentsControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    private String BUILDING_ID;
    private String APARTMENT_1_ID;
    private String APARTMENT_2_ID;
    private final String URI = "/v1/apartments";

    @BeforeEach
    public void setUp() {
        this.tearDown();

        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        Apartment apartment1 = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment1);
        Apartment apartment2 = new Apartment("2", 1, building, 1, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment2);

        BUILDING_ID = building.getId();
        APARTMENT_1_ID = apartment1.getId();
        APARTMENT_2_ID = apartment2.getId();
    }

    @AfterEach
    public void tearDown() {
        this.buildingRepository.deleteAll();
        this.apartmentRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.apartmentRepository).isNotNull();
        assertThat(this.buildingRepository).isNotNull();
    }

    @Test
    void test_getAll_returnsCorrectResponse() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get(URI + "/all?{buildingId}", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(APARTMENT_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(APARTMENT_2_ID)));
    }

    @Test
    void test_getAllByBuildingId_returnsCorrectResponse() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get(URI + "/all?{buildingId}", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(APARTMENT_1_ID)))
                .andExpect(jsonPath("$.[0].building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[1].id", is(APARTMENT_2_ID)))
                .andExpect(jsonPath("$.[1].building.id", is(BUILDING_ID)));
    }

    @Test
    void test_get_isOK() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders
                .get(URI + "/{apartmentId}", APARTMENT_1_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test_get_notFound() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders
                .get(URI + "/{apartmentId}", "666"))
                .andDo(print())
                .andExpect(status().isNotFound());
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
}