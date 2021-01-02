package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.RoleServiceModel;
import com.syn.domo.repository.RoleRepository;
import com.syn.domo.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void initRoles() {
        if (this.roleRepository.count() == 0) {
            for (UserRole value : UserRole.values()) {
                Role role = new Role();
                role.setName(value);
                this.roleRepository.saveAndFlush(role);
            }
        }
    }

    @Override
    public Set<Role> getAll() {
        return this.roleRepository
                .findAll().stream()
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Optional<RoleServiceModel> getByName(UserRole name) {
        Optional<Role> role = this.roleRepository.findByName(name);
        return role.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(role.get(), RoleServiceModel.class));
    }

}
