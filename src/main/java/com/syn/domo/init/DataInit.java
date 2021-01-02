package com.syn.domo.init;

import com.syn.domo.service.RoleService;
import com.syn.domo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public DataInit(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.roleService.initRoles();
        this.userService.initAdmin();
    }
}
