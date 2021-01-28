package com.syn.domo.repository;

import com.syn.domo.config.ApplicationBeanConfigurationTest;
import com.syn.domo.model.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ApplicationBeanConfigurationTest.class)
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

    private String CHILD_ID;
    private String FIRST_NAME;
    private String LAST_NAME;
    private String APARTMENT_ID;

    @BeforeEach
    void setUp() {
        this.tearDown();

        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        Apartment apartment = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment);

        Role role = new Role(UserRole.RESIDENT);
        this.roleRepository.saveAndFlush(role);

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

        Child child = new Child("Child 1", "Doe", LocalDate.now(),
                apartment, Set.of(parent1, parent2));
        this.childRepository.saveAndFlush(child);

        APARTMENT_ID = apartment.getId();
        CHILD_ID = child.getId();
        FIRST_NAME = child.getFirstName();
        LAST_NAME = child.getLastName();
    }

    @AfterEach
    void tearDown() {
        this.childRepository.deleteAll();
        this.residentRepository.deleteAll();
        this.roleRepository.deleteAll();
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
    }

    @Test
    void test_findByFirstNameAndLastNameAndApartmentId_isPresent() {
        Optional<Child> found = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId(FIRST_NAME, LAST_NAME, APARTMENT_ID);

        assertThat(found).isPresent();
        assertEquals(found.get().getId(), CHILD_ID);
        assertEquals(found.get().getFirstName(), FIRST_NAME);
        assertEquals(found.get().getLastName(), LAST_NAME);
        assertEquals(found.get().getApartment().getId(), APARTMENT_ID);
    }

    @Test
    void test_findByFirstNameAndLastNameAndApartmentId_isEmpty() {
        Optional<Child> found = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId(FIRST_NAME, LAST_NAME, "0");
        assertThat(found).isEmpty();

        found = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId("invalid", LAST_NAME, APARTMENT_ID);
        assertThat(found).isEmpty();

        found = this.childRepository
                .findByFirstNameAndLastNameAndApartmentId(FIRST_NAME, "invalid", APARTMENT_ID);
        assertThat(found).isEmpty();
    }

    @Test
    void test_severParentRelations_success() {
        int result = this.childRepository.severParentRelations(CHILD_ID);

        assertTrue(result > 0);
    }

    @Test
    void test_severParentRelations_fail() {
        int result = this.childRepository.severParentRelations("invalidId");

        assertEquals(result, 0);
    }
}