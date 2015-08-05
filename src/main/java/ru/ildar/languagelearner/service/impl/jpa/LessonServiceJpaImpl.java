package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.ildar.languagelearner.controller.dto.LessonDTO;
import ru.ildar.languagelearner.database.dao.LessonRepository;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.exception.LessonNotOfThisUserException;
import ru.ildar.languagelearner.service.LessonService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LessonServiceJpaImpl implements LessonService
{
    @Autowired
    private LessonRepository lessonRepository;

    private final int LESSONS_PER_PAGE = 10;

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getLessons(Cluster cluster)
    {
        return lessonRepository.findByCluster(cluster);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsForPage(Cluster cluster, int page)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "addDate");
        PageRequest pageRequest = new PageRequest(page - 1, LESSONS_PER_PAGE, sort);
        return lessonRepository.findByCluster(cluster, pageRequest);
    }

    @Override
    public int totalLessonPages()
    {
        long c = lessonRepository.count();
        return (int)(c / LESSONS_PER_PAGE + (c % LESSONS_PER_PAGE == 0 ? 0 : 1));
    }

    @Override
    public void deleteLesson(Lesson lesson, String nickname)
    {
        if(lesson == null)
        {
            return;
        }

        if(!lesson.getCluster().getAppUser().getNickname().equals(nickname))
        {
            throw new LessonNotOfThisUserException();
        }

        lessonRepository.delete(lesson);
    }

    @Override
    public Lesson getLessonById(long lessonId)
    {
        return lessonRepository.findOne(lessonId);
    }

    @Override
    public Lesson addLesson(Cluster cluster, LessonDTO lessonDTO)
    {
        Lesson lesson = new Lesson();
        lesson.setCluster(cluster);
        lesson.setLessonName(lessonDTO.getLessonName());
        lesson.setDescription(lessonDTO.getDescription());

        return lessonRepository.save(lesson);
    }

    @Override
    public void addTestGrade(long lessonId, int grade, String nickname)
    {
        Lesson lesson = lessonRepository.findOne(lessonId);
        if(!lesson.getCluster().getAppUser().getNickname().equals(nickname))
        {
            throw new LessonNotOfThisUserException();
        }

        lesson.setSumGrade(lesson.getSumGrade() + grade);
        lesson.setTimesLessonTaken(lesson.getTimesLessonTaken() + 1);
    }
}
