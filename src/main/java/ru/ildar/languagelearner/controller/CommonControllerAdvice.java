package ru.ildar.languagelearner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.ildar.languagelearner.service.AppUserService;

import java.security.Principal;

@ControllerAdvice
public class CommonControllerAdvice
{
    @Autowired
    private AppUserService appUserService;

    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ModelAttribute("userFirstName")
    public String userFirstName(Principal principal)
    {
        if(principal != null)
        {
            return appUserService.getUserByNickname(principal.getName()).getFirstName();
        }

        return null;
    }

    @ModelAttribute("registeredPeopleCount")
    public Long registeredPeopleCount(Principal principal)
    {
        if(principal == null)
        {
            return appUserService.getTotalUsersCount();
        }

        return null;
    }
}
