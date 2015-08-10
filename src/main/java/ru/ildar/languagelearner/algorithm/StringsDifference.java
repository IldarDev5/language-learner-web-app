package ru.ildar.languagelearner.algorithm;

import java.util.Map;

/** Object that carries the information about modifications needed to
 * turn the string to another string. */
public class StringsDifference
{
    private String correctSentence;

    public enum Modification { DELETE, INSERT, REPLACE }

    private Map<Integer, Modification> modifications;

    public StringsDifference() { }
    public StringsDifference(String correctSentence, Map<Integer, Modification> modifications)
    {
        this.correctSentence = correctSentence;
        this.modifications = modifications;
    }

    public String getCorrectSentence()
    {
        return correctSentence;
    }

    public void setCorrectSentence(String correctSentence)
    {
        this.correctSentence = correctSentence;
    }

    public Map<Integer, Modification> getModifications()
    {
        return modifications;
    }

    public void setModifications(Map<Integer, Modification> modifications)
    {
        this.modifications = modifications;
    }
}
