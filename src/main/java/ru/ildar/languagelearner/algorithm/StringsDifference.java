package ru.ildar.languagelearner.algorithm;

import java.util.Map;

/** Object that carries the information about modifications needed to
 * turn the string to another string. */
public class StringsDifference
{
    private String correctSentence;

    private Map<Integer, Modification> modifications;
    private Integer howMuchAdd;

    public StringsDifference() { }
    public StringsDifference(String correctSentence,
                             Map<Integer, Modification> modifications, Integer howMuchAdd)
    {
        this.correctSentence = correctSentence;
        this.modifications = modifications;
        this.howMuchAdd = howMuchAdd;
    }

    public String getCorrectSentence()
    {
        return correctSentence;
    }

    public Map<Integer, Modification> getModifications()
    {
        return modifications;
    }

    public Integer getHowMuchAdd()
    {
        return howMuchAdd;
    }
}

class Modification
{
    public enum ModifOperation { DELETE, INSERT, SYMBOLS_EQUAL, DELETE_ALL, INSERT_ALL, REPLACE }

    private ModifOperation modifOperation;
    private Character symbol;

    public Modification() { }
    public Modification(ModifOperation modifOperation)
    {
        this.modifOperation = modifOperation;
    }
    public Modification(ModifOperation modifOperation, Character symbol)
    {
        this.modifOperation = modifOperation;
        this.symbol = symbol;
    }

    public ModifOperation getModifOperation()
    {
        return modifOperation;
    }

    public Character getSymbol()
    {
        return symbol;
    }
}