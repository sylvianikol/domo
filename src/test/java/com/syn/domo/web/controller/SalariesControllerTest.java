package com.syn.domo.web.controller;

import com.syn.domo.common.ExceptionErrorMessages;
import com.syn.domo.model.entity.*;
import com.syn.domo.repository.BuildingRepository;
import com.syn.domo.repository.RoleRepository;
import com.syn.domo.repository.SalaryRepository;
import com.syn.domo.repository.StaffRepository;
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
import java.util.HashSet;
import java.util.Set;

import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SalariesControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private SalaryRepository salaryRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StaffRepository staffRepository;

    private Salary paidSalary;
    private String BUILDING_ID;
    private String STAFF_ID;
    private String SALARY_ID;
    private boolean PAID;
    private final String URI = "/v1/salaries";

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

        Staff staff = new Staff("Staff 1", "Staff 1", LocalDate.now(),
                "staff@mail.com", null, "0383933", false, Set.of(role),
                "Job 1", BigDecimal.valueOf(50), Set.of(building), new HashSet<>());
        this.staffRepository.saveAndFlush(staff);

        Salary salary = new Salary(BigDecimal.valueOf(100), LocalDate.now(), LocalDate.now().plusMonths(1),
                null, false, BigDecimal.valueOf(100), staff, Set.of(building));
        this.salaryRepository.saveAndFlush(salary);

        this.paidSalary = new Salary(BigDecimal.valueOf(100), LocalDate.now(), LocalDate.now().plusMonths(1),
                null, true, BigDecimal.valueOf(100), staff, Set.of(building));
        this.salaryRepository.saveAndFlush(paidSalary);

        BUILDING_ID = building.getId();
        STAFF_ID = staff.getId();
        SALARY_ID = salary.getId();
    }

    @AfterEach
    void tearDown() {
        this.salaryRepository.deleteAll();
        this.staffRepository.deleteAll();
        this.buildingRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.buildingRepository).isNotNull();
        assertThat(this.salaryRepository).isNotNull();
        assertThat(this.roleRepository).isNotNull();
        assertThat(this.staffRepository).isNotNull();
    }

    @Test
    void test_getAll_isOK() throws Exception {
        this.mvc.perform(get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(SALARY_ID)));
    }

    @Test
    void test_getAll_isNotFound() throws Exception {
        this.salaryRepository.deleteAll();
        this.mvc.perform(get(URI + "/all"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_get_isOk() throws Exception {
        this.mvc.perform(get(URI + "/{salaryId}" , SALARY_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(SALARY_ID)));
    }

    @Test
    void test_get_isNotFound() throws Exception {
        this.mvc.perform(get(URI + "/{salaryId}" , "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_pay_isNoContent_whenSuccess() throws Exception {
        this.mvc.perform(post(URI + "/{salaryId}/pay" , SALARY_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString( URI + "/" + SALARY_ID)));
    }

    @Test
    void test_pay_salaryIdNotFound() throws Exception {
        this.mvc.perform(post(URI + "/{salaryId}/pay" , "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(SALARY_NOT_FOUND)));
    }

    @Test
    void test_pay_isUnprocessable_ifSalaryIsPaid() throws Exception {

        this.mvc.perform(post(URI + "/{salaryId}/pay", this.paidSalary.getId()))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.statusCode", is(422)))
                .andExpect(jsonPath("$.message", is(SALARY_ALREADY_PAID)));
    }

    @Test
    void test_deleteAll_isOkWithCorrectMessage() throws Exception {
        this.mvc.perform(delete(URI + "/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(DELETE_SUCCESSFUL, 2, "salaries")));
    }

    @Test
    void test_deleteAll_isNotFound() throws Exception {
        this.salaryRepository.deleteAll();
        this.mvc.perform(delete(URI + "/delete"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(DELETE_FAILED));
    }

    @Test
    void test_delete_success() throws Exception {
        this.mvc.perform(delete(URI + "/{salaryId}", SALARY_ID))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(URI)));
    }

    @Test
    void test_delete_isNotFound() throws Exception {

        this.mvc.perform(delete(URI + "/{salaryId}", "0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(SALARY_NOT_FOUND)));
    }
}