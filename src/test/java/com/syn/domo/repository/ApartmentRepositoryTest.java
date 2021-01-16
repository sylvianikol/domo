package com.syn.domo.repository;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import org.junit.Before;
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
    void test_findByNumberAndBuildingId_IsEmpty() {
        Optional<Apartment> found = this.apartmentRepository
                .findByNumberAndBuildingId("1", "1");

        assertThat(found).isEmpty();
    }


    @Test
    void test_findByNumberAndBuildingId_returnsExpected() {
        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        Apartment apartment = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment);

        Apartment found = this.apartmentRepository
                .findByNumberAndBuildingId("1", building.getId())
                .orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getNumber().equals("1"));
        assertThat(found.getFloor() == 1);
        assertThat(found.getBuilding().equals(building));
        assertThat(found.getAddedOn().equals(LocalDate.now()));
    }

    @Test
    void test_getDuplicateApartment_isPresent() {
        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        Apartment apartment1 = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment1);
        Apartment apartment2 = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment2);

        Optional<Apartment> duplicate = this.apartmentRepository
                .getDuplicateApartment("1", building.getId(), apartment1.getId());

        assertThat(duplicate).isPresent();
    }

    @Test
    void test_getDuplicateApartment_isEmpty() {
        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        Apartment apartment1 = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment1);
        Apartment apartment2 = new Apartment("2", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment2);

        Optional<Apartment> duplicate = this.apartmentRepository
                .getDuplicateApartment("2", building.getId(), apartment1.getId());

        assertThat(duplicate).isEmpty();
    }

    @Test
    void test_getDuplicateApartment_isDuplicate() {
        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        Apartment original = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(original);
        Apartment duplicate = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(duplicate);

        Apartment found = this.apartmentRepository
                .getDuplicateApartment(duplicate.getNumber(), building.getId(), original.getId())
                .orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getNumber().equals("1"));
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