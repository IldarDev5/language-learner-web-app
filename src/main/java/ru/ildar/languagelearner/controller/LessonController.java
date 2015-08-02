package ru.ildar.languagelearner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.service.ClusterService;
import ru.ildar.languagelearner.service.LessonService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/lessons")
public class LessonController
{
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ClusterService clusterService;

    @RequestMapping(value = "viewLessons", method = RequestMethod.GET)
    public String viewLessons(@RequestParam("clusterId") long clusterId)
    {
        return "redirect:viewLessonsPage?page=1&clusterId=" + clusterId;
    }

    @RequestMapping(value = "viewLessonsPage", method = RequestMethod.GET)
    public ModelAndView viewLessons(@RequestParam("clusterId") long clusterId,
                                    @RequestParam("page") int page,
                                    ModelMap model)
    {
        int pagesCount = lessonService.totalLessonPages();
        if(pagesCount == 0)
        {
            pagesCount = 1;
        }

        if(page > pagesCount)
        {
            page = pagesCount;
        }

        Cluster cluster = clusterService.getClusterById(clusterId);
        List<Lesson> lessons = lessonService.getLessonsForPage(cluster, page);
        model.addAttribute("pagesCount", pagesCount);
        model.addAttribute("language1", cluster.getLanguage1().getDefaultName());
        model.addAttribute("language2", cluster.getLanguage2().getDefaultName());
        return new ModelAndView("lesson/viewLessons", "lessons", lessons);
    }

    @RequestMapping(value = "removeLesson", method = RequestMethod.POST)
    public String removeLesson(@RequestParam("lessonId") long lessonId,
                               @RequestParam("page") int page,
                               Principal principal)
    {
        Lesson lesson = lessonService.getLessonById(lessonId);
        if(lesson == null)
            //No lesson found
        {
            return "redirect:/";
        }

        lessonService.deleteLesson(lesson, principal.getName());
        return "redirect:viewLessonsPage?page=" + page + "&clusterId=" + lesson.getCluster().getClusterId();
    }
}
