package com.syn.domo.service.impl;

import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.service.ChildService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

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

        assertThrows(UnprocessableEntityException.class, () ->
                this.childService.add(childToAdd, "1", "1", Set.of("1", "2")));

    }

    @Test
    void test_edit_withInvalidData() {
        ChildServiceModel childToAdd = new ChildServiceModel();
        childToAdd.setFirstName("");
        childToAdd.setLastName("Doe");

        assertThrows(UnprocessableEntityException.class, () -> this.childService.edit(childToAdd, "1"));
    }
}