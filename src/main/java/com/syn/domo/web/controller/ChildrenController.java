package com.syn.domo.web.controller;

import com.syn.domo.model.binding.ChildAddBindingModel;
import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.model.view.ChildViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.ChildService;
import com.syn.domo.web.controller.namespace.ChildrenNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ChildrenController implements ChildrenNamespace {

    private static final String MANAGE_CHILDREN_TITLE = "Manage Children";
    private static final String ADD_CHILD_TITLE = "Add Child";
    private static final String EDIT_CHILDREN_TITLE = "Edit Children";
    private static final String CHILD_DETAILS = "Child Details";

    private final ChildService childService;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ChildrenController(ChildService childService, BuildingService buildingService, ApartmentService apartmentService, ModelMapper modelMapper) {
        this.childService = childService;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }

//    @GetMapping("/")
//    public ModelAndView manage(@PathVariable(value = "buildingId") String buildingId,
//                               @PathVariable(value = "apartmentId") String apartmentId,
//                               ModelAndView modelAndView) {
//
//        Set<ChildViewModel> children =
//                new LinkedHashSet<>(Collections.unmodifiableCollection(
//                        this.childService.getAllChildrenByApartmentId(apartmentId).stream()
//                                .map(childServiceModel -> this.modelMapper.map(childServiceModel, ChildViewModel.class))
//                                .collect(Collectors.toCollection(LinkedHashSet::new))));
//
//        boolean hasChildren = children.size() > 0;
//
//        if (hasChildren) {
//            modelAndView.addObject("pageH3Title", EDIT_CHILDREN_TITLE);
//        }
//
//        modelAndView.addObject("hasChildren", children.size() > 0)
//                .addObject("buildingName", this.buildingService.getBuildingName(buildingId))
//                .addObject("apartmentNumber", this.apartmentService.getById(apartmentId).getNumber())
//                .addObject("children", children)
//                .addObject("pageTitle", MANAGE_CHILDREN_TITLE)
//                .addObject("pageH2Title", ADD_CHILD_TITLE)
//                .setViewName("manage-children");
//
//        return modelAndView;
//    }
//
//    @PostMapping("/")
//    public ModelAndView add(@PathVariable(value = "buildingId") String buildingId,
//                            @PathVariable(value = "apartmentId") String apartmentId,
//                            @Valid @ModelAttribute("childAddBindingModel")
//                                        ChildAddBindingModel childAddBindingModel,
//                            BindingResult bindingResult,
//                            RedirectAttributes redirectAttributes,
//                            ModelAndView modelAndView) {
//
//        if (bindingResult.hasErrors()) {
//            // TODO: return errors
//            redirectAttributes.addFlashAttribute("childAddBindingModel", childAddBindingModel);
//        } else {
//            this.childService.add(
//                            this.modelMapper.map(childAddBindingModel, ChildServiceModel.class));
//
//        }
//        modelAndView.setViewName("redirect:/buildings/" + buildingId +
//                "/apartments/" + apartmentId + "/children/");
//
//        return modelAndView;
//    }
//
//    @GetMapping("/{childId}")
//    public ModelAndView details(@PathVariable(value = "buildingId") String buildingId,
//                                @PathVariable(value = "apartmentId") String apartmentId,
//                                @PathVariable(value = "childId") String childId,
//                                ModelAndView modelAndView) {
//
//        ChildViewModel child =
//                this.modelMapper.map(this.childService.getById(childId), ChildViewModel.class);
//
//        modelAndView.addObject("child", child)
//            .addObject("buildingName", this.buildingService.getBuildingName(buildingId))
//            .addObject("pageTitle", CHILD_DETAILS)
//            .setViewName("details-child");
//
//        return modelAndView;
//    }
}
