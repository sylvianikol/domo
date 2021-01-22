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
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BuildingRepositoryTest {

    @Autowired
    private BuildingRepository buildingRepository;

    private String BUILDING_ID;
    private String BUILDING_ADDRESS;
    private String BUILDING_NAME;
    private String BUILDING_NEIGHBOURHOOD;

    @BeforeEach
    void setUp() {
        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        BUILDING_ID = building.getId();
        BUILDING_ADDRESS = building.getAddress();
        BUILDING_NAME = building.getName();
        BUILDING_NEIGHBOURHOOD = building.getNeighbourhood();
    }

    @AfterEach
    void tearDown() {
        this.buildingRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.buildingRepository).isNotNull();
    }

    @Test
    void test_findByAddress_isPresent() {
        Optional<Building> found = this.buildingRepository
                .findByAddress(BUILDING_ADDRESS);
        assertThat(found).isPresent();
        assertEquals(found.get().getId(), BUILDING_ID);
        assertEquals(found.get().getAddress(), BUILDING_ADDRESS);
    }

    @Test
    void test_findByAddress_isEmpty() {
        Optional<Building> found = this.buildingRepository.findByAddress("invalid address");
        assertThat(found).isEmpty();
    }

    @Test
    void test_findByIdAndAddress_isPresent() {
        Optional<Building> found = this.buildingRepository
                .findByIdAndAddress(BUILDING_ID, BUILDING_ADDRESS);
        assertThat(found).isPresent();
        assertEquals(found.get().getId(), BUILDING_ID);
        assertEquals(found.get().getAddress(), BUILDING_ADDRESS);
    }

    @Test
    void test_findByIdAndAddress_isEmpty() {
        Optional<Building> found = this.buildingRepository
                .findByIdAndAddress("0", BUILDING_ADDRESS);
        assertThat(found).isEmpty();

        found = this.buildingRepository
                .findByIdAndAddress(BUILDING_ID, "invalid address");
        assertThat(found).isEmpty();

        found = this.buildingRepository
                .findByIdAndAddress("0", "invalid address");
        assertThat(found).isEmpty();

    }

    @Test
    void test_findByNameAndNeighbourhood_isPresent() {
        Optional<Building> found = this.buildingRepository
                .findByNameAndNeighbourhood(BUILDING_NAME, BUILDING_NEIGHBOURHOOD);
        assertThat(found).isPresent();
        assertEquals(found.get().getId(), BUILDING_ID);
        assertEquals(found.get().getName(), BUILDING_NAME);
        assertEquals(found.get().getNeighbourhood(), BUILDING_NEIGHBOURHOOD);
    }

    @Test
    void test_findByNameAndNeighbourhood_isEmpty() {
        Optional<Building> found = this.buildingRepository
                .findByNameAndNeighbourhood("invalid name", BUILDING_NEIGHBOURHOOD);
        assertThat(found).isEmpty();

        found = this.buildingRepository
                .findByNameAndNeighbourhood(BUILDING_NAME, "invalid neighbourhood");
        assertThat(found).isEmpty();

        found = this.buildingRepository
                .findByNameAndNeighbourhood("invalid name", "invalid neighbourhood");
        assertThat(found).isEmpty();
    }

    @Test
    void test_findAllByIdIn_isNotEmpty() {
        Set<Building> buildings = this.buildingRepository
                .findAllByIdIn(Set.of(BUILDING_ID));

        assertThat(!buildings.isEmpty());
        assertEquals(buildings.size(), 1);
    }

    @Test
    void test_findAllByIdIn_isEmpty() {
        Set<Building> buildings = this.buildingRepository
                .findAllByIdIn(Set.of("0"));

        assertThat(buildings.isEmpty());
    }
}