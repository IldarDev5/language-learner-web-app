package ru.ildar.languagelearner.service.impl.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ildar.languagelearner.database.dao.LessonRepository;
import ru.ildar.languagelearner.service.LessonService;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LessonServiceJpaImpl implements LessonService
{
    @Autowired
    private LessonRepository lessonRepository;
}
