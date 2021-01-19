package com.syn.domo.repository;

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
class BuildingRepositoryTest {

    @Autowired
    private BuildingRepository buildingRepository;
    private Building building;

    @BeforeEach
    void setUp() {
        this.building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);
    }

    @AfterEach
    void tearDown() {
        this.buildingRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.buildingRepository).isNotNull();
        assertThat(this.building).isNotNull();
    }

    @Test
    void test_findByAddress_isPresentWhenValidParam() {
        Optional<Building> found = this.buildingRepository.findByAddress("TestAddress");
        assertThat(found).isPresent();
    }

    @Test
    void test_findByAddress_isEmptyWhenInvalidParam() {
        Optional<Building> found = this.buildingRepository.findByAddress("invalid address");
        assertThat(found).isEmpty();
    }

    @Test
    void test_findByAddress_returnsCorrectBuilding() {
        Optional<Building> found = this.buildingRepository.findByAddress("TestAddress");
        assertThat(found).isPresent();
        assertThat(found.get().equals(this.building));
    }

    @Test
    void test_findByIdAndAddress_isPresentWhenValidParams() {
        Optional<Building> found = this.buildingRepository
                .findByIdAndAddress(this.building.getId(), "TestAddress");
        assertThat(found).isPresent();
    }

    @Test
    void test_findByIdAndAddress_isEmptyWhenInvalidParams() {
        Optional<Building> found = this.buildingRepository
                .findByIdAndAddress("0", "TestAddress");
        assertThat(found).isEmpty();

        found = this.buildingRepository
                .findByIdAndAddress(this.building.getId(), "invalid address");
        assertThat(found).isEmpty();

        found = this.buildingRepository
                .findByIdAndAddress("0", "invalid address");
        assertThat(found).isEmpty();

    }

    @Test
    void test_findByIdAndAddress_returnsCorrectBuilding() {
        Optional<Building> found = this.buildingRepository
                .findByIdAndAddress(this.building.getId(), "TestAddress");
        assertThat(found).isPresent();
        assertThat(found.get().equals(this.building));
    }

    @Test
    void test_findByNameAndNeighbourhood_isPresentWhenValidParams() {
        Optional<Building> found = this.buildingRepository
                .findByNameAndNeighbourhood("TestBuilding 1", "Test neighbourhood");
        assertThat(found).isPresent();
    }

    @Test
    void test_findByNameAndNeighbourhood_isEmptyWhenInvalidParams() {
        Optional<Building> found = this.buildingRepository
                .findByNameAndNeighbourhood("invalid name", "Test neighbourhood");
        assertThat(found).isEmpty();

        found = this.buildingRepository
                .findByNameAndNeighbourhood("TestBuilding 1", "invalid neighbourhood");
        assertThat(found).isEmpty();

        found = this.buildingRepository
                .findByNameAndNeighbourhood("invalid name", "invalid neighbourhood");
        assertThat(found).isEmpty();
    }

    @Test
    void test_findByNameAndNeighbourhood_returnsCorrectBuilding() {
        Optional<Building> found = this.buildingRepository
                .findByNameAndNeighbourhood("TestBuilding 1", "Test neighbourhood");
        assertThat(found).isPresent();
        assertThat(found.get().equals(this.building));
    }

    @Test
    void test_findAllByIdIn_isNotEmptyWhenFound() {
        Set<Building> buildings = this.buildingRepository
                .findAllByIdIn(Set.of(this.building.getId()));

        assertThat(!buildings.isEmpty());
        assertThat(buildings.size() == 1);
    }

    @Test
    void test_findAllByIdIn_isEmptyWhenNotFound() {
        Set<Building> buildings = this.buildingRepository
                .findAllByIdIn(Set.of("0"));

        assertThat(buildings.isEmpty());
    }
}