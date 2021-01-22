package com.syn.domo.repository;

import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        this.tearDown();
        this.roleRepository.saveAndFlush(new Role(UserRole.RESIDENT));
    }

    @AfterEach
    void tearDown() {
        this.roleRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.roleRepository).isNotNull();
    }

    @Test
    void test_findByName_isPresent() {
        Optional<Role> found = this.roleRepository.findByName(UserRole.RESIDENT);

        assertThat(found).isPresent();
    }

    @Test
    void test_findByName_isEmpty() {
        Optional<Role> found = this.roleRepository.findByName(null);

        assertThat(found).isEmpty();
    }
}