package com.syn.domo.service.impl;

import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.service.ChildService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Set;

import static com.syn.domo.common.ValidationErrorMessages.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChildServiceImplTest {

    @Autowired
    private ChildService childService;

    @Test
    void test_add_withInvalidData() {
        ChildServiceModel childToAdd = new ChildServiceModel();
        childToAdd.setFirstName("");
        childToAdd.setLastName("Doe");

        ResponseModel<ChildServiceModel> responseModel = this.childService
                .add(childToAdd, "1", "1", Set.of("1", "2"));

        Map<String, Set<String>> errors = responseModel.getErrorContainer().getErrors();

        assertEquals(errors.get("firstName"), Set.of(FIRST_NAME_INVALID, FIRST_NAME_NOT_EMPTY));
    }

    @Test
    void test_edit_withInvalidData() {
        ChildServiceModel childToAdd = new ChildServiceModel();
        childToAdd.setFirstName("");
        childToAdd.setLastName("Doe");

        ResponseModel<ChildServiceModel> responseModel = this.childService.edit(childToAdd, "1");

        Map<String, Set<String>> errors = responseModel.getErrorContainer().getErrors();

        assertEquals(errors.get("firstName"), Set.of(FIRST_NAME_INVALID, FIRST_NAME_NOT_EMPTY));
    }
}