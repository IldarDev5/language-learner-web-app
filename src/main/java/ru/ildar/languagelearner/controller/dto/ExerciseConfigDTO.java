package ru.ildar.languagelearner.controller.dto;

import java.io.Serializable;

public class ExerciseConfigDTO implements Serializable
{
    /** Count of questions to have in the test */
    private int countOfQuestions;
    /** Should all questions be unique or there should be also duplicates.
     * If true, the count of questions is equal to the count of translations in the lesson. */
    private boolean uniqueQuestions;
    /** Should primary language (that, sentences in which are to be translated) be the second one */
    private boolean invertLanguages;

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
}
