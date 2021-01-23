package com.syn.domo.web.controller;

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
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FeesControllerTest extends AbstractTest {

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
    private FeeRepository feeRepository;

    private String BUILDING_ID;
    private String APARTMENT_ID;
    private String USER_ID;
    private String FEE_1_ID;
    private String FEE_2_ID;
    private final String URI = "/v1/fees";

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

        Resident resident = new Resident("John", "Doe",
                LocalDate.now(), "john@test.mail",
                null, "099383838", false,
                Set.of(role), Set.of(apartment));
        this.residentRepository.saveAndFlush(resident);

        Fee fee1 = new Fee(BigDecimal.valueOf(10), LocalDate.now(), LocalDate.now().plusMonths(1),
                null, false, null, apartment);
        this.feeRepository.saveAndFlush(fee1);

        Fee fee2 = new Fee(BigDecimal.valueOf(20), LocalDate.now(), LocalDate.now().plusMonths(1),
                null, false, null, apartment);
        this.feeRepository.saveAndFlush(fee2);

        BUILDING_ID = building.getId();
        APARTMENT_ID = apartment.getId();
        USER_ID = resident.getId();
        FEE_1_ID = fee1.getId();
        FEE_2_ID = fee2.getId();
    }

    @AfterEach
    public void tearDown() {
        this.feeRepository.deleteAll();
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
        assertThat(this.feeRepository).isNotNull();
    }

    @Test
    void test_getAll_isOK() throws Exception {
        this.mvc.perform(get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(FEE_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(FEE_2_ID)));
    }

    @Test
    void test_getAll_isNotFound() throws Exception {
        this.feeRepository.deleteAll();
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
                .andExpect(jsonPath("$.[0].id", is(FEE_1_ID)))
                .andExpect(jsonPath("$.[1].id", is(FEE_2_ID)));
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
                .andExpect(jsonPath("$.[0].id", is(FEE_1_ID)))
                .andExpect(jsonPath("$.[0].apartment.building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[1].id", is(FEE_2_ID)))
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
                .andExpect(jsonPath("$.[0].id", is(FEE_1_ID)))
                .andExpect(jsonPath("$.[0].apartment.building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[0].apartment.id", is(APARTMENT_ID)))
                .andExpect(jsonPath("$.[1].id", is(FEE_2_ID)))
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
                .andExpect(jsonPath("$.[0].id", is(FEE_1_ID)))
                .andExpect(jsonPath("$.[0].apartment.id", is(APARTMENT_ID)))
                .andExpect(jsonPath("$.[1].id", is(FEE_2_ID)))
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
        this.feeRepository.deleteAll();
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
                .andExpect(jsonPath("$.[0].id", is(FEE_1_ID)))
                .andExpect(jsonPath("$.[0].apartment.building.id", is(BUILDING_ID)))
                .andExpect(jsonPath("$.[1].id", is(FEE_2_ID)))
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
                .andExpect(jsonPath("$.[0].id", is(FEE_1_ID)))
                .andExpect(jsonPath("$.[0].apartment.id", is(APARTMENT_ID)))
                .andExpect(jsonPath("$.[1].id", is(FEE_2_ID)))
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

        this.mvc.perform(get(URI + "/{feeId}", FEE_1_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test_get_notFound() throws Exception {

        this.mvc.perform(get(URI + "/{feeId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    void test_pay_isNoContent() throws Exception {

        this.mvc.perform(post(URI + "/{feeId}/pay", FEE_1_ID)
                .param("userId", USER_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(URI + "/" + FEE_1_ID)));

    }

    @Test
    void test_deleteAll_isOkWithCorrectMessage() throws Exception {
        this.mvc.perform(delete(URI + "/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "fees")));
    }

    @Test
    void test_deleteAll_isNotFound() throws Exception {
        this.feeRepository.deleteAll();
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
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "fees")));
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
        this.feeRepository.deleteAll();
        this.mvc.perform(delete(URI + "/delete")
                .param("buildingId", BUILDING_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_deleteAll_byBuildingAndApartment_isOkWithCorrectMessage() throws Exception {
        this.mvc.perform(delete(URI + "/delete")
                .param("buildingId", BUILDING_ID)
                .param("apartmentId", APARTMENT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "fees")));
    }

    @Test
    void test_deleteAll_byBuildingAndApartmentInvalid_isNotFound() throws Exception {
        this.mvc.perform(delete(URI + "/delete")
                .param("buildingId", "0")
                .param("apartmentId", "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_deleteAll_byApartmentId_isOkWithCorrectMessage() throws Exception {
        this.mvc.perform(delete(URI + "/delete")
                .param("apartmentId", APARTMENT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "fees")));
    }

    @Test
    void test_deleteAll_byApartmentIdInvalid_isNotFound() throws Exception {
        this.mvc.perform(delete(URI + "/delete")
                .param("apartmentId", "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_delete_success() throws Exception {

        this.mvc.perform(delete(URI + "/{feeId}", FEE_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION,
                containsString(URI)));
    }

    @Test
    void test_delete_isNotFound() throws Exception {

        this.mvc.perform(delete(URI + "/{feeId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(FEE_NOT_FOUND));
    }

}