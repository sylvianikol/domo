package com.syn.domo.web.controller;

import com.syn.domo.model.binding.ChildBindingModel;
import com.syn.domo.model.binding.ResidentBindingModel;
import com.syn.domo.model.entity.*;
import com.syn.domo.repository.*;
import com.syn.domo.web.AbstractTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;
import static com.syn.domo.common.ValidationErrorMessages.FIRST_NAME_INVALID;
import static com.syn.domo.common.ValidationErrorMessages.FIRST_NAME_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ResidentsControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ResidentRepository residentRepository;

    private String BUILDING_ID;
    private String APARTMENT_ID;
    private String RESIDENT_1_ID;
    private String RESIDENT_2_ID;
    private final String URI = "/v1/residents";

    @BeforeEach
    public void setUp() {
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

        BUILDING_ID = building.getId();
        APARTMENT_ID = apartment.getId();

        List<Resident> all = this.residentRepository.findAll(Sort.by("id"));
        RESIDENT_1_ID = all.get(0).getId();
        RESIDENT_2_ID = all.get(1).getId();
    }

    @AfterEach
    public void tearDown() {
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
    }

    @Test
    void test_getAll_isOK() throws Exception {
        List<Resident> all = this.residentRepository.findAll();
        this.mvc.perform(get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(all.get(0).getId())))
                .andExpect(jsonPath("$.[1].id", is(all.get(1).getId())));
    }

    @Test
    void test_getAll_isNotFound() throws Exception {
        this.residentRepository.deleteAll();
        this.mvc.perform(get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getAll_withPageable_IsOK() throws Exception {
        List<Resident> all = this.residentRepository.findAll();
        this.mvc.perform(get(URI + "/all")
                .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(all.get(0).getId())))
                .andExpect(jsonPath("$.[1].id", is(all.get(1).getId())));
    }

    @Test
    void test_getAll_withPageable_IsNotFound() throws Exception {

        this.mvc.perform(get(URI + "/all")
                .param("page", "100"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getAll_byBuildingId_isOk() throws Exception {

        this.mvc.perform(get(URI + "/all")
                .param("buildingId", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(RESIDENT_1_ID)))
                .andExpect(jsonPath("$.[0].apartments.[0].building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[1].id", is(RESIDENT_2_ID)))
                .andExpect(jsonPath("$.[1].apartments.[0].building.id", is(BUILDING_ID)));
    }

    @Test
    void test_getAll_byBuildingIdAndApartmentId_isOk() throws Exception {

        this.mvc.perform(get(URI + "/all")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", APARTMENT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(RESIDENT_1_ID)))
                .andExpect(jsonPath("$.[0].apartments.[0].id", is(APARTMENT_ID)))
                .andExpect(jsonPath("$.[0].apartments.[0].building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[1].id", is(RESIDENT_2_ID)))
                .andExpect(jsonPath("$.[1].apartments.[0].id", is(APARTMENT_ID)))
                .andExpect(jsonPath("$.[1].apartments.[0].building.id", is(BUILDING_ID)));
    }

    @Test
    void test_getAll_byApartmentId_isOk() throws Exception {

        this.mvc.perform(get(URI + "/all")
                .param("apartmentId", APARTMENT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(RESIDENT_1_ID)))
                .andExpect(jsonPath("$.[0].apartments.[0].id", is(APARTMENT_ID)))
                .andExpect(jsonPath("$.[1].id", is(RESIDENT_2_ID)))
                .andExpect(jsonPath("$.[1].apartments.[0].id", is(APARTMENT_ID)));
    }

    @Test
    void test_getAll_byBuildingIdInvalid_isNotFound() throws Exception {
        this.mvc.perform(get(URI + "/all")
                .param("buildingId", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getAll_byApartmentIdInvalid_isNotFound() throws Exception {
        this.mvc.perform(get(URI + "/all")
                .param("apartmentId", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getAll_byApartmentIdEmpty_isNotFound() throws Exception {
        this.residentRepository.deleteAll();
        this.mvc.perform(get(URI + "/all")
                .param("apartmentId", APARTMENT_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getAll_byBuildingId_withPageable_IsOk() throws Exception {

        this.mvc.perform(get(URI + "/all")
                .param("buildingId", BUILDING_ID)
                .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(RESIDENT_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(RESIDENT_2_ID)));
    }

    @Test
    void test_getAll_byBuildingId_withPageable_IsNotFound() throws Exception {

        this.mvc.perform(get(URI + "/all")
                .param("buildingId", BUILDING_ID)
                .param("page", "100"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getAll_byApartmentId_withPageable_IsOk() throws Exception {

        this.mvc.perform(get(URI + "/all")
                .param("apartmentId", APARTMENT_ID)
                .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(RESIDENT_1_ID)))
                .andExpect(jsonPath("$.[0].apartments.[0].id", is(APARTMENT_ID)))
                .andExpect(jsonPath("$.[1].id", is(RESIDENT_2_ID)))
                .andExpect(jsonPath("$.[1].apartments.[0].id", is(APARTMENT_ID)));
    }

    @Test
    void test_getAll_byApartmentId_withPageable_IsNotFound() throws Exception {

        this.mvc.perform(get(URI + "/all")
                .param("apartmentId", APARTMENT_ID)
                .param("page", "100"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_get_isOK() throws Exception {

        this.mvc.perform(get(URI + "/{residentId}", RESIDENT_1_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test_get_notFound() throws Exception {

        this.mvc.perform(get(URI + "/{residentId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_add_isCreated() throws Exception {
        ResidentBindingModel residentBindingModel = new ResidentBindingModel(
                "Bob", "Hope", "hope@mail.com", "044838383");

        String inputJson = super.mapToJson(residentBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", APARTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(getAHeaderLocation())));
    }

    @Test
    void test_add_isUnprocessable() throws Exception {
        ResidentBindingModel residentBindingModel = new ResidentBindingModel(
                "", "Hope", "hope@mail.com", "044838383");

        String inputJson = super.mapToJson(residentBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", APARTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.object.firstName", is("")))
                .andExpect(jsonPath("$.errorContainer.errors.firstName[0]", is(FIRST_NAME_INVALID)))
                .andExpect(jsonPath("$.errorContainer.errors.firstName[1]", is(FIRST_NAME_NOT_EMPTY)));
    }

    @Test
    void test_add_withBuildingIdInvalid_isNotFound() throws Exception {
        ResidentBindingModel residentBindingModel = new ResidentBindingModel(
                "Bob", "Hope", "hope@mail.com", "044838383");

        String inputJson = super.mapToJson(residentBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", "0")
                .param("apartmentId", APARTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(BUILDING_NOT_FOUND));
    }

    @Test
    void test_add_withApartmentIdInvalid_isNotFound() throws Exception {
        ResidentBindingModel residentBindingModel = new ResidentBindingModel(
                "Bob", "Hope", "hope@mail.com", "044838383");

        String inputJson = super.mapToJson(residentBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(APARTMENT_NOT_FOUND));
    }

    @Test
    void test_edit_isNoContent() throws Exception {
        ResidentBindingModel residentBindingModel = new ResidentBindingModel(
                "Edit Name", "Hope", "hope@mail.com", "044838383");

        String inputJson = super.mapToJson(residentBindingModel);

        this.mvc.perform(put(URI + "/{residentId}", RESIDENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION,
                        containsString(URI + "/" + RESIDENT_1_ID)));

    }

    @Test
    void test_edit_isUnprocessable() throws Exception {
        ResidentBindingModel residentBindingModel = new ResidentBindingModel(
                "", "Hope", "hope@mail.com", "044838383");

        String inputJson = super.mapToJson(residentBindingModel);

        this.mvc.perform(put(URI + "/{residentId}", RESIDENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorContainer.errors.firstName[0]", is(FIRST_NAME_INVALID)))
                .andExpect(jsonPath("$.errorContainer.errors.firstName[1]", is(FIRST_NAME_NOT_EMPTY)));

    }

    @Test
    void test_edit_IdInvalid_isNotFound() throws Exception {
        ResidentBindingModel residentBindingModel = new ResidentBindingModel(
                "Edit Name", "Hope", "hope@mail.com", "044838383");

        String inputJson = super.mapToJson(residentBindingModel);

        this.mvc.perform(put(URI + "/{residentId}", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(RESIDENT_NOT_FOUND));

    }

    @Test
    void test_deleteAll_isOkWithCorrectMessage() throws Exception {
        this.mvc.perform(delete(URI + "/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "residents")));
    }

    @Test
    void test_deleteAll_isNotFound() throws Exception {
        this.residentRepository.deleteAll();
        this.mvc.perform(delete(URI + "/delete"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_deleteAll_byBuildingId_isOkWithCorrectMessage() throws Exception {
        this.mvc.perform(delete(URI + "/delete")
                .param("buildingId", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "residents")));
    }

    @Test
    void test_deleteAll_byBuildingIdInvalid_isNotFound() throws Exception {
        this.mvc.perform(delete(URI + "/delete")
                .param("buildingId", "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_deleteAll_byBuildingIdEmpty_isNotFound() throws Exception {
        this.residentRepository.deleteAll();
        this.mvc.perform(delete(URI + "/delete")
                .param("buildingId", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_delete_success() throws Exception {

        this.mvc.perform(delete(URI + "/{residentId}", RESIDENT_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(URI)));
    }

    @Test
    void test_delete_isNotFound() throws Exception {

        this.mvc.perform(delete(URI + "/{residentId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(RESIDENT_NOT_FOUND));
    }

    private String getAHeaderLocation() {
        return URI + "/" +
                this.residentRepository.findAll().stream()
                        .filter(a -> !a.getId().equals(RESIDENT_1_ID) && !a.getId().equals(RESIDENT_2_ID))
                        .findFirst()
                        .map(Resident::getId)
                        .orElse("");
    }
}