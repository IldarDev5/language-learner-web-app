package ru.ildar.languagelearner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class MainController
{
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView mainPage()
    {
        return mainPage(0);
    }

    @RequestMapping(value = "/mainPage", method = RequestMethod.GET)
    public ModelAndView mainPage(@RequestParam(value = "page", required = false) Integer page)
    {
        return new ModelAndView("main/mainPage");
    }
}
