package ru.ildar.languagelearner.database.dao;

import org.springframework.data.repository.CrudRepository;
import ru.ildar.languagelearner.database.domain.Lesson;

public interface LessonRepository extends CrudRepository<Lesson, Long>
{
}
