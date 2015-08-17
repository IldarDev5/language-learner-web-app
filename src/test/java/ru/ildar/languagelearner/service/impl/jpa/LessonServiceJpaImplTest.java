package ru.ildar.languagelearner.service.impl.jpa;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.ildar.languagelearner.controller.dto.LessonDTO;
import ru.ildar.languagelearner.controller.dto.TranslationDTO;
import ru.ildar.languagelearner.database.domain.AppUser;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.exception.LessonNotOfThisUserException;
import ru.ildar.languagelearner.service.BaseServiceTest;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class LessonServiceJpaImplTest extends BaseServiceTest
{
    @Test
    public void testGetLessons()
    {
        Lesson l1 = new Lesson();
        Lesson l2 = new Lesson();
        Cluster cluster = new Cluster();

        doReturn(Arrays.asList(l1, l2)).when(lessonRepository).findByCluster(cluster);

        List<Lesson> ret = lessonService.getLessons(cluster);
        assertThat(ret.size(), is(2));
        assertThat(ret, contains(l1, l2));

        verify(lessonRepository).findByCluster(cluster);
        verifyNoMoreInteractions(repos);
    }

    @Test
    public void testTotalLessonPages()
    {
        long[] l = {9, 10, 11, 15, 20};
        int[] pages = { 1, 1, 2, 2, 2 };
        for(int i = 0;i < l.length;i++)
        {
            doReturn(l[i]).when(lessonRepository).count();
            assertThat(lessonService.totalLessonPages(), is(pages[i]));
        }

        verify(lessonRepository, times(l.length)).count();
        verifyNoMoreInteractions(repos);
    }

    @Test
    public void testDeleteLesson_LessonIsNull_ShouldOmitCallingRepositoryMethod()
    {
        lessonService.deleteLesson(null, "Ildar");
        verifyZeroInteractions(repos);
    }

    @Test(expected = LessonNotOfThisUserException.class)
    public void testDeleteLesson_LessonNotOfThisUser_ShouldThrowException()
    {
        String nickname = "Ildar";
        String anotherNick = "James";
        Lesson lesson = new Lesson(new Cluster(new AppUser(anotherNick)));

        lessonService.deleteLesson(lesson, nickname);
    }

    @Test
    public void testDeleteLesson_EverythingIsOk()
    {
        String nickname = "Ildar";
        Lesson lesson = new Lesson(new Cluster(new AppUser(nickname)));

        lessonService.deleteLesson(lesson, nickname);

        verify(lessonRepository).delete(lesson);
        verifyNoMoreInteractions(repos);
    }

    @Test
    public void testAddLesson()
    {
        Cluster cluster = new Cluster();
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setDescription("descr");
        lessonDTO.setLessonName("name");


        lessonService.addLesson(cluster, lessonDTO);
        ArgumentCaptor<Lesson> captor = ArgumentCaptor.forClass(Lesson.class);
        verify(lessonRepository).save(captor.capture());
        Lesson lesson = captor.getValue();

        assertThat(lesson.getCluster(), is(cluster));
        assertThat(lesson.getDescription(), is(lessonDTO.getDescription()));
        assertThat(lesson.getLessonName(), is(lessonDTO.getLessonName()));

        verifyNoMoreInteractions(repos);
    }

    @Test(expected = LessonNotOfThisUserException.class)
    public void testAddTestGrade_LessonNotOfThisUser_ShouldThrowException()
    {
        String nickname = "Ildar";
        String anotherNick = "James";
        long lessonId = 5;
        Lesson lesson = new Lesson(new Cluster(new AppUser(anotherNick)));

        doReturn(lesson).when(lessonRepository).findOne(lessonId);

        lessonService.addTestGrade(lessonId, 0.9, nickname);
    }

    @Test
    public void testAddTestGrade_EverythingIsOk()
    {
        String nickname = "Ildar";
        long lessonId = 5;
        Lesson lesson = new Lesson(new Cluster(new AppUser(nickname)));
        lesson.setSumGrade(5.0);
        lesson.setTimesLessonTaken(10);

        doReturn(lesson).when(lessonRepository).findOne(lessonId);

        lessonService.addTestGrade(lessonId, 0.9, nickname);

        assertThat(lesson.getSumGrade(), is(5.9));
        assertThat(lesson.getTimesLessonTaken(), is(11l));

        verify(lessonRepository).findOne(lessonId);
        verifyNoMoreInteractions(repos);
    }

    @Test
    public void testGetLessonsForPage()
    {
        Cluster cluster = new Cluster();
        int page = 10;
        List<Lesson> lessons = Arrays.asList(new Lesson(), new Lesson());

        doReturn(lessons).when(lessonRepository).findByCluster(Matchers.any(Cluster.class),
                Matchers.any(PageRequest.class));

        List<Lesson> ret = lessonService.getLessonsForPage(cluster, page);
        assertThat(ret, is(lessons));
        assertThat(ret.size(), is(2));

        ArgumentCaptor<Cluster> clusterCaptor = ArgumentCaptor.forClass(Cluster.class);
        ArgumentCaptor<PageRequest> pageCaptor = ArgumentCaptor.forClass(PageRequest.class);

        verify(lessonRepository).findByCluster(clusterCaptor.capture(), pageCaptor.capture());

        Cluster retCl = clusterCaptor.getValue();
        PageRequest retPR = pageCaptor.getValue();

        assertThat(retCl, is(cluster));
        assertThat(retPR.getSort(), is(new Sort(Sort.Direction.DESC, "addDate")));
        assertThat(retPR.getPageNumber(), is(page - 1));
        assertThat(retPR.getPageSize(), is(10));

        verifyNoMoreInteractions(repos);
    }
}
