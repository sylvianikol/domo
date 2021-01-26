package com.syn.domo.web.controller;

import com.syn.domo.common.ExceptionErrorMessages;
import com.syn.domo.model.binding.BuildingBindingModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.entity.UserRole;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.syn.domo.common.ExceptionErrorMessages.BUILDING_NOT_FOUND;
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
class BuildingsControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StaffRepository staffRepository;

    private String BUILDING_1_ID;
    private String BUILDING_2_ID;
    private String BUILDING_1_NAME;
    private String BUILDING_2_NAME;
    private String NEIGHBOURHOOD;
    private String BUILDING_2_ADDRESS;
    private String STAFF_1_ID;
    private String STAFF_2_ID;
    private final String URI = "/v1/buildings";

    @BeforeEach
    public void setUp() {
        this.tearDown();

        Building building1 = new Building("TestBuilding 1",
                "Test neighbourhood", "TestAddress 1",3,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building1);

        Building building2 = new Building("TestBuilding 2",
                "Test neighbourhood", "TestAddress 2",5,
                BigDecimal.valueOf(5), BigDecimal.valueOf(100), LocalDate.now());
        this.buildingRepository.saveAndFlush(building2);

        Role role = this.roleRepository.findByName(UserRole.STAFF).orElse(null);

        if (role == null) {
            role = new Role(UserRole.STAFF);
            this.roleRepository.saveAndFlush(role);
        }

        Staff staff1 = new Staff("Staff 1", "Staff 1", LocalDate.now(),
                "staff1@mail.com", null, "0383933", false, Set.of(role),
                "Job 1", BigDecimal.valueOf(500), new HashSet<>());
        this.staffRepository.saveAndFlush(staff1);
        Staff staff2 = new Staff("Staff 2", "Staff 2", LocalDate.now(),
                "staff2@mail.com", null, "546464", false, Set.of(role),
                "Job 2", BigDecimal.valueOf(500), new HashSet<>());
        this.staffRepository.saveAndFlush(staff2);

        BUILDING_1_ID = building1.getId();
        BUILDING_2_ID = building2.getId();
        BUILDING_1_NAME = building1.getName();
        BUILDING_2_NAME = building2.getName();
        NEIGHBOURHOOD = building1.getNeighbourhood();
        BUILDING_2_ADDRESS = building2.getAddress();
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
        this.mvc.perform(MockMvcRequestBuilders
                .get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(BUILDING_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(BUILDING_2_ID)));
    }

    @Test
    void test_getAll_isNotFound() throws Exception {
        this.buildingRepository.deleteAll();
        this.mvc.perform(MockMvcRequestBuilders
                .get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getAll_withPageable_IsOK() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders
                .get(URI + "/all")
                .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(BUILDING_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(BUILDING_2_ID)));
    }

    @Test
    void test_getAll_withPageable_IsNotFound() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders
                .get(URI + "/all")
                .param("page", "100"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_get_isOK() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders
                .get(URI + "/{buildingId}", BUILDING_1_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test_get_notFound() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders
                .get(URI + "/{buildingId}", "666"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_add_isCreated() throws Exception {

        BuildingBindingModel buildingBindingModel =
                new BuildingBindingModel("Test Name", "Test Neighbourhood",
                        "Test Address", 2,
                        BigDecimal.valueOf(0), BigDecimal.valueOf(5));

        String inputJson = super.mapToJson(buildingBindingModel);

        this.mvc.perform(post(URI + "/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(getAHeaderLocation())));
    }

    @Test
    void test_add_isUnprocessable() throws Exception {
        BuildingBindingModel buildingBindingModel =
                new BuildingBindingModel("", "Test Neighbourhood",
                        "Test Address", 2,
                        BigDecimal.valueOf(0), BigDecimal.valueOf(5));

        String inputJson = super.mapToJson(buildingBindingModel);

        this.mvc.perform(post(URI + "/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void test_add_isUnprocessableWhenAddressOccupied() throws Exception {
        BuildingBindingModel buildingBindingModel =
                new BuildingBindingModel("New Building", NEIGHBOURHOOD,
                        BUILDING_2_ADDRESS, 4,
                        BigDecimal.valueOf(0), BigDecimal.valueOf(5));

        String inputJson = super.mapToJson(buildingBindingModel);

        this.mvc.perform(post(URI + "/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorContainer.errors.address.[0]",
                        is(String.format(ADDRESS_OCCUPIED, BUILDING_2_ADDRESS))));
    }

    @Test
    void test_add_isUnprocessableIfBuildingNameExists() throws Exception {
        BuildingBindingModel buildingBindingModel =
                new BuildingBindingModel(BUILDING_1_NAME, NEIGHBOURHOOD,
                        "Test Address 3", 5,
                        BigDecimal.valueOf(0), BigDecimal.valueOf(5));

        String inputJson = super.mapToJson(buildingBindingModel);

        this.mvc.perform(post(URI + "/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorContainer.errors.nameExists.[0]",
                        is(String.format(BUILDING_NAME_EXISTS, BUILDING_1_NAME, NEIGHBOURHOOD))));
    }

    @Test
    void test_edit_isNoContent() throws Exception {
        BuildingBindingModel buildingBindingModel =
                new BuildingBindingModel("New Name", "New Neighbourhood",
                        "New Test Address", 2,
                        BigDecimal.valueOf(0), BigDecimal.valueOf(5));

        String inputJson = super.mapToJson(buildingBindingModel);

        this.mvc.perform(put(URI + "/{buildingId}", BUILDING_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION,
                        containsString(URI + "/" + BUILDING_1_ID)));

    }

    @Test
    void test_edit_isUnprocessable() throws Exception {
        BuildingBindingModel buildingBindingModel =
                new BuildingBindingModel("", "Test Neighbourhood",
                        "Test Address", 2,
                        BigDecimal.valueOf(0), BigDecimal.valueOf(5));

        String inputJson = super.mapToJson(buildingBindingModel);

        this.mvc.perform(put(URI + "/{buildingId}", BUILDING_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorContainer.errors.name[0]",
                        is(BUILDING_NAME_NOT_EMPTY)));

    }

    @Test
    void test_edit_AddressOccupied() throws Exception {

        BuildingBindingModel buildingBindingModel = new BuildingBindingModel();
        buildingBindingModel.setName("New Name");
        buildingBindingModel.setNeighbourhood(NEIGHBOURHOOD);
        buildingBindingModel.setAddress(BUILDING_2_ADDRESS);
        buildingBindingModel.setFloors(2);
        buildingBindingModel.setBudget(BigDecimal.ZERO);
        buildingBindingModel.setBaseFee(BigDecimal.ZERO);

        String inputJson = super.mapToJson(buildingBindingModel);

        this.mvc.perform(put(URI + "/" + BUILDING_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorContainer.errors.address[0]",
                        is(String.format(ADDRESS_OCCUPIED, BUILDING_2_ADDRESS))));

    }

    @Test
    void test_edit_buildingNameExistsInNeighbourhood() throws Exception {

        BuildingBindingModel buildingBindingModel = new BuildingBindingModel();
        buildingBindingModel.setName(BUILDING_2_NAME);
        buildingBindingModel.setNeighbourhood(NEIGHBOURHOOD);
        buildingBindingModel.setAddress("New Address");
        buildingBindingModel.setFloors(2);
        buildingBindingModel.setBudget(BigDecimal.ZERO);
        buildingBindingModel.setBaseFee(BigDecimal.ZERO);

        String inputJson = super.mapToJson(buildingBindingModel);

        this.mvc.perform(put(URI + "/" + BUILDING_1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorContainer.errors.name[0]",
                        is(String.format(BUILDING_NAME_EXISTS, BUILDING_2_NAME, NEIGHBOURHOOD))));

    }

    @Test
    void test_edit_IdInvalid_isNotFound() throws Exception {
        BuildingBindingModel buildingBindingModel =
                new BuildingBindingModel("Edit Name", "Test Neighbourhood",
                        "Test Address", 2,
                        BigDecimal.valueOf(0), BigDecimal.valueOf(5));

        String inputJson = super.mapToJson(buildingBindingModel);

        this.mvc.perform(put(URI + "/{buildingId}", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    void test_deleteAll_isOkWithCorrectMessage() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.delete(URI + "/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "buildings")));
    }

    @Test
    void test_deleteAll_isNotFound() throws Exception {
        this.buildingRepository.deleteAll();
        this.mvc.perform(MockMvcRequestBuilders.delete(URI + "/delete"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_delete_success() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders
                .delete(URI + "/{buildingId}", BUILDING_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(URI)));
    }

    @Test
    void test_delete_isNotFound() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders
                .delete(URI + "/{buildingId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(ExceptionErrorMessages.BUILDING_NOT_FOUND));
    }

    @Test
    void test_assignStaff() throws Exception {

        this.mvc.perform(put(URI + "/{buildingId}/assign", BUILDING_1_ID)
                .param("staffIds", STAFF_1_ID, STAFF_2_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION,
                containsString(URI + "/" + BUILDING_1_ID)));;
    }

    @Test
    void test_assignStaff_throwsWhenBuildingNotFound() throws Exception {
        this.mvc.perform(put(URI + "/{buildingId}/assign", "0")
                .param("staffIds", STAFF_1_ID, STAFF_2_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(is(BUILDING_NOT_FOUND)));
    }

    private String getAHeaderLocation() {
        return URI + "/" +
                this.buildingRepository.findAll().stream()
                        .filter(a -> !a.getId().equals(BUILDING_1_ID) && !a.getId().equals(BUILDING_2_ID))
                        .findFirst()
                        .map(Building::getId)
                        .orElse("");
    }
}