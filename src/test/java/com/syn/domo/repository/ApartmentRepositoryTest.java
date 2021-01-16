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

@DataJpaTest
class ApartmentRepositoryTest {

    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    private Building building;
    private Apartment apartment;

    @BeforeEach
    public void setup() {
        this.building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        apartment = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment);
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
    void test_findByNumberAndBuildingId_IsPresent() {

        Optional<Apartment> found = this.apartmentRepository
                .findByNumberAndBuildingId(apartment.getNumber(), building.getId());

        assertThat(found).isPresent();
    }

    @Test
    void test_findByNumberAndBuildingId_IsEmpty() {
        Optional<Apartment> found = this.apartmentRepository
                .findByNumberAndBuildingId("2", building.getId());

        assertThat(found).isEmpty();

        found = this.apartmentRepository
                .findByNumberAndBuildingId(apartment.getNumber(), "2");

        assertThat(found).isEmpty();
    }


    @Test
    void test_findByNumberAndBuildingId_returnsExpected() {

        Apartment found = this.apartmentRepository
                .findByNumberAndBuildingId(apartment.getNumber(), building.getId())
                .orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getNumber().equals("1"));
        assertThat(found.getFloor() == 1);
        assertThat(found.getBuilding().equals(building));
        assertThat(found.getAddedOn().equals(LocalDate.now()));
    }

    @Test
    void test_getDuplicateApartment_isPresent() {

        Apartment apartment2 = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment2);

        Optional<Apartment> duplicate = this.apartmentRepository
                .getDuplicateApartment(apartment2.getNumber(), building.getId(), apartment2.getId());

        assertThat(duplicate).isPresent();
    }

    @Test
    void test_getDuplicateApartment_isEmpty() {

        Apartment apartment2 = new Apartment("2", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment2);

        Optional<Apartment> duplicate = this.apartmentRepository
                .getDuplicateApartment(apartment2.getNumber(), building.getId(), apartment2.getId());

        assertThat(duplicate).isEmpty();
    }

    @Test
    void test_getDuplicateApartment_isDuplicate() {

        Apartment apartment2 = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment2);

        Apartment duplicate = this.apartmentRepository
                .getDuplicateApartment(apartment2.getNumber(), building.getId(), apartment2.getId())
                .orElse(null);

        assertThat(duplicate).isNotNull();
        assertThat(duplicate.getNumber().equals("1"));
        assertThat(duplicate.getBuilding().getId().equals(building.getId()));
        assertThat(!duplicate.getId().equals(apartment2.getId()));
    }

    @Test
    void test_getAllByBuildingId_isNotEmpty() {
        Set<Apartment> apartments = this.apartmentRepository
                .getAllByBuildingId(building.getId());

        assertThat(apartments).isNotEmpty();
    }

    @Test
    void test_getAllByBuildingId_isEmpty() {
        Set<Apartment> apartments = this.apartmentRepository
                .getAllByBuildingId("0");

        assertThat(apartments).isEmpty();
    }

    @Test
    void test_findByIdAndBuildingId_IsPresent() {
        Optional<Apartment> found = this.apartmentRepository
                .findByIdAndBuildingId(apartment.getId(), building.getId());

        assertThat(found).isPresent();
    }

    @Test
    void test_findByIdAndBuildingId_IsEmpty() {
        Optional<Apartment> found = this.apartmentRepository
                .findByIdAndBuildingId("0", building.getId());

        assertThat(found).isEmpty();
    }
}