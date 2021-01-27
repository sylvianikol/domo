package com.syn.domo.web.controller;

import com.syn.domo.model.binding.UserActivateBindingModel;
import com.syn.domo.model.service.UserActivateServiceModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.model.view.UserViewModel;
import com.syn.domo.service.UserService;
import com.syn.domo.web.controller.namespace.AccountNamespace;
import com.syn.domo.web.controller.namespace.BaseNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class AccountController implements AccountNamespace {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public AccountController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activate(@RequestParam(name = "userId") String userId) {

        Optional<UserServiceModel> user = this.userService.get(userId);

        return user.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(user.get(), UserViewModel.class));
    }

    @PostMapping("/activate")
    public ResponseEntity<?> createPassword(@RequestParam(name = "userId") String userId,
                                            @Valid @RequestBody UserActivateBindingModel userActivateBindingModel,
                                            BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(userActivateBindingModel);
        }

        UserActivateServiceModel userActivateServiceModel = this.userService
                .createPassword(userId, this.modelMapper
                        .map(userActivateBindingModel, UserActivateServiceModel.class));

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .location(uriComponentsBuilder.path(BaseNamespace.BASE_URI).build().toUri())
                .build();
    }
}
