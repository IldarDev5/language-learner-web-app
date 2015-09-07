package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ildar.languagelearner.controller.dto.LessonDTO;
import ru.ildar.languagelearner.controller.dto.PageRetrievalResult;
import ru.ildar.languagelearner.controller.dto.util.DtoConverter;
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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static ru.ildar.languagelearner.controller.dto.util.DtoConverter.convertLessonToDTO;
import static ru.ildar.languagelearner.controller.dto.util.DtoConverter.convertTranslationToDTO;

@Service("translationService")
@Transactional
public class TranslationServiceJpaImpl implements TranslationService
{
    private TranslationRepository translationRepository;
    private LessonRepository lessonRepository;

    private final int TRANSLATIONS_PER_PAGE = 10;

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
    @Transactional(readOnly = true)
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
                .map(DtoConverter::convertTranslationToDTO)
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
            //This means that the lesson is not found
        {
            throw new LessonNotFoundException(lessonId);
        }

        exerciser.setTranslationsCount(count);
    }

    @Override
    @Transactional(readOnly = true)
    public Translation getTranslation(long translationId)
    {
        return translationRepository.findOne(translationId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageRetrievalResult<LessonDTO> searchTranslations(String searchQuery, String username, int page)
    {
        PageRequest pageRequest = new PageRequest(page - 1, TRANSLATIONS_PER_PAGE);
        Page<Translation> ret = translationRepository.searchTranslationPairs(searchQuery.toLowerCase(),
                username, pageRequest);

        /* Group translations by their lessons, and then make this query return LessonDTOs with
           translations converted to DTOs and set to the field translations of LessonDTOs */
        List<LessonDTO> lessons = ret.getContent().stream()
                .collect(groupingBy(Translation::getLesson))
                .entrySet().stream().map((pair) -> {
                    LessonDTO dto = convertLessonToDTO(pair.getKey());
                    dto.setTranslations(pair.getValue().stream()
                            .map(DtoConverter::convertTranslationToDTO).collect(toList()));
                    return dto;
                }).collect(toList());
        return new PageRetrievalResult<>(lessons, ret.getTotalPages());
    }
}
