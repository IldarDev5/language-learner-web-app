package ru.ildar.languagelearner.controller.dto;

import java.io.Serializable;

public class ExerciseConfigDTO implements Serializable
{
    private int countOfQuestions;
    private boolean uniqueQuestions;
    private boolean invertLanguages;
    private boolean showLessonBeforeExercise;
    //TODO: See if everything will be ok without setters

    /** Count of questions to have in the test */
    public int getCountOfQuestions()
    {
        return countOfQuestions;
    }

    public void setCountOfQuestions(int countOfQuestions)
    {
        this.countOfQuestions = countOfQuestions;
    }

    /** Should all questions be unique or there should be also duplicates.
     * If true, the count of questions is equal to the count of translations in the lesson. */
    public boolean isUniqueQuestions()
    {
        return uniqueQuestions;
    }

    public void setUniqueQuestions(boolean uniqueQuestions)
    {
        this.uniqueQuestions = uniqueQuestions;
    }

    /** Should primary language (that, sentences in which are to be translated) be the second one */
    public boolean isInvertLanguages()
    {
        return invertLanguages;
    }

    public void setInvertLanguages(boolean invertLanguages)
    {
        this.invertLanguages = invertLanguages;
    }

    /** Show the list of sentence pairs of the lesson before starting the exercise */
    public boolean isShowLessonBeforeExercise()
    {
        return showLessonBeforeExercise;
    }

    public void setShowLessonBeforeExercise(boolean showLessonBeforeExercise)
    {
        this.showLessonBeforeExercise = showLessonBeforeExercise;
    }
}
