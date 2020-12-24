package com.syn.domo.web.controller;

import com.syn.domo.model.binding.ApartmentAddBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ApartmentAddViewModel;
import com.syn.domo.model.view.ApartmentViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FloorService;
import com.syn.domo.web.controller.namespace.ApartmentsNamespace;
import com.syn.domo.web.controller.namespace.BuildingsNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.syn.domo.web.controller.namespace.ApartmentsNamespace.URI_APARTMENTS;

@RestController
public class ApartmentsController implements ApartmentsNamespace {
    private static final String MANAGE_APARTMENTS_TITLE = "Manage Apartments";
    private static final String ADD_APARTMENTS_TITLE = "Add Apartment";
    private static final String ALL_APARTMENTS = "All Apartments in ";
    private static final String APARTMENT_DETAILS = "Apartment Details";

    private final ApartmentService apartmentService;
    private final BuildingService buildingService;
    private final FloorService floorService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentsController(ApartmentService apartmentService, BuildingService buildingService, FloorService floorService, ModelMapper modelMapper) {
        this.apartmentService = apartmentService;
        this.buildingService = buildingService;
        this.floorService = floorService;
        this.modelMapper = modelMapper;
    }
    // how to -> /buildings/buildingId/apartments/apartmentId
    // https://www.youtube.com/watch?t=6921&v=un6U-_65zhE&feature=youtu.be

    @GetMapping
    public ResponseEntity<Set<ApartmentViewModel>> all(@PathVariable(value = "buildingId") String buildingId) {
        Set<ApartmentServiceModel> apartmentServiceModels =
                this.apartmentService.getAllApartmentsByBuildingId(buildingId);
        Set<ApartmentViewModel> apartments = apartmentServiceModels.stream()
                .map(apartmentServiceModel -> this.modelMapper.map(apartmentServiceModel, ApartmentViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return apartments.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(apartments);
    }

    @GetMapping("/{apartmentId}")
    public ResponseEntity<ApartmentViewModel> one(@PathVariable(value = "buildingId") String buildingId,
                                                  @PathVariable(value = "apartmentId") String apartmentId) {
        Optional<ApartmentServiceModel> apartment =
                this.apartmentService.getOptById(apartmentId);

        return apartment.filter(a -> a.getBuilding().getId().equals(buildingId))
                .map(a -> ResponseEntity.ok()
                        .body(this.modelMapper.map(a, ApartmentViewModel.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<ApartmentAddViewModel> add(@PathVariable(value = "buildingId") String buildingId,
                                                     @Valid @RequestBody ApartmentAddBindingModel apartmentAddBindingModel,
                                                     BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(this.modelMapper.map(bindingResult.getTarget(),
                            ApartmentAddViewModel.class));
        }

        if (this.buildingService.getOptById(buildingId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (this.apartmentService.alreadyExists(apartmentAddBindingModel.getNumber(), buildingId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(this.modelMapper.map(bindingResult.getTarget(),
                            ApartmentAddViewModel.class));
        }

        String apartmentId =
                this.apartmentService.add(this.modelMapper.map(apartmentAddBindingModel,
                        ApartmentServiceModel.class), buildingId).getId();

        return ResponseEntity.created(uriComponentsBuilder
                .path(URI_APARTMENTS + "/{apartmentId}")
                .buildAndExpand(buildingId, apartmentId)
                .toUri()).build();


    }

//    @GetMapping("/")
//    public ModelAndView manage(@PathVariable(value = "buildingId") String buildingId,
//                               ModelAndView modelAndView) {
//        String buildingName = this.buildingService.getBuildingName(buildingId);
//        Set<ApartmentServiceModel> apartments =
//                this.apartmentService.getAllApartmentsByBuildingId(buildingId);
//
//        if (apartments.size() > 0) {
//            modelAndView.addObject("hasApartments", true);
//            modelAndView.addObject("pageH3Title", ALL_APARTMENTS + buildingName);
//            modelAndView.addObject("apartments", apartments);
//        }
//
//        modelAndView.addObject("pageH2Title", ADD_APARTMENTS_TITLE);
//        modelAndView.addObject("floorNumbers",
//                this.floorService.getAllFloorNumbersByBuildingId(buildingId));
//
//        modelAndView.addObject("buildingId", buildingId);
//        modelAndView.addObject("pageTitle",
//                buildingName + ": " + MANAGE_APARTMENTS_TITLE);
//        modelAndView.setViewName("manage-apartments");
//
//        return modelAndView;
//    }
//
//    @PostMapping("/")
//    public ModelAndView add(@PathVariable(value = "buildingId") String buildingId,
//                            @Valid @ModelAttribute("apartmentAddBindingModel")
//                                            ApartmentAddBindingModel apartmentAddBindingModel,
//                            BindingResult bindingResult, ModelAndView modelAndView,
//                            RedirectAttributes redirectAttributes) {
//
//        if (bindingResult.hasErrors()) {
//            modelAndView.setViewName("redirect:/buildings/" + buildingId + "/apartments/");
//        } else {
//            String apartmentNumber = apartmentAddBindingModel.getNumber();
//            Integer floorNumber = apartmentAddBindingModel.getFloorNumber();
//
//            if (this.apartmentService.alreadyExists(apartmentNumber, buildingId)) {
//                redirectAttributes.addFlashAttribute("alreadyExists",true);
//            } else {
//                this.apartmentService.add(
//                        this.modelMapper.map(apartmentAddBindingModel, ApartmentServiceModel.class),
//                        buildingId);
//
//                redirectAttributes.addFlashAttribute("success",true);
//            }
//
//            redirectAttributes.addFlashAttribute("floorNumber", floorNumber);
//            redirectAttributes.addFlashAttribute("apartmentNumber", apartmentNumber);
//            modelAndView.setViewName("redirect:/buildings/" + buildingId + "/apartments/");
//        }
//
//        return modelAndView;
//    }
//
//    @GetMapping("/{apartmentId}")
//    public ModelAndView details(@PathVariable(value = "buildingId") String buildingId,
//                                @PathVariable(value = "apartmentId") String apartmentId,
//                                ModelAndView modelAndView) {
//
//        ApartmentViewModel apartment =
//                this.modelMapper.map(this.apartmentService.getById(apartmentId), ApartmentViewModel.class);
//
//        if (this.apartmentService.hasResidents(apartmentId)) {
//            modelAndView.addObject("hasResidents", true);
//        }
//
//        modelAndView.addObject("buildingName", this.buildingService.getBuildingName(buildingId));
//        modelAndView.addObject("apartment", apartment);
//        modelAndView.addObject("pageTitle", APARTMENT_DETAILS);
//
//
//        modelAndView.setViewName("details-apartment");
//        return modelAndView;
//    }
}
