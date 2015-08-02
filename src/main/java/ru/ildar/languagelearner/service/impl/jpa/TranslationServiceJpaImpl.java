package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ildar.languagelearner.controller.dto.TranslationDTO;
import ru.ildar.languagelearner.database.dao.TranslationRepository;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.database.domain.Translation;
import ru.ildar.languagelearner.service.TranslationService;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class TranslationServiceJpaImpl implements TranslationService
{
    @Autowired
    private TranslationRepository translationRepository;

    @Override
    public void addTranslations(Lesson lesson, List<TranslationDTO> translations)
    {
        List<Translation> translationsToSave = translations.stream()
                .map((tr) ->
                {
                    Translation translation = new Translation();
                    translation.setLesson(lesson);
                    translation.setSentence1(tr.getSentence1());
                    translation.setSentence2(tr.getSentence2());
                    translation.setSentence1Language(lesson.getCluster().getLanguage1());
                    translation.setSentence2Language(lesson.getCluster().getLanguage2());
                    return translation;
                })
                .collect(toList());

        translationRepository.save(translationsToSave);
    }
}
