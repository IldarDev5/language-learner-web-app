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

import java.security.Principal;
import java.util.List;

@ControllerAdvice
public class CommonControllerAdvice
{
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private ClusterService clusterService;

    private static final int CLUSTERS_TO_TAKE = 5;

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

    @ModelAttribute("popularClusters")
    public List<PopularCluster> popularClusters(Principal principal)
    {
        if(principal == null)
        {
            return clusterService.getMostPopularClusters(CLUSTERS_TO_TAKE);
        }

        return null;
    }
}
