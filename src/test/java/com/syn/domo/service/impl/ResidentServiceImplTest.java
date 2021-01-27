package com.syn.domo.service.impl;

import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.notification.service.NotificationService;
import com.syn.domo.repository.ResidentRepository;
import com.syn.domo.service.*;
import com.syn.domo.utils.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.Set;

import static com.syn.domo.common.ValidationErrorMessages.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResidentServiceImplTest {

    @Autowired
    private ResidentService residentService;

    @Test
    void test_add_withInvalidData() {
        ResidentServiceModel residentToAdd = new ResidentServiceModel(null, "", "Doe",
                null, "john@mail.com", "334343", false, null, null, null);

        assertThrows(UnprocessableEntityException.class, () ->
                this.residentService.add(residentToAdd, "1", "1"));
    }

    @Test
    void test_edit_serviceModelNotValid() {
        ResidentServiceModel residentToAdd = new ResidentServiceModel(null, "", "Doe",
                null, "john@mail.com", "334343", false, null, null, null);

        assertThrows(UnprocessableEntityException.class, () ->
                this.residentService.edit(residentToAdd, "1"));
    }
}