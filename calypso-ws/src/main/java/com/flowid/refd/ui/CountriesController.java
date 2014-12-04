package com.flowid.refd.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CountriesController {
    private static final Logger logger = LoggerFactory.getLogger(CountriesController.class);

    @RequestMapping(value = "/*", method = RequestMethod.GET)
    public String init() {
        logger.debug("Starting root.");
        return "redirect:/ui/countries";
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public ModelAndView countries() {
        logger.debug("Starting countries.");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("countries");
        return mav;
    }
}
