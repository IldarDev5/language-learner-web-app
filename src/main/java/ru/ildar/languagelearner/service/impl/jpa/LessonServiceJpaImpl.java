package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.ildar.languagelearner.controller.dto.LessonDTO;
import ru.ildar.languagelearner.controller.dto.PageRetrievalResult;
import ru.ildar.languagelearner.database.dao.LessonRepository;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.exception.LessonNotOfThisUserException;
import ru.ildar.languagelearner.service.LessonService;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("lessonService")
@Transactional
public class LessonServiceJpaImpl implements LessonService
{
    private LessonRepository lessonRepository;

    @Autowired
    public LessonServiceJpaImpl(LessonRepository lessonRepository)
    {
        this.lessonRepository = lessonRepository;
    }

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
        /*Find a lesson list from the specified page, but before then sort the query result by
            the lesson add date field in descending order */
        Sort sort = new Sort(Sort.Direction.DESC, "addDate");
        PageRequest pageRequest = new PageRequest(page - 1, LESSONS_PER_PAGE, sort);
        return lessonRepository.findByCluster(cluster, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
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
            //Lesson doesn't belong to this user
        {
            throw new LessonNotOfThisUserException();
        }

        lessonRepository.delete(lesson);
    }

    @Override
    @Transactional(readOnly = true)
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
    public void addTestGrade(long lessonId, double grade, String nickname)
    {
        Lesson lesson = lessonRepository.findOne(lessonId);
        if(!lesson.getCluster().getAppUser().getNickname().equals(nickname))
            //Lesson doesn't belong to this user
        {
            throw new LessonNotOfThisUserException();
        }

        lesson.setSumGrade(lesson.getSumGrade() + grade);
        lesson.setTimesLessonTaken(lesson.getTimesLessonTaken() + 1);
        lesson.setLastTaken(new Date());
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Lesson> getLessonsNotTakenLongestTime(int lessonsToTake, String username)
    {
        PageRequest pageRequest = new PageRequest(0, lessonsToTake);
        return lessonRepository.findLessonsNotTakenLongestTime(username, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public PageRetrievalResult<Lesson> searchLessons(String searchQuery, String username, int page)
    {
        PageRequest pageRequest = new PageRequest(page - 1, LESSONS_PER_PAGE);
        Page<Lesson> lessons = lessonRepository.searchLessonsOfUser(searchQuery.toLowerCase(),
                username, pageRequest);

        return new PageRetrievalResult<>(lessons.getContent(), lessons.getTotalPages());
    }
}