package com.syn.domo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(BuildingNotFoundException.class)
    public ModelAndView handleBuildingNotFound(BuildingNotFoundException ex) {
        return buildModelAndView(ex.getMessage(), convertStackTraceToString(ex));
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResidentNotFoundException.class)
    public ModelAndView handleResidentNotFound(ResidentNotFoundException ex) {
        return buildModelAndView(ex.getMessage(), convertStackTraceToString(ex));
    }

    private ModelAndView buildModelAndView(String message, String s) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", message)
                .addObject("trace", s)
                .addObject("status", HttpStatus.NOT_FOUND);
        return modelAndView;
    }

    private String convertStackTraceToString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
