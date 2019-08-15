package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.meta.ClientPages;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@Controller
public class RegisterOrderController {

    @GetMapping(ClientPages.REGISTRATION_SUCCESS)
    public ModelAndView registerSuccess(ModelMap model, @ModelAttribute("basketCost") BigDecimal basketCost) {
        model.addAttribute("basketCost", basketCost);
        return new ModelAndView("register-success", model);
    }

}
