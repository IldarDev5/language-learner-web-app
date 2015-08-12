package ru.ildar.languagelearner.service.impl.jpa;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.ildar.languagelearner.controller.dto.TranslationDTO;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Language;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.database.domain.Translation;
import ru.ildar.languagelearner.exception.LessonNotFoundException;
import ru.ildar.languagelearner.exercise.Exerciser;
import ru.ildar.languagelearner.service.BaseServiceTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class TranslationServiceJpaImplTest extends BaseServiceTest
{
    @Test(expected = IllegalArgumentException.class)
    public void testAddTranslations_ArgumentsAreNull_ShouldThrowException()
    {
        translationService.addTranslations(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTranslations_LessonClusterIsNull_ShouldThrowException()
    {
        translationService.addTranslations(new Lesson(null), new ArrayList<>());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddTranslations_EverythingIsOk()
    {
        Cluster cluster = new Cluster();
        cluster.setLanguage1(new Language("English"));
        cluster.setLanguage2(new Language("Russian"));
        Lesson lesson = new Lesson(cluster);

        TranslationDTO translationDTO1 = new TranslationDTO();
        translationDTO1.setSentence1("Hello World");
        translationDTO1.setSentence2("Привет, мир");
        TranslationDTO translationDTO2 = new TranslationDTO();
        translationDTO2.setSentence1("I'm Ildar");
        translationDTO2.setSentence2("Я - Ильдар");
        List<TranslationDTO> translations = Arrays.asList(translationDTO1, translationDTO2);

        translationService.addTranslations(lesson, translations);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(translationRepository).save(captor.capture());
        List<Translation> ret = (List<Translation>)captor.getValue();

        assertThat(ret.size(), is(2));
        assertThat(ret.get(0), allOf(
                hasProperty("lesson", is(lesson)),
                hasProperty("sentence1", is(translationDTO1.getSentence1())),
                hasProperty("sentence2", is(translationDTO1.getSentence2())),
                hasProperty("sentence1Language", is(cluster.getLanguage1())),
                hasProperty("sentence2Language", is(cluster.getLanguage2()))
        ));
        assertThat(ret.get(1), allOf(
                hasProperty("lesson", is(lesson)),
                hasProperty("sentence1", is(translationDTO2.getSentence1())),
                hasProperty("sentence2", is(translationDTO2.getSentence2())),
                hasProperty("sentence1Language", is(cluster.getLanguage1())),
                hasProperty("sentence2Language", is(cluster.getLanguage2()))
        ));

        verifyZeroInteractions(repos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTranslationsCount_ExerciserIsNull_ShouldThrowException()
    {
        translationService.setTranslationsCount(null, 5);
    }

    @Test(expected = LessonNotFoundException.class)
    public void testSetTranslationsCount_CountIsNull_ShouldThrowException()
    {
        doReturn(null).when(lessonRepository).findTranslationsCount(5);
        translationService.setTranslationsCount(new Exerciser(), 5);
    }

    @Test
    public void testSetTranslationsCount_EverythingIsOk()
    {
        Exerciser exerciser = new Exerciser();
        long count = 10;
        long lessonId = 5;

        doReturn(count).when(lessonRepository).findTranslationsCount(lessonId);

        translationService.setTranslationsCount(exerciser, lessonId);

        verify(lessonRepository).findTranslationsCount(lessonId);
        verifyNoMoreInteractions(repos);

        assertThat(exerciser.getTranslationsCount(), is(count));
    }
}
