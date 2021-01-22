package com.syn.domo.repository;

import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.entity.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StaffRepositoryTest {

    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StaffRepository staffRepository;

    private String STAFF_ID;
    private Set<String> STAFF_IDS;

    @BeforeEach
    void setUp() {
        this.tearDown();

        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        Role role = this.roleRepository.findByName(UserRole.STAFF).orElse(null);

        if (role == null) {
            role = new Role(UserRole.STAFF);
            this.roleRepository.saveAndFlush(role);
        }

        Staff staff1 = new Staff("Staff 1", "Staff 1", LocalDate.now(),
                "staff1@mail.com", null, "0383933", false, Set.of(role),
                "Job 1", BigDecimal.valueOf(500), Set.of(building));
        this.staffRepository.saveAndFlush(staff1);
        Staff staff2 = new Staff("Staff 2", "Staff 2", LocalDate.now(),
                "staff2@mail.com", null, "546464", false, Set.of(role),
                "Job 2", BigDecimal.valueOf(500), Set.of(building));
        this.staffRepository.saveAndFlush(staff2);

        STAFF_IDS = Set.of(staff1.getId(), staff2.getId());
        STAFF_ID = staff1.getId();
    }

    @AfterEach
    void tearDown() {
        this.staffRepository.deleteAll();
        this.roleRepository.deleteAll();
        this.buildingRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.buildingRepository).isNotNull();
        assertThat(this.roleRepository).isNotNull();
        assertThat(this.staffRepository).isNotNull();
        assertThat(this.STAFF_IDS).isNotNull();
    }

    @Test
    void test_findAllByIdIn_isNotEmpty() {
        Set<Staff> all = this.staffRepository.findAllByIdIn(STAFF_IDS);

        assertThat(all).isNotEmpty();
    }

    @Test
    void test_findAllByIdIn_isEmpty() {
        Set<Staff> all = this.staffRepository
                .findAllByIdIn(Set.of("invalidId1", "invalidId2"));

        assertThat(all).isEmpty();
    }

    @Test
    void test_cancelBuildingAssignments_success() {
        int result = this.staffRepository.cancelBuildingAssignments(STAFF_ID);

        assertTrue(result > 0);
    }

    @Test
    void test_cancelBuildingAssignments_fail() {
        int result = this.staffRepository.cancelBuildingAssignments("invalidId");

        assertEquals(result, 0);
    }
}