package com.syn.domo.web.controller;

import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.model.binding.ChildBindingModel;
import com.syn.domo.model.binding.ChildEditBindingModel;
import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.model.view.ChildViewModel;
import com.syn.domo.service.ChildService;
import com.syn.domo.web.controller.namespace.ChildrenNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.syn.domo.common.DefaultParamValues.EMPTY_URL;

@RestController
public class ChildrenController implements ChildrenNamespace {

    private final ChildService childService;
    private final ModelMapper modelMapper;

    @Autowired
    public ChildrenController(ChildService childService, ModelMapper modelMapper) {
        this.childService = childService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Set<ChildViewModel>> getAll(@RequestParam(required = false, defaultValue = EMPTY_URL,
                                                                    name = "buildingId") String buildingId,
                                                      @RequestParam(required = false, defaultValue = EMPTY_URL,
                                                                    name = "apartmentId") String apartmentId,
                                                      @RequestParam(required = false, defaultValue = EMPTY_URL,
                                                                    name = "parentId") String parentId) {
        Set<ChildViewModel> children = this.childService
                .getAll(buildingId, apartmentId, parentId)
                .stream()
                .map(c -> this.modelMapper.map(c, ChildViewModel.class))
                .collect(Collectors.toUnmodifiableSet());

        return children.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(children);
    }

    @GetMapping("/{childId}")
    public ResponseEntity<ChildViewModel> get(@PathVariable(value = "childId") String childId) {

        Optional<ChildServiceModel> child = this.childService.get(childId);

        return child.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(child.get(), ChildViewModel.class));
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestParam(name = "buildingId") String buildingId,
                                 @RequestParam(name = "apartmentId") String apartmentId,
                                 @RequestParam(name = "parentIds") Set<String> parentIds,
                                 @Valid @RequestBody ChildBindingModel childBindingModel,
                                 BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(childBindingModel, bindingResult));
        }

        ResponseModel<ChildServiceModel> responseModel = this.childService.add(
                this.modelMapper.map(childBindingModel, ChildServiceModel.class),
                buildingId, apartmentId, parentIds);

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.created(uriComponentsBuilder.path(URI_CHILDREN + "/{childId}")
                .buildAndExpand(responseModel.getId())
                .toUri()).build();
    }

    @PutMapping("/{childId}")
    public ResponseEntity<?> edit(@PathVariable(value = "buildingId") String buildingId,
                                  @PathVariable(value = "apartmentId") String apartmentId,
                                  @PathVariable(value = "childId") String childId,
                                  @Valid @RequestBody ChildEditBindingModel childEditBindingModel,
                                  BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(childEditBindingModel, bindingResult));
        }

        ResponseModel<ChildServiceModel> responseModel = this.childService.edit(
                this.modelMapper.map(childEditBindingModel, ChildServiceModel.class),
                buildingId, apartmentId, childId);

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.created(uriComponentsBuilder.path(URI_CHILDREN + "/{child_id}")
                .buildAndExpand(buildingId, apartmentId, childId)
                .toUri()).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@PathVariable(value = "buildingId") String buildingId,
                                       @PathVariable(value = "apartmentId") String apartmentId,
                                       UriComponentsBuilder uriComponentsBuilder) {

        this.childService.deleteAll(buildingId, apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_CHILDREN)
                        .buildAndExpand(buildingId, apartmentId)
                        .toUri()).build();
    }

    @DeleteMapping("/{childId}")
    public ResponseEntity<?> delete(@PathVariable(value = "buildingId") String buildingId,
                                    @PathVariable(value = "apartmentId") String apartmentId,
                                    @PathVariable(value = "childId") String childId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.childService.delete(childId, buildingId, apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_CHILDREN)
                        .buildAndExpand(buildingId, apartmentId)
                        .toUri()).build();
    }
}
