package ru.ildar.languagelearner.exercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.ildar.languagelearner.controller.dto.ExerciseConfigDTO;
import ru.ildar.languagelearner.controller.dto.TranslationDTO;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Lesson;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Random;

/** Exerciser of a current user(session). */
@Component("userExerciser")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Exerciser implements Serializable
{
    @Autowired
    private ExerciserGrade exerciserGrade;

    private Lesson lesson;

    private List<TranslationDTO> correctTranslations;
    private List<TranslationDTO> actualTranslations;
    private TranslationDTO deletedTranslation;

    private TranslationDTO currentTranslation;
    private long translationsCount;
    private ExerciseConfigDTO config;

    private int questionNumber = 1;
    private Double sumGrade;

    private Random rand = new Random(new Date().getTime());

    public boolean continueTest()
    {
        return questionNumber <= config.getCountOfQuestions();
    }

    public void incrementQuestionNumber() { questionNumber++; }

    public void saveCurrentTranslation()
    {
        //actualTranslations.add(currentTranslation);
    }

    public void nextQuestion()
    {
        int nextTrans = rand.nextInt(correctTranslations.size());
        TranslationDTO translation = correctTranslations.get(nextTrans);
        if(config.isUniqueQuestions())
        {
            correctTranslations.remove(nextTrans);
            deletedTranslation = translation;
        }

        String sentenceToTrans = config.isInvertLanguages() ? translation.getSentence2() :
                translation.getSentence1();

        TranslationDTO currentTrans = new TranslationDTO();
        currentTrans.setTranslationId(translation.getTranslationId());
        currentTrans.setSentence1(sentenceToTrans);

        currentTranslation = currentTrans;
    }

    public void incrementSumGrade()
    {
        sumGrade += exerciserGrade.getGrade();
    }

    public Double getTotalGrade()
    {
        return (double)((int)(sumGrade / (questionNumber - 1) * 10000)) / 10000;
    }

    public void resetExerciser()
    {
        lesson = null;
        correctTranslations = null;
        actualTranslations = null;
        currentTranslation = null;
        translationsCount = 0;
        config = null;
        questionNumber = 1;
        sumGrade = 0.0;
    }

    public Lesson getLesson()
    {
        return lesson;
    }

    public void setLesson(Lesson lesson)
    {
        this.lesson = lesson;
        this.exerciserGrade.setLessonId(lesson.getLessonId());
    }

    public List<TranslationDTO> getCorrectTranslations()
    {
        return correctTranslations;
    }

    public void setCorrectTranslations(List<TranslationDTO> correctTranslations)
    {
        this.correctTranslations = correctTranslations;
    }

    public ExerciseConfigDTO getConfig()
    {
        return config;
    }

    public void setConfig(ExerciseConfigDTO config)
    {
        this.config = config;
    }

    public int getQuestionNumber()
    {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber)
    {
        this.questionNumber = questionNumber;
    }

    public List<TranslationDTO> getActualTranslations()
    {
        return actualTranslations;
    }

    public void setActualTranslations(List<TranslationDTO> actualTranslations)
    {
        this.actualTranslations = actualTranslations;
    }

    public TranslationDTO getCurrentTranslation()
    {
        return currentTranslation;
    }

    public void setCurrentTranslation(TranslationDTO currentTranslation)
    {
        this.currentTranslation = currentTranslation;
    }

    public long getTranslationsCount()
    {
        return translationsCount;
    }

    public void setTranslationsCount(long translationsCount)
    {
        this.translationsCount = translationsCount;
    }

    public Double getSumGrade()
    {
        return sumGrade;
    }

    public void setSumGrade(Double sumGrade)
    {
        this.sumGrade = sumGrade;
    }
}
