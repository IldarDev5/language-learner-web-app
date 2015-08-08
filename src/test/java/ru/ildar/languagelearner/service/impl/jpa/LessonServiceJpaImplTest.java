package ru.ildar.languagelearner.service.impl.jpa;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
}
