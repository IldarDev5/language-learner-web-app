package ru.ildar.languagelearner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.ildar.languagelearner.controller.dto.ClusterDTO;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.exception.ClusterAlreadyExistsException;
import ru.ildar.languagelearner.exception.ClusterNotOfThisUserException;
import ru.ildar.languagelearner.exception.LanguageNotFoundException;
import ru.ildar.languagelearner.exception.LanguagesAreEqualException;
import ru.ildar.languagelearner.service.ClusterService;
import ru.ildar.languagelearner.service.LanguageService;
import ru.ildar.languagelearner.service.LessonService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/cluster")
public class ClusterController
{
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private LessonService lessonService;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView createClusterPage(ClusterDTO clusterDTO, ModelMap model, Principal principal)
    {
        /* Adding a pair of two languages to view in <select>s
          the cluster for which doesn't exist. This helps to
          get rid of a bug when a pair of languages is visualized
          for which there is already existing cluster. */
        model.addAttribute("langPair", clusterService.getNonExistentLanguagePair(principal.getName()));
        model.addAttribute("languages", languageService.getLanguagesAsStrings());
        return new ModelAndView("cluster/create", "cluster", clusterDTO);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ModelAndView createCluster(@ModelAttribute("cluster") @Valid ClusterDTO clusterDTO,
                                      BindingResult bindingResult, ModelMap model,
                                      Principal principal)
    {
        if(bindingResult.hasFieldErrors())
        {
            return createClusterPage(clusterDTO, model, principal);
        }

        long clusterId;
        try
        {
            clusterId = clusterService.addCluster(clusterDTO, principal.getName());
        }
        catch (LanguagesAreEqualException e)
        {
            bindingResult.rejectValue("language1", null, "Languages must not be equal.");
            return createClusterPage(clusterDTO, model, principal);
        }
        catch (LanguageNotFoundException e)
        {
            bindingResult.rejectValue("language" + e.getLanguageNumber(), null,
                    "The language is not found.");
            return createClusterPage(clusterDTO, model, principal);
        }
        catch (ClusterAlreadyExistsException e)
        {
            bindingResult.reject(null, "Cluster with such language pair already exists.");
            return createClusterPage(clusterDTO, model, principal);
        }

        return new ModelAndView("redirect:viewCluster/" + clusterId + "?created=true");
    }

    @RequestMapping(value = "viewClusters", method = RequestMethod.GET)
    public ModelAndView viewClusters(Principal principal)
    {
        String nickname = principal.getName();
        List<Cluster> clusters = clusterService.getClustersOfUser(nickname);
        return new ModelAndView("cluster/viewClusters", "clusters", clusters);
    }

    @RequestMapping(value = "viewCluster/{id}", method = RequestMethod.GET)
    public ModelAndView viewCluster(@PathVariable("id") Long id,
                                    @RequestParam(value = "created", required = false) Boolean created,
                                    ModelMap model,
                                    Principal principal)
    {
        Cluster cluster = clusterService.getClusterById(id);
        if(cluster == null || !cluster.getAppUser().getNickname().equals(principal.getName()))
            //User tries to access non-existent or not his cluster
        {
            throw new ClusterNotOfThisUserException();
        }

        if(created != null && created)
        {
            model.addAttribute("created", true);
        }
        model.addAttribute("lessonsCount", lessonService.getLessons(cluster).size());
        return new ModelAndView("cluster/viewCluster", "cls", cluster);
    }

    @RequestMapping(value = "checkClusterExistence", method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> checkClusterExistence(@RequestParam("lang1") String lang1,
                                                     @RequestParam("lang2") String lang2,
                                                     Principal principal)
    {
        boolean exists = clusterService.checkClusterExistence(lang1, lang2, principal.getName());
        Map<String, Object> map = new HashMap<>();
        map.put("exists", exists);
        return map;
    }
}
