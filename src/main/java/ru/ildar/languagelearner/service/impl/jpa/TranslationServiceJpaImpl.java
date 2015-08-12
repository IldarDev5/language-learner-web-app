package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ildar.languagelearner.exception.LessonNotFoundException;
import ru.ildar.languagelearner.exercise.Exerciser;
import ru.ildar.languagelearner.controller.dto.TranslationDTO;
import ru.ildar.languagelearner.database.dao.LessonRepository;
import ru.ildar.languagelearner.database.dao.TranslationRepository;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.database.domain.Translation;
import ru.ildar.languagelearner.exception.LessonNotOfThisUserException;
import ru.ildar.languagelearner.service.TranslationService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service("translationService")
@Transactional
public class TranslationServiceJpaImpl implements TranslationService
{
    private TranslationRepository translationRepository;
    private LessonRepository lessonRepository;

    @Autowired
    public TranslationServiceJpaImpl(TranslationRepository translationRepository,
                                     LessonRepository lessonRepository)
    {
        this.translationRepository = translationRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public void addTranslations(Lesson lesson, List<TranslationDTO> translations)
    {
        if(lesson == null || translations == null)
        {
            throw new IllegalArgumentException("Lesson and translations must not be null.");
        }
        if(lesson.getCluster() == null)
        {
            throw new IllegalArgumentException("Lesson cluster must not be null.");
        }

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

    @Override
    public void fillExerciser(long lessonId, Exerciser exerciser, String nickname)
    {
        if(exerciser == null || nickname == null)
        {
            throw new IllegalArgumentException("Exerciser and nickname arguments must not be null.");
        }

        Lesson lesson = lessonRepository.findOne(lessonId);
        if(lesson == null)
        {
            throw new LessonNotFoundException(lessonId);
        }

        if(!lesson.getCluster().getAppUser().getNickname().equals(nickname))
        {
            throw new LessonNotOfThisUserException();
        }

        List<Translation> translations = translationRepository.findByLesson_LessonId(lessonId);
        List<TranslationDTO> translationDTOs = translations.stream()
                .map((tr) ->
                {
                    TranslationDTO translation = new TranslationDTO();
                    translation.setTranslationId(tr.getTranslationId());
                    translation.setSentence1(tr.getSentence1());
                    translation.setSentence2(tr.getSentence2());
                    return translation;
                })
                .collect(toList());
        exerciser.setCorrectTranslations(translationDTOs);
    }

    @Override
    @Transactional(readOnly = true)
    public void setTranslationsCount(Exerciser exerciser, long lessonId)
    {
        if(exerciser == null)
        {
            throw new IllegalArgumentException("Exerciser must not be null.");
        }

        Long count = lessonRepository.findTranslationsCount(lessonId);
        if(count == null)
        {
            throw new LessonNotFoundException(lessonId);
        }

        exerciser.setTranslationsCount(count);
    }

    @Override
    public Translation getTranslation(long translationId)
    {
        return translationRepository.findOne(translationId);
    }
}
