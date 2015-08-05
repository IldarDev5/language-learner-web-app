package ru.ildar.languagelearner.database.dao;

import org.springframework.data.repository.CrudRepository;
import ru.ildar.languagelearner.database.domain.Translation;

import java.util.List;

public interface TranslationRepository extends CrudRepository<Translation, Long>
{
    List<Translation> findByLesson_LessonId(long lessonId);
}
