package ru.ildar.languagelearner.controller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TranslationDTO
{
    @Min(0)
    private Long translationId;

    @NotNull
    @Size(max = 500)
    private String sentence1;
    @NotNull
    @Size(max = 500)
    private String sentence2;

    public Long getTranslationId()
    {
        return translationId;
    }

    public void setTranslationId(Long translationId)
    {
        this.translationId = translationId;
    }

    public String getSentence1()
    {
        return sentence1;
    }

    public void setSentence1(String sentence1)
    {
        this.sentence1 = sentence1;
    }

    public String getSentence2()
    {
        return sentence2;
    }

    public void setSentence2(String sentence2)
    {
        this.sentence2 = sentence2;
    }
}
