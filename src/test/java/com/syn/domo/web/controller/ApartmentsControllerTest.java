package com.syn.domo.web.controller;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private Building building;
    private Apartment apartment1;
    private Apartment apartment2;

    @BeforeEach
    public void setUp() {
        this.building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        apartment1 = new Apartment("1", 1, building, 0, LocalDate.now());
        apartment2 = new Apartment("2", 1, building, 1, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment1);
        this.apartmentRepository.saveAndFlush(apartment2);
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
        assertThat(this.building).isNotNull();
        assertThat(this.apartment1).isNotNull();
        assertThat(this.apartment2).isNotNull();
    }

    @Test
    void test_getAll() throws Exception {
//        this.mvc.perform(MockMvcRequestBuilders.get("/all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.all[*]"))
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
}