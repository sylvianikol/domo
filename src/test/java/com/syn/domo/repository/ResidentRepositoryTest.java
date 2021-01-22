package com.syn.domo.repository;

import com.syn.domo.model.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ResidentRepositoryTest {

    @Autowired
    private ResidentRepository residentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    private Set<String> residentIds;

    @BeforeEach
    void setUp() {
        this.tearDown();

        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        Apartment apartment = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment);

        Role role = this.roleRepository.findByName(UserRole.RESIDENT).orElse(null);

        if (role == null) {
            role = new Role(UserRole.RESIDENT);
            this.roleRepository.saveAndFlush(role);
        }

        Resident resident1 = new Resident("John", "Doe",
                LocalDate.now(), "john@test.mail",
                null, "099383838", false,
                Set.of(role), Set.of(apartment));
        this.residentRepository.saveAndFlush(resident1);

        Resident resident2 = new Resident("Jane", "Doe",
                LocalDate.now(), "jane@test.mail",
                null, "03848393", false,
                Set.of(role), Set.of(apartment));
        this.residentRepository.saveAndFlush(resident2);

        residentIds = Set.of(resident1.getId(), resident2.getId());
    }

    @AfterEach
    void tearDown() {
        this.residentRepository.deleteAll();
        this.apartmentRepository.deleteAll();
        this.buildingRepository.deleteAll();
    }
    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.buildingRepository).isNotNull();
        assertThat(this.apartmentRepository).isNotNull();
        assertThat(this.roleRepository).isNotNull();
        assertThat(this.residentRepository).isNotNull();
        assertThat(this.residentIds).isNotNull();
    }

    @Test
    void test_findAllByIdIn_isNotEmpty() {
        Set<Resident> all = this.residentRepository
                .findAllByIdIn(residentIds);

        assertThat(!all.isEmpty());
    }

    @Test
    void test_findAllByIdIn_isEmpty() {
        Set<Resident> all = this.residentRepository
                .findAllByIdIn(Set.of("invalidId1", "invalidId2"));

        assertThat(all.isEmpty());
    }
}