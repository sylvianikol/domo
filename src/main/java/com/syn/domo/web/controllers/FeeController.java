package com.syn.domo.web.controllers;

import com.syn.domo.model.binding.FeeAddBindingModel;
import com.syn.domo.model.view.FeeViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/fees")
public class FeeController {

    private static final String ADD_FEE_TITLE = "Generate Monthly Fees";

    private final FeeService feeService;

    @Autowired
    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping("/generate")
    public ModelAndView generate(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", ADD_FEE_TITLE);
        modelAndView.setViewName("generate-fees");
        return modelAndView;
    }

    @PostMapping("/generate")
    public ModelAndView generatePost(@Valid @ModelAttribute("feeAddBindingModel")
                                                 FeeAddBindingModel feeAddBindingModel,
                                     BindingResult bindingResult, ModelAndView modelAndView) {

        List<FeeViewModel> feeViewModels =
                    this.feeService.generateMonthlyFees();

        return modelAndView;
    }
}
