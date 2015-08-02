package ru.ildar.languagelearner.controller.lesson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.ildar.languagelearner.controller.dto.LessonDTO;
import ru.ildar.languagelearner.controller.dto.TranslationDTO;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.service.ClusterService;
import ru.ildar.languagelearner.service.LessonService;
import ru.ildar.languagelearner.service.TranslationService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/lessons")
public class AddLessonController
{
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private TranslationService translationService;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView createLessonPage(@RequestParam("clusterId") Cluster cluster,
                                         LessonDTO lessonDTO, ModelMap model)
    {
        lessonDTO.setClusterId(cluster.getClusterId());
        model.addAttribute("language1", cluster.getLanguage1().getDefaultName());
        model.addAttribute("language2", cluster.getLanguage2().getDefaultName());
        return new ModelAndView("lesson/createLesson", "lesson", lessonDTO);
    }

    @RequestMapping(value = "create", params = {"addTranslation"})
    public ModelAndView addTranslation(LessonDTO lessonDTO, ModelMap model)
    {
        lessonDTO.getTranslations().add(new TranslationDTO());
        Cluster cluster = clusterService.getClusterById(lessonDTO.getClusterId());
        return createLessonPage(cluster, lessonDTO, model);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ModelAndView createLesson(@ModelAttribute("lesson") @Valid LessonDTO lessonDTO,
                                     BindingResult bindingResult, Principal principal,
                                     ModelMap model)
    {
        long clusterId = lessonDTO.getClusterId();
        Cluster cluster = clusterService.checkClusterOwner(clusterId, principal.getName());

        if(bindingResult.hasFieldErrors())
        {
            return createLessonPage(cluster, lessonDTO, model);
        }

        Lesson lesson = lessonService.addLesson(cluster, lessonDTO);
        translationService.addTranslations(lesson, lessonDTO.getTranslations());

        return new ModelAndView("redirect:/lessons/viewLessons?clusterId=" + clusterId);
    }
}
