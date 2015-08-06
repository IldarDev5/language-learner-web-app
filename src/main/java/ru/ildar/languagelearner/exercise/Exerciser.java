package ru.ildar.languagelearner.exercise;

import ru.ildar.languagelearner.controller.dto.ExerciseConfigDTO;
import ru.ildar.languagelearner.controller.dto.TranslationDTO;
import ru.ildar.languagelearner.database.domain.Cluster;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Exerciser implements Serializable
{
    private Cluster lessonCluster;

    private List<TranslationDTO> correctTranslations;
    private List<TranslationDTO> actualTranslations;
    private TranslationDTO deletedTranslation;

    private TranslationDTO currentTranslation;
    private long translationsCount;
    private ExerciseConfigDTO config;

    private int questionNumber = 0;
    private int grade;

    private Random rand = new Random(new Date().getTime());

    public boolean continueTest()
    {
        return questionNumber < config.getCountOfQuestions();
    }

    public void incrementQuestionNumber() { questionNumber++; }

    public void saveCurrentTranslation()
    {
        actualTranslations.add(currentTranslation);
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


    public Cluster getLessonCluster()
    {
        return lessonCluster;
    }

    public void setLessonCluster(Cluster lessonCluster)
    {
        this.lessonCluster = lessonCluster;
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

    public int getGrade()
    {
        return grade;
    }

    public void setGrade(int grade)
    {
        this.grade = grade;
    }
}
