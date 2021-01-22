package com.syn.domo.web.controller;

import com.syn.domo.model.binding.StaffBindingModel;
import com.syn.domo.model.entity.*;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.repository.RoleRepository;
import com.syn.domo.repository.StaffRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static com.syn.domo.common.ExceptionErrorMessages.STAFF_NOT_FOUND;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;
import static com.syn.domo.common.ValidationErrorMessages.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StaffControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private RoleRepository roleRepository;

    private String BUILDING_ID;
    private String STAFF_1_ID;
    private String STAFF_2_ID;
    private final String URI = "/v1/staff";

    @BeforeEach
    public void setUp() {
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

        BUILDING_ID = building.getId();
        STAFF_1_ID = staff1.getId();
        STAFF_2_ID = staff2.getId();
    }

    @AfterEach
    public void tearDown() {
        this.staffRepository.deleteAll();
        this.buildingRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.staffRepository).isNotNull();
        assertThat(this.buildingRepository).isNotNull();
    }

    @Test
    void test_getAll_isOK() throws Exception {
        this.mvc.perform(get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(STAFF_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(STAFF_2_ID)));
    }

    @Test
    void test_getAll_isNotFound() throws Exception {
        this.staffRepository.deleteAll();
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
                .andExpect(jsonPath("$.[0].id", is(STAFF_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(STAFF_2_ID)));
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
                .andExpect(jsonPath("$.[0].id", is(STAFF_1_ID)))
                .andExpect(jsonPath("$.[0].buildings.[0].id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[1].id", is(STAFF_2_ID)))
                .andExpect(jsonPath("$.[1].buildings.[0].id", is(BUILDING_ID)));
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
        this.staffRepository.deleteAll();
        this.mvc.perform(get(URI + "/all")
                .param("buildingId", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_get_isOK() throws Exception {

        this.mvc.perform(get(URI + "/{staffId}", STAFF_1_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test_get_notFound() throws Exception {

        this.mvc.perform(get(URI + "/{staffId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_add_isCreated() throws Exception {

        StaffBindingModel staffBindingModel =
                new StaffBindingModel("New Staff", "Staff", "new@mail.com",
                        "224234", "Job", BigDecimal.valueOf(500));

        String inputJson = super.mapToJson(staffBindingModel);

        this.mvc.perform(post(URI + "/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(getAHeaderLocation())));
    }

    @Test
    void test_add_isUnprocessable() throws Exception {
        StaffBindingModel staffBindingModel =
                new StaffBindingModel("", "Staff", "new@mail.com",
                        "224234", "Job", BigDecimal.valueOf(500));

        String inputJson = super.mapToJson(staffBindingModel);

        this.mvc.perform(post(URI + "/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    void test_edit_isNoContent() throws Exception {
        StaffBindingModel staffBindingModel =
                new StaffBindingModel("Edit Staff", "Staff", "new@mail.com",
                        "224234", "Job", BigDecimal.valueOf(500));

        String inputJson = super.mapToJson(staffBindingModel);

        this.mvc.perform(put(URI + "/{staffId}", STAFF_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION,
                        containsString(URI + "/" + STAFF_1_ID)));

    }

    @Test
    void test_edit_isUnprocessable() throws Exception {
        StaffBindingModel staffBindingModel =
                new StaffBindingModel("", "Staff", "new@mail.com",
                        "224234", "Job", BigDecimal.valueOf(500));

        String inputJson = super.mapToJson(staffBindingModel);

        this.mvc.perform(put(URI + "/{staffId}", STAFF_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.object.firstName", is("")))
                .andExpect(jsonPath("$.errorContainer.errors.firstName[0]", is(FIRST_NAME_INVALID)))
                .andExpect(jsonPath("$.errorContainer.errors.firstName[1]", is(FIRST_NAME_NOT_EMPTY)));

    }

    @Test
    void test_edit_IdInvalid_isNotFound() throws Exception {
        StaffBindingModel staffBindingModel =
                new StaffBindingModel("Edit Staff", "Staff", "new@mail.com",
                        "224234", "Job", BigDecimal.valueOf(500));

        String inputJson = super.mapToJson(staffBindingModel);

        this.mvc.perform(put(URI + "/{staffId}", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    void test_deleteAll_isOkWithCorrectMessage() throws Exception {
        this.mvc.perform(delete(URI + "/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "staff")));
    }

    @Test
    void test_deleteAll_isNotFound() throws Exception {
        this.staffRepository.deleteAll();
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
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "staff")));
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
        this.staffRepository.deleteAll();
        this.mvc.perform(delete(URI + "/delete")
                .param("buildingId", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_delete_success() throws Exception {

        this.mvc.perform(delete(URI + "/{staffId}", STAFF_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(URI)));
    }

    @Test
    void test_delete_isNotFound() throws Exception {

        this.mvc.perform(delete(URI + "/{staffId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(STAFF_NOT_FOUND));
    }

    @Test
    void test_assign_isNoContent() throws Exception {

        this.mvc.perform(put(URI + "/{staffId}/assign", STAFF_1_ID)
                .param("buildingIds", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION,
                        containsString(URI + "/" + STAFF_1_ID)));
    }

    @Test
    void test_assign_staffIdInvalid_isNotFound() throws Exception {

        this.mvc.perform(put(URI + "/{staffId}/assign", "0")
                .param("buildingIds", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_assign_buildingIdInvalid_isNotFound() throws Exception {

        this.mvc.perform(put(URI + "/{staffId}/assign", "0")
                .param("buildingIds", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private String getAHeaderLocation() {
        return URI + "/" +
                this.staffRepository.findAll().stream()
                        .filter(a -> !a.getId().equals(STAFF_1_ID) && !a.getId().equals(STAFF_2_ID))
                        .findFirst()
                        .map(Staff::getId)
                        .orElse("");
    }
}