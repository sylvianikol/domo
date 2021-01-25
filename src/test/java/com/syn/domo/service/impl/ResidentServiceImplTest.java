package com.syn.domo.service.impl;

import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.service.RoleServiceModel;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.notification.service.NotificationService;
import com.syn.domo.repository.ResidentRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.RoleService;
import com.syn.domo.service.UserService;
import com.syn.domo.utils.ValidationUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static com.syn.domo.common.ValidationErrorMessages.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ResidentServiceImplTest {

    ResidentServiceImpl residentService;

    @Mock
    ResidentRepository residentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ValidationUtil validationUtil;
    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        this.residentService = new ResidentServiceImpl(
                residentRepository, userService, buildingService,
                apartmentService, roleService, modelMapper,
                validationUtil, notificationService);
    }

    @Test
    void test_add_serviceModelNotValid() throws MessagingException, InterruptedException {
        ResidentServiceModel residentToAdd = new ResidentServiceModel(null, "", "Doe",
                null, "john@mail.com", "334343", false, null, null, null);

        ResponseModel<ResidentServiceModel> responseModel =
                this.residentService.add(residentToAdd, "1", "1");

        Map<String, Set<String>> errors = responseModel.getErrorContainer().getErrors();

        assertEquals(errors.get("firstName"), Set.of(FIRST_NAME_INVALID, FIRST_NAME_NOT_EMPTY));
    }

    @Test
    void test_edit_serviceModelNotValid() {
        ResidentServiceModel residentToAdd = new ResidentServiceModel(null, "", "Doe",
                null, "john@mail.com", "334343", false, null, null, null);

        ResponseModel<ResidentServiceModel> responseModel =
                this.residentService.edit(residentToAdd, "1");

        Map<String, Set<String>> errors = responseModel.getErrorContainer().getErrors();

        assertEquals(errors.get("firstName"), Set.of(FIRST_NAME_INVALID, FIRST_NAME_NOT_EMPTY));
    }
}