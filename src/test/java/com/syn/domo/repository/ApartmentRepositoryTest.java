package com.syn.domo.repository;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ApartmentRepositoryTest {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private BuildingRepository buildingRepository;

//    @Before
//    void setup() {
//
//    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.apartmentRepository).isNotNull();
        assertThat(this.buildingRepository).isNotNull();
    }

    @Test
    void test_findByNumberAndBuildingId_IsPresent() {
        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        Apartment apartment = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment);

        Optional<Apartment> found = this.apartmentRepository
                .findByNumberAndBuildingId("1", building.getId());

        assertThat(found).isPresent();
    }

    @Test
    void getDuplicateApartment() {
    }

    @Test
    void getAllByBuildingId() {
    }

    @Test
    void getByIdIn() {
    }

    @Test
    void findByIdAndBuildingId() {
    }
}