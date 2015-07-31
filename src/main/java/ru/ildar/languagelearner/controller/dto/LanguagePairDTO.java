package ru.ildar.languagelearner.controller.dto;

/** DTO class for language pair to show in view */
public class LanguagePairDTO
{
    private String language1;
    private String language2;

    public LanguagePairDTO() { }
    public LanguagePairDTO(String language1, String language2)
    {
        this.language1 = language1;
        this.language2 = language2;
    }

    public String getLanguage1()
    {
        return language1;
    }

    public void setLanguage1(String language1)
    {
        this.language1 = language1;
    }

    public String getLanguage2()
    {
        return language2;
    }

    public void setLanguage2(String language2)
    {
        this.language2 = language2;
    }
}
