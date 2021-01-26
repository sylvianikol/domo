package com.syn.domo.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.syn.domo.model.binding.ChildBindingModel;
import com.syn.domo.model.entity.*;
import com.syn.domo.repository.*;
import com.syn.domo.web.AbstractTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;
import static com.syn.domo.common.ValidationErrorMessages.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ChildrenControllerTest extends AbstractTest {

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
    @Autowired
    private ChildRepository childRepository;

    private String BUILDING_ID;
    private String APARTMENT_ID;
    private String PARENT_1_ID;
    private String PARENT_2_ID;
    private String CHILD_1_ID;
    private String CHILD_2_ID;
    private String CHILD_1_FIRST_NAME;
    private String CHILD_1_LAST_NAME;
    private String APARTMENT_NUMBER;
    private final String URI = "/v1/children";

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

        Child child1 = new Child("Child 1", "Doe", LocalDate.now(),
                apartment, Set.of(parent1, parent2));
        this.childRepository.saveAndFlush(child1);

        Child child2 = new Child("Child 2", "Doe", LocalDate.now(),
                apartment, Set.of(parent1, parent2));
        this.childRepository.saveAndFlush(child2);

        BUILDING_ID = building.getId();
        APARTMENT_ID = apartment.getId();
        PARENT_1_ID = parent1.getId();
        PARENT_2_ID = parent2.getId();
        CHILD_1_ID = child1.getId();
        CHILD_2_ID = child2.getId();
        CHILD_1_FIRST_NAME = child1.getFirstName();
        CHILD_1_LAST_NAME = child1.getLastName();
        APARTMENT_NUMBER = apartment.getNumber();
    }

    @AfterEach
    public void tearDown() {
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
    }

    @Test
    void test_getAll_isOK() throws Exception {
        this.mvc.perform(get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(CHILD_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(CHILD_2_ID)));
    }

    @Test
    void test_getAll_isNotFound() throws Exception {
        this.childRepository.deleteAll();
        this.mvc.perform(get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getAll_withPageable_IsOK() throws Exception {

        this.mvc.perform(get(URI + "/all")
                .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(CHILD_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(CHILD_2_ID)));
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
                .andExpect(jsonPath("$.[0].id", is(CHILD_1_ID)))
                .andExpect(jsonPath("$.[0].apartment.building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[1].id", is(CHILD_2_ID)))
                .andExpect(jsonPath("$.[1].apartment.building.id", is(BUILDING_ID)));
    }

    @Test
    void test_getAll_byBuildingIdAndApartmentId_isOk() throws Exception {
        this.mvc.perform(get(URI + "/all")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", APARTMENT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(CHILD_1_ID)))
                .andExpect(jsonPath("$.[0].apartment.building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[0].apartment.id", is(APARTMENT_ID)))
                .andExpect(jsonPath("$.[1].id", is(CHILD_2_ID)))
                .andExpect(jsonPath("$.[1].apartment.building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[1].apartment.id", is(APARTMENT_ID)));
    }

    @Test
    void test_getAll_byApartmentId_isOk() throws Exception {
        this.mvc.perform(get(URI + "/all")
                .param("apartmentId", APARTMENT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(CHILD_1_ID)))
                .andExpect(jsonPath("$.[0].apartment.id", is(APARTMENT_ID)))
                .andExpect(jsonPath("$.[1].id", is(CHILD_2_ID)))
                .andExpect(jsonPath("$.[1].apartment.id", is(APARTMENT_ID)));
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
        this.childRepository.deleteAll();
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
                .andExpect(jsonPath("$.[0].id", is(CHILD_1_ID)))
                .andExpect(jsonPath("$.[0].apartment.building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[1].id", is(CHILD_2_ID)))
                .andExpect(jsonPath("$.[1].apartment.building.id", is(BUILDING_ID)));
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
                .andExpect(jsonPath("$.[0].id", is(CHILD_1_ID)))
                .andExpect(jsonPath("$.[0].apartment.id", is(APARTMENT_ID)))
                .andExpect(jsonPath("$.[1].id", is(CHILD_2_ID)))
                .andExpect(jsonPath("$.[1].apartment.id", is(APARTMENT_ID)));
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

        this.mvc.perform(get(URI + "/{childId}", CHILD_1_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test_get_notFound() throws Exception {

        this.mvc.perform(get(URI + "/{childId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_add_isCreated() throws Exception {

        ChildBindingModel childBindingModel = new ChildBindingModel();
        childBindingModel.setFirstName("Chucky");
        childBindingModel.setLastName("Doll");

        String inputJson = super.mapToJson(childBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", APARTMENT_ID)
                .param("parentIds", PARENT_1_ID, PARENT_2_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(getAHeaderLocation())));
    }

    @Test
    void test_add_isUnprocessable() throws Exception {
        ChildBindingModel childBindingModel = new ChildBindingModel();
        childBindingModel.setFirstName("");
        childBindingModel.setLastName("Doll");

        String inputJson = super.mapToJson(childBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", APARTMENT_ID)
                .param("parentIds", PARENT_1_ID, PARENT_2_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.object.firstName", is("")))
                .andExpect(jsonPath("$.errorContainer.errors.firstName[0]", is(FIRST_NAME_INVALID)))
                .andExpect(jsonPath("$.errorContainer.errors.firstName[1]", is(FIRST_NAME_NOT_EMPTY)));
    }

    @Test
    void test_add_withBuildingIdInvalid_isNotFound() throws Exception {
        ChildBindingModel childBindingModel = new ChildBindingModel();
        childBindingModel.setFirstName("Chucky");
        childBindingModel.setLastName("Doll");

        String inputJson = super.mapToJson(childBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", "0")
                .param("apartmentId", APARTMENT_ID)
                .param("parentIds", PARENT_1_ID, PARENT_2_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(BUILDING_NOT_FOUND));
    }

    @Test
    void test_add_withApartmentIdInvalid_isNotFound() throws Exception {
        ChildBindingModel childBindingModel = new ChildBindingModel();
        childBindingModel.setFirstName("Chucky");
        childBindingModel.setLastName("Doll");

        String inputJson = super.mapToJson(childBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", "0")
                .param("parentIds", PARENT_1_ID, PARENT_2_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(APARTMENT_NOT_FOUND));
    }

    @Test
    void test_add_withNoParents_isUnprocessable() throws Exception {
        ChildBindingModel childBindingModel = new ChildBindingModel();
        childBindingModel.setFirstName("Chucky");
        childBindingModel.setLastName("Doll");

        String inputJson = super.mapToJson(childBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", APARTMENT_ID)
                .param("parentIds", "", "")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(PARENTS_NOT_FOUND));
    }

    @Test
    void test_add_withParentIdsInvalid_isUnprocessable() throws Exception {
        ChildBindingModel childBindingModel = new ChildBindingModel();
        childBindingModel.setFirstName("Chucky");
        childBindingModel.setLastName("Doll");

        String inputJson = super.mapToJson(childBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", APARTMENT_ID)
                .param("parentIds", "0", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(PARENTS_NOT_FOUND));
    }

    @Test
    void test_add_throwsIfChildExistsInApartment() throws Exception {
        ChildBindingModel childToAdd = new ChildBindingModel();
        childToAdd.setFirstName(CHILD_1_FIRST_NAME);
        childToAdd.setLastName(CHILD_1_LAST_NAME);

        String inputJson = super.mapToJson(childToAdd);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", APARTMENT_ID)
                .param("parentIds", PARENT_1_ID, PARENT_2_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(String.format(CHILD_ALREADY_EXISTS,
                        CHILD_1_FIRST_NAME, CHILD_1_LAST_NAME, APARTMENT_NUMBER)));
    }

    @Test
    void test_edit_isNoContent() throws Exception {
        ChildBindingModel childBindingModel = new ChildBindingModel();
        childBindingModel.setFirstName("Edit Name");
        childBindingModel.setLastName("Doll");

        String inputJson = super.mapToJson(childBindingModel);

        this.mvc.perform(put(URI + "/{childId}", CHILD_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION,
                        containsString(URI + "/" + CHILD_1_ID)));

    }

    @Test
    void test_edit_isUnprocessable() throws Exception {
        ChildBindingModel childBindingModel = new ChildBindingModel();
        childBindingModel.setFirstName("");
        childBindingModel.setLastName("Doll");

        String inputJson = super.mapToJson(childBindingModel);

        this.mvc.perform(put(URI + "/{childId}", CHILD_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorContainer.errors.firstName[0]", is(FIRST_NAME_INVALID)))
                .andExpect(jsonPath("$.errorContainer.errors.firstName[1]", is(FIRST_NAME_NOT_EMPTY)));

    }

    @Test
    void test_edit_IdInvalid_isNotFound() throws Exception {
        ChildBindingModel childBindingModel = new ChildBindingModel();
        childBindingModel.setFirstName("Chucky");
        childBindingModel.setLastName("Doll");

        String inputJson = super.mapToJson(childBindingModel);

        this.mvc.perform(put(URI + "/{childId}", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(CHILD_NOT_FOUND));

    }

    @Test
    void test_deleteAll_isOkWithCorrectMessage() throws Exception {
        this.mvc.perform(delete(URI + "/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "children")));
    }

    @Test
    void test_deleteAll_isNotFound() throws Exception {
        this.childRepository.deleteAll();
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
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "children")));
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
        this.childRepository.deleteAll();
        this.mvc.perform(delete(URI + "/delete")
                .param("buildingId", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_delete_success() throws Exception {

        this.mvc.perform(delete(URI + "/{childId}", CHILD_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION,
                containsString(URI)));
    }

    @Test
    void test_delete_isNotFound() throws Exception {

        this.mvc.perform(delete(URI + "/{childId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(CHILD_NOT_FOUND));
    }

    private String getAHeaderLocation() {
        return URI + "/" +
                this.childRepository.findAll().stream()
                        .filter(a -> !a.getId().equals(CHILD_1_ID) && !a.getId().equals(CHILD_2_ID))
                        .findFirst()
                        .map(Child::getId)
                        .orElse("");
    }
}