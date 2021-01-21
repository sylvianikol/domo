package com.syn.domo.web.controller;

import com.syn.domo.common.ValidationErrorMessages;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.model.binding.ChildBindingModel;
import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.model.view.ChildViewModel;
import com.syn.domo.service.ChildService;
import com.syn.domo.web.controller.namespace.ChildrenNamespace;
import com.syn.domo.web.filter.ChildFilter;
import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;

import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;
import static com.syn.domo.common.ValidationErrorMessages.*;

@RestController
public class ChildrenController implements ChildrenNamespace {

    private final ChildService childService;
    private final ModelMapper modelMapper;

    @Autowired
    public ChildrenController(ChildService childService, ModelMapper modelMapper) {
        this.childService = childService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<Set<ChildViewModel>> getAll(@RequestParam(required = false,
                                                              name = "buildingId") String buildingId,
                                                      @RequestParam(required = false,
                                                              name = "apartmentId") String apartmentId,
                                                      @RequestParam(required = false,
                                                              name = "parentId") String parentId,
                                                      Pageable pageable
                                                      ) {
        Set<ChildViewModel> children = this.childService
                .getAll(new ChildFilter(buildingId, apartmentId, parentId), pageable)
                .stream()
                .map(c -> this.modelMapper.map(c, ChildViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

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

    @PostMapping("/add")
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
    public ResponseEntity<?> edit(@PathVariable(value = "childId") String childId,
                                  @Valid @RequestBody ChildBindingModel childBindingModel,
                                  BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(childBindingModel, bindingResult));
        }

        ResponseModel<ChildServiceModel> responseModel = this.childService.edit(
                this.modelMapper.map(childBindingModel, ChildServiceModel.class), childId);

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .location(uriComponentsBuilder.path(URI_CHILDREN + "/{childId}")
                        .buildAndExpand(childId)
                        .toUri()).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAll(@RequestParam(required = false,
                                              name = "buildingId") String buildingId,
                                       @RequestParam(required = false,
                                               name = "apartmentId") String apartmentId,
                                       @RequestParam(required = false,
                                               name = "parentId") String parentId,
                                       UriComponentsBuilder uriComponentsBuilder) {

        int result = this.childService
                .deleteAll(new ChildFilter(buildingId, apartmentId, parentId));

        return result == 0
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(DELETE_FAILED)
                : ResponseEntity.ok().body(String.format(DELETE_SUCCESSFUL, result, "children"));
    }

    @DeleteMapping("/{childId}")
    public ResponseEntity<?> delete(@PathVariable(value = "childId") String childId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.childService.delete(childId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_CHILDREN).build().toUri())
                .build();
    }
}
