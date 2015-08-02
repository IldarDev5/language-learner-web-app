package ru.ildar.languagelearner.exception;

public class LanguageNotFoundException extends Exception
{
    private int languageNumber;

    public LanguageNotFoundException(int langNumber)
    {
        this.languageNumber = langNumber;
    }

    public int getLanguageNumber()
    {
        return languageNumber;
    }
}
