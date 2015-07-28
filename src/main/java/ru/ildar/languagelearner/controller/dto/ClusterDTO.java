package ru.ildar.languagelearner.controller.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ClusterDTO
{
    @NotNull
    @Size(min = 2, max = 100)
    private String language1;

    @NotNull
    @Size(min = 2, max = 100)
    private String language2;

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
