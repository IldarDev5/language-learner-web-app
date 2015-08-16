package ru.ildar.languagelearner.controller.dto;

public class ExerciseTranslationDTO
{
    private Long translationId;
    private String translationSentence;
    private Boolean invertLanguages;

    public Long getTranslationId()
    {
        return translationId;
    }

    public void setTranslationId(Long translationId)
    {
        this.translationId = translationId;
    }

    public String getTranslationSentence()
    {
        return translationSentence;
    }

    public void setTranslationSentence(String translationSentence)
    {
        this.translationSentence = translationSentence;
    }

    public Boolean getInvertLanguages()
    {
        return invertLanguages;
    }

    public void setInvertLanguages(Boolean invertLanguages)
    {
        this.invertLanguages = invertLanguages;
    }
}
