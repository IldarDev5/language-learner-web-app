package ru.ildar.languagelearner.controller.dto.util;

import ru.ildar.languagelearner.controller.dto.LessonDTO;
import ru.ildar.languagelearner.controller.dto.TranslationDTO;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.database.domain.Translation;

import java.text.SimpleDateFormat;

public final class DtoConverter
{
    private static SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

    private DtoConverter() { }

    public static LessonDTO convertLessonToDTO(Lesson l)
    {
        return new LessonDTO(l.getLessonId(), l.getLessonName(),
                l.getDescription(), l.averageGrade(), fmt.format(l.getAddDate()),
                l.getAddDate(), l.getTranslationsCount());
    }

    public static TranslationDTO convertTranslationToDTO(Translation tr)
    {
        return new TranslationDTO(tr.getTranslationId(), tr.getSentence1(), tr.getSentence2());
    }
}
