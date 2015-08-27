package ru.ildar.languagelearner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.ildar.languagelearner.controller.dto.PopularCluster;
import ru.ildar.languagelearner.service.AppUserService;
import ru.ildar.languagelearner.service.ClusterService;
import ru.ildar.languagelearner.service.LessonService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CommonControllerAdvice
{
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private UserDataMaps userDataMaps;

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

    @ModelAttribute("notAuthUserData")
    public Map<String, Object> dataForNotAuthUsers(Principal principal)
    {
        if(principal == null)
        {
            return userDataMaps.getNonAuthUserData();
        }

        return null;
    }

    @ModelAttribute("authUserData")
    public Map<String, Object> dataForAuthUsers(Principal principal)
    {
        if(principal != null)
        {
            return userDataMaps.getAuthUserData(principal);
        }

        return null;
    }
}
