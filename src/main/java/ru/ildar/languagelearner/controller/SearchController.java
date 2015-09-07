package ru.ildar.languagelearner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.ildar.languagelearner.controller.dto.LessonDTO;
import ru.ildar.languagelearner.controller.dto.PageRetrievalResult;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.database.domain.Translation;
import ru.ildar.languagelearner.service.LessonService;
import ru.ildar.languagelearner.service.TranslationService;

import javax.ws.rs.core.MediaType;
import java.security.Principal;

@Controller
@RequestMapping("/search")
public class SearchController
{
    @Autowired
    private LessonService lessonService;
    @Autowired
    private TranslationService translationService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView searchInfo(@RequestParam("searchText") String searchText,
                                   @RequestParam(value = "page", required = false) Integer page,
                                   Principal principal, ModelMap model)
    {
        if(page == null)
            page = 1;

        PageRetrievalResult<LessonDTO> retrievalResult = searchLessonInfo(searchText, page, principal);
        model.addAttribute("searchQuery", searchText);
        return new ModelAndView("search/searchResults", "retrievalResult", retrievalResult);
    }

    @RequestMapping(value = "lessonInfo", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public PageRetrievalResult<LessonDTO> searchLessonInfo(@RequestParam("searchText") String searchText,
                                         @RequestParam("page") int page,
                                         Principal principal)
    {
        return lessonService.searchLessons(searchText, principal.getName(), page);
    }

    @RequestMapping(value = "translationInfo", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public PageRetrievalResult<LessonDTO> searchTranslationInfo(
                                @RequestParam("searchText") String searchText,
                                @RequestParam("page") int page,
                                Principal principal)
    {
        return translationService.searchTranslations(searchText, principal.getName(), page);
    }
}
