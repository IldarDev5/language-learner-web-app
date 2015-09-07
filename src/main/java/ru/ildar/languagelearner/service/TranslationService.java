package ru.ildar.languagelearner.service;

import ru.ildar.languagelearner.controller.dto.LessonDTO;
import ru.ildar.languagelearner.controller.dto.PageRetrievalResult;
import ru.ildar.languagelearner.database.domain.Translation;
import ru.ildar.languagelearner.exercise.Exerciser;
import ru.ildar.languagelearner.controller.dto.TranslationDTO;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.exception.*;

import java.util.List;

public interface TranslationService
{
    /**
     * Add new translations as part of this lesson.
     * Sentence 1 of each translation will be considered to have language 1
     * from the cluster definition, and sentence 2 will be considered to have language 2.
     * @param lesson Lesson which owns these translations
     * @param translations Translations to add
     * @throws IllegalArgumentException if any of the arguments are null
     * or the lesson cluster is null
     */
    void addTranslations(Lesson lesson, List<TranslationDTO> translations);

    /**
     * Fill the exerciser with a list of correct translations of this lesson.
     * The method checks if this lesson doesn't belong to this user.
     * @param lessonId ID of the lesson
     * @param exerciser Exerciser object
     * @param nickname Nickname of the user
     * @throws IllegalArgumentException if any of the arguments are null
     * @throws LessonNotFoundException If the lesson specified by this ID is not found
     * @throws LessonNotOfThisUserException If the lesson does not belong to this user
     */
    void fillExerciser(long lessonId, Exerciser exerciser, String nickname);

    /**
     * Sets the translations count of this lesson to exerciser.
     * @param exerciser Exerciser object
     * @param lessonId ID of the lesson whose translations count value to set
     * @throws IllegalArgumentException If the exerciser argument is null
     * @throws LessonNotFoundException If the lesson specified by this ID is not found
     */
    void setTranslationsCount(Exerciser exerciser, long lessonId);

    /**
     * Returns the translation specified by this ID
     */
    Translation getTranslation(long translationId);

    /**
     * Find translation pairs by this search query. Returning translations must have this search string
     * present either in the main sentence, or in its translation.<br />
     * Search query will be used in the lower case, search is made ignorant to the case of letters.
     * @param searchQuery String to search translation pairs by
     * @param username The name of the user whose translation pairs to search
     * @param page Page of the returning data
     */
    PageRetrievalResult<LessonDTO> searchTranslations(String searchQuery, String username, int page);
}
