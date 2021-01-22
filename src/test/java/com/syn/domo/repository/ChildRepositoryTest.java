package com.syn.domo.repository;

import com.syn.domo.model.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChildRepositoryTest {

    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private ResidentRepository residentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    private Child child;
    private String APARTMENT_ID;

    @BeforeEach
    void setUp() {
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

        Resident parent1 = new Resident("John", "Doe",
                LocalDate.now(), "john@test.mail",
                null, "099383838", false,
                Set.of(role), Set.of(apartment));
        this.residentRepository.saveAndFlush(parent1);

        Resident parent2 = new Resident("Jane", "Doe",
                LocalDate.now(), "jane@test.mail",
                null, "03848393", false,
                Set.of(role), Set.of(apartment));
        this.residentRepository.saveAndFlush(parent2);

        child = new Child("Child 1", "Doe", LocalDate.now(),
                apartment, Set.of(parent1, parent2));
        this.childRepository.saveAndFlush(child);

        APARTMENT_ID = apartment.getId();
    }

    @AfterEach
    void tearDown() {
        this.childRepository.deleteAll();
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
        assertThat(this.childRepository).isNotNull();
        assertThat(this.child).isNotNull();
        assertThat(this.APARTMENT_ID).isNotNull();
    }

    @Test
    void test_findByFirstNameAndLastNameAndApartmentId_isPresent() {
        Optional<Child> found = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId(this.child.getFirstName(),
                        this.child.getLastName(), APARTMENT_ID);
        assertThat(found).isPresent();
        assertThat(found.get().getId().equals(this.child.getId()));
    }

    @Test
    void test_findByFirstNameAndLastNameAndApartmentId_isEmpty() {
        Optional<Child> found = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId(this.child.getFirstName(),
                        this.child.getLastName(), "0");
        assertThat(found).isEmpty();

        found = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId("invalid",
                        this.child.getLastName(), APARTMENT_ID);
        assertThat(found).isEmpty();

        found = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId(this.child.getFirstName(),
                        "invalid", APARTMENT_ID);
        assertThat(found).isEmpty();
    }

    @Test
    void test_severParentRelations_success() {
        int result = this.childRepository.severParentRelations(this.child.getId());

        assertTrue(result > 0);
    }

    @Test
    void test_severParentRelations_fail() {
        int result = this.childRepository.severParentRelations("invalidId");

        assertEquals(result, 0);
    }
}