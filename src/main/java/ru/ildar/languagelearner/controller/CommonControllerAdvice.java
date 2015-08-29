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

import javax.servlet.ServletContext;
import java.security.Principal;
import java.util.Date;
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

    @Autowired
    private ServletContext servletContext;

    /** How many minutes to wait before updating application data for non-authenticated users. */
    private static final double MINUTES_TO_WAIT = 60.0;

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

    /** Data for non-authenticated users. This data is not so important to be constantly precise,
     * so we're caching it - the users are shown the same data for amount of time, specified by the
     * MINUTES_TO_WAIT constant, and after this time is up the update happens, so users can see new
     * data. */
    @ModelAttribute("notAuthUserData")
    @SuppressWarnings("unchecked")
    public Map<String, Object> dataForNotAuthUsers(Principal principal)
    {
        if(principal != null)
            return null;

        Date prevUpdateDate = (Date)servletContext.getAttribute("prevUpdateDate");
        Map<String, Object> map;
        if(prevUpdateDate == null || getDiffMinutes(new Date(), prevUpdateDate) >= MINUTES_TO_WAIT)
            //The data is needed to be updated
        {
            map = userDataMaps.getNonAuthUserData();
            servletContext.setAttribute("nonAuthUserData", map);
            servletContext.setAttribute("prevUpdateDate", new Date());
        }
        else
            //Returning the old data as the time is not up yet
        {
            map = (Map<String, Object>)servletContext.getAttribute("nonAuthUserData");
        }

        return map;
    }

    /** Returns difference in minutes between the first and the second dates. */
    private double getDiffMinutes(Date date, Date prevUpdateDate)
    {
        long msDiff = date.getTime() - prevUpdateDate.getTime();
        return (double)msDiff / 1000 / 60;
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
