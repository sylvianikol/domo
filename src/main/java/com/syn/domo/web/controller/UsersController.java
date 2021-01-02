package com.syn.domo.web.controller;

import com.syn.domo.model.ErrorResponse;
import com.syn.domo.model.binding.UserAddBindingModel;
import com.syn.domo.model.binding.UserEditBindingModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.model.view.UserViewModel;
import com.syn.domo.service.UserService;
import com.syn.domo.web.controller.namespace.UsersNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UsersController implements UsersNamespace {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersController(UserService userService,
                           ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Set<UserViewModel>> all(@PathVariable(value = "buildingId") String buildingId,
                                                  @PathVariable(value = "apartmentId") String apartmentId) {
        Set<UserViewModel> users = this.userService
                        .getAllByApartmentIdAndBuildingId(buildingId, apartmentId)
                .stream()
                .map(r -> this.modelMapper.map(r, UserViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return users.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserViewModel> one(@PathVariable(value = "buildingId") String buildingId,
                                                 @PathVariable(value = "apartmentId") String apartmentId,
                                                 @PathVariable(value = "userId") String userId) {

        return this.userService.getById(userId)
                .filter(r -> r.getApartment().getId().equals(apartmentId)
                        && r.getApartment().getBuilding().getId().equals(buildingId))
                .map(u -> ResponseEntity.ok()
                        .body(this.modelMapper.map(u, UserViewModel.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> add(@PathVariable(value = "buildingId") String buildingId,
                                 @PathVariable(value = "apartmentId") String apartmentId,
                                 @Valid @RequestBody UserAddBindingModel userAddBindingModel,
                                 BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        String userId = this.userService.add(
                this.modelMapper.map(userAddBindingModel, UserServiceModel.class),
                buildingId, apartmentId).getId();

        return ResponseEntity.created(uriComponentsBuilder
                .path(URI_USERS + "/{userId}")
                .buildAndExpand(buildingId, apartmentId, userId)
                .toUri()).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> edit(@PathVariable(value = "buildingId") String buildingId,
                                  @PathVariable(value = "apartmentId") String apartmentId,
                                  @PathVariable(value = "userId") String userId,
                                  @Valid @RequestBody UserEditBindingModel userEditBindingModel,
                                  BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        this.userService.edit(this.modelMapper.map(userEditBindingModel, UserServiceModel.class),
                buildingId, apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_USERS + "/{userId}")
                        .buildAndExpand(buildingId, apartmentId, userId)
                        .toUri()).build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable(value = "buildingId") String buildingId,
                                    @PathVariable(value = "apartmentId") String apartmentId,
                                    @PathVariable(value = "userId") String userId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.userService.delete(userId, buildingId, apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_USERS)
                        .buildAndExpand(buildingId, apartmentId)
                        .toUri())
                .build();
    }
}
