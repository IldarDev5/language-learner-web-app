package ru.ildar.languagelearner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.ildar.languagelearner.algorithm.StringsDifference;
import ru.ildar.languagelearner.algorithm.StringsDifferenceAlgorithm;
import ru.ildar.languagelearner.database.domain.Translation;
import ru.ildar.languagelearner.exception.LessonNotOfThisUserException;
import ru.ildar.languagelearner.service.TranslationService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/translation")
public class TranslationController
{
    @Autowired
    private TranslationService translationService;
    @Autowired
    private StringsDifferenceAlgorithm stringsDifferenceAlgorithm;

    @RequestMapping(value = "checkTranslation", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> checkTranslation(@RequestParam("translationId") Long translationId,
                                   @RequestParam("translation") String translationSentence,
                                   @RequestParam("invertLanguages") Boolean invertLanguages,
                                   Principal principal)
    {
        Map<String, Object> toReturn = new HashMap<>();

        Translation tr = translationService.getTranslation(translationId);
        String username = principal.getName();
        if(!tr.getLesson().getCluster().getAppUser().getNickname().equals(username))
            //User that is not the owner of this cluster is trying to manipulate this translation
        {
            toReturn.put("error", "NotRightUser");
            return toReturn;
        }

        String correctTranslation = invertLanguages ? tr.getSentence1() : tr.getSentence2();
        StringsDifference difference = stringsDifferenceAlgorithm
                .calculateDifference(correctTranslation, translationSentence);
        toReturn.put("difference", difference);
        return toReturn;
    }
}
