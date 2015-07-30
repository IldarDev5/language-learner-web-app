package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ildar.languagelearner.database.dao.LessonRepository;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.service.LessonService;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LessonServiceJpaImpl implements LessonService
{
    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public List<Lesson> getLessons(Cluster cluster)
    {
        return lessonRepository.findByCluster(cluster);
    }
}
