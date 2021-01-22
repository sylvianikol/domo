package com.syn.domo.repository;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ApartmentRepositoryTest {

    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    private Building building;

    private String BUILDING_ID;
    private String APARTMENT_ID;
    private String APARTMENT_NUMBER;

    @BeforeEach
    public void setUp() {
        this.tearDown();

        this.building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(this.building);

        Apartment apartment = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment);

        BUILDING_ID = this.building.getId();
        APARTMENT_ID = apartment.getId();
        APARTMENT_NUMBER = apartment.getNumber();
    }

    @AfterEach
    public void tearDown() {
        this.apartmentRepository.deleteAll();
        this.buildingRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.apartmentRepository).isNotNull();
        assertThat(this.buildingRepository).isNotNull();
    }

    @Test
    void test_findByNumberAndBuildingId_IsPresent() {

        Optional<Apartment> found = this.apartmentRepository
                .findByNumberAndBuildingId(APARTMENT_NUMBER, BUILDING_ID);

        assertThat(found).isPresent();
        assertEquals(found.get().getId(), APARTMENT_ID);
    }

    @Test
    void test_findByNumberAndBuildingId_IsEmpty() {
        Optional<Apartment> found = this.apartmentRepository
                .findByNumberAndBuildingId("2", BUILDING_ID);

        assertThat(found).isEmpty();

        found = this.apartmentRepository
                .findByNumberAndBuildingId(APARTMENT_NUMBER, "2");

        assertThat(found).isEmpty();
    }

    @Test
    void test_getDuplicateApartment_IsPresent() {

        Apartment apartment2 = new Apartment("1", 1, this.building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment2);

        Optional<Apartment> duplicate = this.apartmentRepository
                .getDuplicateApartment(apartment2.getNumber(), BUILDING_ID, apartment2.getId());

        assertThat(duplicate).isPresent();
    }

    @Test
    void test_getDuplicateApartment_IsEmpty() {

        Apartment apartment2 = new Apartment("2", 1, this.building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment2);

        Optional<Apartment> duplicate = this.apartmentRepository
                .getDuplicateApartment(apartment2.getNumber(), BUILDING_ID, apartment2.getId());

        assertThat(duplicate).isEmpty();
    }

    @Test
    void test_getDuplicateApartment_returnsDuplicate() {

        Apartment apartment2 = new Apartment("1", 1, this.building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment2);

        Apartment duplicate = this.apartmentRepository
                .getDuplicateApartment("1", BUILDING_ID, apartment2.getId())
                .orElse(null);

        assertThat(duplicate).isNotNull();
        assertEquals(duplicate.getNumber(), "1");
        assertEquals(duplicate.getBuilding().getId(), BUILDING_ID);
        assertNotEquals(duplicate.getId(), apartment2.getId());
    }

    @Test
    void test_getAllByBuildingId_isNotEmpty() {
        Set<Apartment> apartments = this.apartmentRepository
                .findAllByBuildingId(BUILDING_ID);

        assertThat(apartments).isNotEmpty();
        assertEquals(apartments.iterator().next().getBuilding().getId(), BUILDING_ID);
    }

    @Test
    void test_getAllByBuildingId_isEmpty() {
        Set<Apartment> apartments = this.apartmentRepository
                .findAllByBuildingId("0");

        assertThat(apartments).isEmpty();
    }

    @Test
    void test_getAllByBuildingId_returnsExpectedSize() {
        Set<Apartment> apartments = this.apartmentRepository
                .findAllByBuildingId(BUILDING_ID);

        assertEquals(apartments.size(), 1);
    }

    @Test
    void test_findByIdAndBuildingId_IsPresent() {
        Optional<Apartment> found = this.apartmentRepository
                .findByIdAndBuildingId(APARTMENT_ID, BUILDING_ID);

        assertThat(found).isPresent();
        assertEquals(found.get().getId(), APARTMENT_ID);
        assertEquals(found.get().getBuilding().getId(), BUILDING_ID);
    }

    @Test
    void test_findByIdAndBuildingId_IsEmpty() {
        Optional<Apartment> found = this.apartmentRepository
                .findByIdAndBuildingId("0", BUILDING_ID);

        assertThat(found).isEmpty();

        found = this.apartmentRepository
                .findByIdAndBuildingId(APARTMENT_ID, "0");

        assertThat(found).isEmpty();

        found = this.apartmentRepository
                .findByIdAndBuildingId("0", "0");

        assertThat(found).isEmpty();
    }
}