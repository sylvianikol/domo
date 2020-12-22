package com.syn.domo.web.controller;

import com.syn.domo.exception.BuildingArchivedExistsException;
import com.syn.domo.exception.BuildingExistsException;
import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.binding.BuildingEditBindingModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.view.BuildingViewModel;
import com.syn.domo.service.BuildingService;
import com.syn.domo.web.controller.namespace.BuildingsNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Controller
public class BuildingsController implements BuildingsNamespace {
    private static final String MANAGE_BUILDINGS_TITLE = "Manage Buildings";
    private static final String BUILDING_DETAILS = "Building Details";

    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public BuildingsController(BuildingService buildingService, ModelMapper modelMapper) {
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView manage(ModelAndView modelAndView) {

        modelAndView.addObject("buildings",
                this.buildingService.getAllBuildings().stream()
                        .map(buildingServiceModel -> this.modelMapper
                                .map(buildingServiceModel, BuildingViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new)))
                .addObject("pageTitle", MANAGE_BUILDINGS_TITLE)
                .setViewName("manage-buildings");

        return modelAndView;
    }

    @PostMapping("/")
    public ModelAndView add(@Valid @ModelAttribute("buildingAddBindingModel")
                                            BuildingAddBindingModel buildingAddBindingModel,
                            BindingResult bindingResult, ModelAndView modelAndView,
                            RedirectAttributes redirectAttributes) {

        modelAndView.setViewName("redirect:/buildings/");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong");
            return modelAndView;
        }

        String buildingName = buildingAddBindingModel.getName().trim();
        String  buildingAddress = buildingAddBindingModel.getAddress().trim();

        try {
            redirectAttributes.addFlashAttribute("addedBuilding", this.modelMapper
                            .map(this.buildingService.add(
                                    this.modelMapper.map(buildingAddBindingModel, BuildingServiceModel.class)),
                                    BuildingViewModel.class));

        } catch (BuildingExistsException | BuildingArchivedExistsException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage())
                    .addFlashAttribute("foundBuilding", this.modelMapper
                            .map(this.buildingService.getByNameAndAddress(buildingName, buildingAddress),
                                    BuildingViewModel.class));
        }

        return modelAndView;
    }

    @GetMapping("/{buildingId}")
    public ModelAndView manage(@PathVariable(value = "buildingId") String buildingId, ModelAndView modelAndView) {
        BuildingViewModel building = this.modelMapper.map(
                this.buildingService.getById(buildingId), BuildingViewModel.class);

        if (building.getApartments().size() > 0) {
            modelAndView.addObject("hasApartments", true);
        }

        modelAndView.addObject("building", building)
                .addObject("pageTitle", BUILDING_DETAILS)
                .setViewName("details-building.html");
        return modelAndView;
    }

    @PostMapping("/{buildingId}/edit")
    public ModelAndView edit(@PathVariable(value = "buildingId") String buildingId,
                             @Valid @ModelAttribute("buildingEditBindingModel")
                                     BuildingEditBindingModel buildingEditBindingModel,
                             BindingResult bindingResult, ModelAndView modelAndView,
                             RedirectAttributes redirectAttributes) {

        modelAndView.setViewName("redirect:/buildings/{buildingId}");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong");
            return modelAndView;
        }

        String buildingName = buildingEditBindingModel.getName().trim();
        String  buildingAddress = buildingEditBindingModel.getAddress().trim();

        try {
            redirectAttributes.addFlashAttribute("editedBuilding", this.modelMapper
                    .map(this.buildingService.add(
                            this.modelMapper.map(buildingEditBindingModel, BuildingServiceModel.class)),
                            BuildingViewModel.class));

        } catch (BuildingExistsException | BuildingArchivedExistsException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage())
                    .addFlashAttribute("foundBuilding", this.modelMapper
                            .map(this.buildingService.getByNameAndAddress(buildingName, buildingAddress),
                                    BuildingViewModel.class));
        }

        return modelAndView;
    }

    @PostMapping("/{buildingId}/archive")
    public ModelAndView archive(@PathVariable(value = "buildingId") String buildingId,
                                ModelAndView modelAndView) {
        BuildingViewModel archivedBuilding =
                this.modelMapper.map(this.buildingService.archive(buildingId), BuildingViewModel.class);

        modelAndView.setViewName("redirect:/buildings/{buildingId}");
        return modelAndView;
    }

    @PostMapping("/{buildingId}/activate")
    public ModelAndView activate(@PathVariable(value = "buildingId") String buildingId,
                                 ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        BuildingServiceModel buildingServiceModel = this.buildingService.activate(buildingId);

        redirectAttributes.addFlashAttribute("activatedBuilding", this.modelMapper
                        .map(buildingServiceModel, BuildingViewModel.class));
        modelAndView.setViewName("redirect:/buildings/{buildingId}");
        return modelAndView;
    }

    @PostMapping("/{buildingId}/delete")
    public ModelAndView delete(@PathVariable(value = "buildingId") String buildingId,
                                 ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        BuildingServiceModel buildingServiceModel = this.buildingService.delete(buildingId);
        BuildingViewModel deletedBuilding = this.modelMapper
                .map(buildingServiceModel, BuildingViewModel.class);
        redirectAttributes.addFlashAttribute("deletedBuilding", deletedBuilding);
        modelAndView.setViewName("redirect:/buildings/");
        return modelAndView;
    }
}
