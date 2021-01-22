package com.syn.domo.repository;

import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserEntity;
import com.syn.domo.model.entity.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private String USER_EMAIL;
    private String PHONE_NUMBER;

    @BeforeEach
    void setUp() {
        this.tearDown();

        Role role = new Role(UserRole.STAFF);
        this.roleRepository.saveAndFlush(role);

        UserEntity user = new UserEntity("John", "Doe", LocalDate.now(), "john@mail.com",
                null, "839339", false, Set.of(role));
        this.userRepository.saveAndFlush(user);

        USER_EMAIL = user.getEmail();
        PHONE_NUMBER = user.getPhoneNumber();
    }

    @AfterEach
    void tearDown() {
        this.userRepository.deleteAll();
        this.roleRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.userRepository).isNotNull();
        assertThat(this.roleRepository).isNotNull();
    }

    @Test
    void test_findByEmail_isPresent() {
        Optional<UserEntity> found = this.userRepository.findByEmail(USER_EMAIL);

        assertThat(found).isPresent();
        assertEquals(found.get().getEmail(), USER_EMAIL);
    }

    @Test
    void test_findByEmail_isEmpty() {
        Optional<UserEntity> found = this.userRepository.findByEmail("invalid@Email");

        assertThat(found).isEmpty();
    }

    @Test
    void test_findByPhoneNumber_isPresent() {
        Optional<UserEntity> found = this.userRepository.findByPhoneNumber(PHONE_NUMBER);

        assertThat(found).isPresent();
        assertEquals(found.get().getPhoneNumber(), PHONE_NUMBER);
    }

    @Test
    void test_findByPhoneNumber_isEmpty() {
        Optional<UserEntity> found = this.userRepository.findByPhoneNumber("invalidPhoneNumber");

        assertThat(found).isEmpty();
    }

}