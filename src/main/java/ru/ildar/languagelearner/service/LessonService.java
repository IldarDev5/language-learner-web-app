package ru.ildar.languagelearner.service;

import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Lesson;

import java.util.List;

public interface LessonService
{
    List<Lesson> getLessons(Cluster cluster);
}
