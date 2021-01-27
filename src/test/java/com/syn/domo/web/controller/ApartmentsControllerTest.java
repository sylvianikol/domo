package com.syn.domo.web.controller;

import com.syn.domo.model.binding.ApartmentBindingModel;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.repository.ApartmentRepository;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.web.AbstractTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;
import static com.syn.domo.common.ValidationErrorMessages.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApartmentsControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    private String BUILDING_ID;
    private String BUILDING_NAME;
    private String APARTMENT_1_ID;
    private String APARTMENT_2_ID;
    private final String URI = "/v1/apartments";

    @BeforeEach
    public void setUp() {
        this.tearDown();

        Building building = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building);

        Apartment apartment1 = new Apartment("1", 1, building, 0, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment1);
        Apartment apartment2 = new Apartment("2", 1, building, 1, LocalDate.now());
        this.apartmentRepository.saveAndFlush(apartment2);

        BUILDING_ID = building.getId();
        BUILDING_NAME = building.getName();
        APARTMENT_1_ID = apartment1.getId();
        APARTMENT_2_ID = apartment2.getId();
    }

    @AfterEach
    public void tearDown() {
        this.apartmentRepository.deleteAll();
        this.buildingRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.apartmentRepository).isNotNull();
        assertThat(this.buildingRepository).isNotNull();
    }

    @Test
    void test_getAll_isOK() throws Exception {
        this.mvc.perform(get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(APARTMENT_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(APARTMENT_2_ID)));
    }

    @Test
    void test_getAll_isNotFound() throws Exception {
        this.apartmentRepository.deleteAll();
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
                .andExpect(jsonPath("$.[0].id", is(APARTMENT_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(APARTMENT_2_ID)));
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
                .andExpect(jsonPath("$.[0].id", is(APARTMENT_1_ID)))
                .andExpect(jsonPath("$.[0].building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[1].id", is(APARTMENT_2_ID)))
                .andExpect(jsonPath("$.[1].building.id", is(BUILDING_ID)));
    }

    @Test
    void test_getAll_byBuildingIdInvalid_isNotFound() throws Exception {
        this.mvc.perform(get(URI + "/all")
                .param("buildingId", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getAll_byBuildingIdEmpty_isNotFound() throws Exception {
        this.apartmentRepository.deleteAll();
        this.mvc.perform(get(URI + "/all")
                .param("buildingId", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_get_isOK() throws Exception {

        this.mvc.perform(get(URI + "/{apartmentId}", APARTMENT_1_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test_get_notFound() throws Exception {

        this.mvc.perform(get(URI + "/{apartmentId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_add_isCreated() throws Exception {

        ApartmentBindingModel apartmentBindingModel = new ApartmentBindingModel();
        apartmentBindingModel.setNumber("3");
        apartmentBindingModel.setFloor(2);
        apartmentBindingModel.setPets(0);

        String inputJson = super.mapToJson(apartmentBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(getAHeaderLocation())));
    }

    @Test
    void test_add_isUnprocessable_ifInvalidData() throws Exception {
        ApartmentBindingModel apartmentBindingModel = new ApartmentBindingModel();
        apartmentBindingModel.setNumber("");
        apartmentBindingModel.setFloor(-1);
        apartmentBindingModel.setPets(-1);

        String inputJson = super.mapToJson(apartmentBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.number", is("")));
    }

    @Test
    void test_add_isNotFound_ifBuildingIdInvalid() throws Exception {
        ApartmentBindingModel apartmentBindingModel = new ApartmentBindingModel();
        apartmentBindingModel.setNumber("3");
        apartmentBindingModel.setFloor(1);
        apartmentBindingModel.setPets(1);

        String inputJson = super.mapToJson(apartmentBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(BUILDING_NOT_FOUND)));
    }

    @Test
    void test_add_isConflictIfApartmentNumberExists() throws Exception {
        ApartmentBindingModel apartment = new ApartmentBindingModel();
        apartment.setNumber("1");
        apartment.setFloor(1);
        apartment.setPets(1);

        String inputJson = super.mapToJson(apartment);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode", is(409)))
                .andExpect(jsonPath("$.message", is(ENTITY_EXISTS)))
                .andExpect(jsonPath("$.errorContainer.errors.number[0]",
                        is(String.format(APARTMENT_EXISTS, "1", BUILDING_NAME))));
    }

    @Test
    void test_add_isConflictIfFloorInvalid() throws Exception {
        ApartmentBindingModel apartmentBindingModel = new ApartmentBindingModel();
        apartmentBindingModel.setNumber("3");
        apartmentBindingModel.setFloor(10);
        apartmentBindingModel.setPets(1);

        String inputJson = super.mapToJson(apartmentBindingModel);

        this.mvc.perform(post(URI + "/add")
                .param("buildingId", BUILDING_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.statusCode", is(422)))
                .andExpect(jsonPath("$.message", is(UNPROCESSABLE_ENTITY)))
                .andExpect(jsonPath("$.errorContainer.errors.floor[0]", is(FLOOR_INVALID)));
    }

    @Test
    void test_edit_isNoContent() throws Exception {
        ApartmentBindingModel apartmentBindingModel = new ApartmentBindingModel();
        apartmentBindingModel.setNumber("3");
        apartmentBindingModel.setFloor(2);
        apartmentBindingModel.setPets(0);

        String inputJson = super.mapToJson(apartmentBindingModel);

        this.mvc.perform(put(URI + "/{apartmentId}", APARTMENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION,
                        containsString(URI + "/" + APARTMENT_1_ID)));

    }

    @Test
    void test_edit_isUnprocessable() throws Exception {
        ApartmentBindingModel apartmentBindingModel = new ApartmentBindingModel();
        apartmentBindingModel.setNumber("");
        apartmentBindingModel.setFloor(-1);
        apartmentBindingModel.setPets(-1);

        String inputJson = super.mapToJson(apartmentBindingModel);

        this.mvc.perform(put(URI + "/{apartmentId}", APARTMENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.number", is("")));
    }

    @Test
    void test_edit_isNotFound_ifIdInvalid() throws Exception {
        ApartmentBindingModel apartmentBindingModel = new ApartmentBindingModel();
        apartmentBindingModel.setNumber("3");
        apartmentBindingModel.setFloor(2);
        apartmentBindingModel.setPets(0);

        String inputJson = super.mapToJson(apartmentBindingModel);

        this.mvc.perform(put(URI + "/{apartmentId}", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    void test_edit_isUnprocessable_ifFloorInvalid() throws Exception {
        ApartmentBindingModel apartmentBindingModel = new ApartmentBindingModel();
        apartmentBindingModel.setNumber("1");
        apartmentBindingModel.setFloor(100);
        apartmentBindingModel.setPets(1);

        String inputJson = super.mapToJson(apartmentBindingModel);

        this.mvc.perform(put(URI + "/{apartmentId}", APARTMENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorContainer.errors.floor.[0]", is(FLOOR_INVALID)));
    }

    @Test
    void test_edit_isConflict_ifDuplicate() throws Exception {
        ApartmentBindingModel apartment = new ApartmentBindingModel();
        apartment.setNumber("2");
        apartment.setFloor(1);
        apartment.setPets(1);

        String inputJson = super.mapToJson(apartment);

        this.mvc.perform(put(URI + "/{apartmentId}", APARTMENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode", is(409)))
                .andExpect(jsonPath("$.message", is(ENTITY_EXISTS)))
                .andExpect(jsonPath("$.errorContainer.errors.duplicate[0]",
                        is(String.format(APARTMENT_EXISTS,apartment.getNumber(), BUILDING_NAME))));

    }

    @Test
    void test_deleteAll_isOkWithCorrectMessage() throws Exception {
        this.mvc.perform(delete(URI + "/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "apartments")));
    }

    @Test
    void test_deleteAll_isNotFound() throws Exception {
        this.apartmentRepository.deleteAll();
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
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "apartments")));
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
        this.apartmentRepository.deleteAll();
        this.mvc.perform(delete(URI + "/delete")
                .param("buildingId", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_delete_success() throws Exception {

        this.mvc.perform(delete(URI + "/{apartmentId}", APARTMENT_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION,
                containsString(URI)));
    }

    @Test
    void test_delete_isNotFound() throws Exception {

        this.mvc.perform(delete(URI + "/{apartmentId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(APARTMENT_NOT_FOUND)));
    }

    private String getAHeaderLocation() {
        return URI + "/" +
                this.apartmentRepository.findAll().stream()
                        .filter(a -> !a.getId().equals(APARTMENT_1_ID) && !a.getId().equals(APARTMENT_2_ID))
                        .findFirst()
                        .map(Apartment::getId)
                        .orElse("");
    }
}