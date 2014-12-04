package com.flowid.refd.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StatusController {
    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ModelAndView countries() {
        logger.debug("Starting status.");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("countries");
        return mav;
    }
}
