package com.syn.domo.web.controllers;

import com.syn.domo.model.binding.FeeAddBindingModel;
import com.syn.domo.model.view.FeeViewModel;
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
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/fees")
public class FeeController {

    private static final String ADD_FEES_TITLE = "Generate Monthly Fees";
    private static final String GENERATED_FEES_TITLE = "Monthly Fees Generated!";

    private final FeeService feeService;

    @Autowired
    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping("/generate")
    public ModelAndView generate(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", ADD_FEES_TITLE);
        modelAndView.setViewName("generate-fees");
        return modelAndView;
    }

    @PostMapping("/generate")
    public ModelAndView generatePost(@Valid @ModelAttribute("feeAddBindingModel")
                                                 FeeAddBindingModel feeAddBindingModel,
                                     BindingResult bindingResult, ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/generate");
        } else {
            List<FeeViewModel> feeViewModels =
                    this.feeService.generateMonthlyFees(feeAddBindingModel);
            modelAndView.addObject("notEmpty", !feeViewModels.isEmpty());
            modelAndView.addObject("fees", feeViewModels);
            modelAndView.addObject("pageTitle", GENERATED_FEES_TITLE);
            modelAndView.setViewName("generate-fees");
        }

        return modelAndView;
    }
}
