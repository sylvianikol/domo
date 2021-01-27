package com.syn.domo.web.controller;

import com.syn.domo.model.binding.UserActivateBindingModel;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserEntity;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.repository.RoleRepository;
import com.syn.domo.repository.UserRepository;
import com.syn.domo.web.AbstractTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Set;

import static com.syn.domo.common.ExceptionErrorMessages.UNPROCESSABLE_ENTITY;
import static com.syn.domo.common.ExceptionErrorMessages.USER_NOT_FOUND;
import static com.syn.domo.common.ValidationErrorMessages.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private String USER_ID;
    private final String URI = "/v1//account";

    @BeforeEach
    public void setUp() {
        this.tearDown();
        Role role = new Role(UserRole.RESIDENT);
        this.roleRepository.saveAndFlush(role);

        UserEntity user = new UserEntity("John", "Doe", LocalDate.now(),
                "test@test.mail", null, "099383838", false,
                Set.of(role));
        this.userRepository.saveAndFlush(user);

        USER_ID = user.getId();
    }

    @AfterEach
    public void tearDown() {
        this.userRepository.deleteAll();
        this.roleRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.userRepository).isNotNull();
    }

    @Test
    void test_activate_isOk() throws Exception {
        this.mvc.perform(get(URI + "/activate")
                .param("userId", USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_ID)));
    }

    @Test
    void test_activate_isNotFound() throws Exception {
        this.mvc.perform(get(URI + "/activate")
                .param("userId", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_createPassword_success() throws Exception {
        UserActivateBindingModel bindingModel = new UserActivateBindingModel();
        bindingModel.setPassword("123");
        bindingModel.setConfirmPassword("123");

        String inputJson = super.mapToJson(bindingModel);

        this.mvc.perform(post(URI + "/activate")
                .param("userId", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("/v1")));
    }

    @Test
    void test_createPassword_isUnprocessableIfInvalidData() throws Exception {
        UserActivateBindingModel bindingModel = new UserActivateBindingModel();
        bindingModel.setPassword("");
        bindingModel.setConfirmPassword("123");

        String inputJson = super.mapToJson(bindingModel);

        this.mvc.perform(post(URI + "/activate")
                .param("userId", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.password", is("")))
                .andExpect(jsonPath("$.confirmPassword", is("123")));
      }

    @Test
    void test_createPassword_isUnprocessableIfPasswordsMismatch() throws Exception {
        UserActivateBindingModel bindingModel = new UserActivateBindingModel();
        bindingModel.setPassword("124");
        bindingModel.setConfirmPassword("123");

        String inputJson = super.mapToJson(bindingModel);

        this.mvc.perform(post(URI + "/activate")
                .param("userId", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.statusCode", is(422)))
                .andExpect(jsonPath("$.message", is(UNPROCESSABLE_ENTITY)))
                .andExpect(jsonPath("$.errorContainer.errors.password[0]", is(PASSWORDS_DONT_MATCH)));
    }

    @Test
    void test_createPassword_userIdNotFound() throws Exception {
        UserActivateBindingModel bindingModel = new UserActivateBindingModel();
        bindingModel.setPassword("123");
        bindingModel.setConfirmPassword("123");

        String inputJson = super.mapToJson(bindingModel);

        this.mvc.perform(post(URI + "/activate")
                .param("userId", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(USER_NOT_FOUND)));
    }
}