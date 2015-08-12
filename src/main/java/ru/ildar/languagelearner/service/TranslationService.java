package ru.ildar.languagelearner.service;

import ru.ildar.languagelearner.database.domain.Translation;
import ru.ildar.languagelearner.exercise.Exerciser;
import ru.ildar.languagelearner.controller.dto.TranslationDTO;
import ru.ildar.languagelearner.database.domain.Lesson;

import java.util.List;

public interface TranslationService
{
    /**
     * Add new translations as part of this lesson.
     * Sentence 1 of each translation will be considered to have language 1
     * from the cluster definition, and sentence 2 will be considered to have language 2
     * @param lesson Lesson which owns these translations
     * @param translations Translations to add
     */
    void addTranslations(Lesson lesson, List<TranslationDTO> translations);

    void fillExerciser(long lessonId, Exerciser exerciser, String nickname);

    void setTranslationsCount(Exerciser exerciser, long lessonId);

    Translation getTranslation(long translationId);
}
