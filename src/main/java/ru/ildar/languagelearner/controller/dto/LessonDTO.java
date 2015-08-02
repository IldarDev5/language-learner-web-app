package ru.ildar.languagelearner.controller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class LessonDTO
{
    @NotNull
    @Size(min = 3, max = 100)
    private String lessonName;
    @Size(min = 3, max = 300)
    private String description;
    @NotNull
    @Min(1)
    private Long clusterId;
    @NotNull
    private List<TranslationDTO> translations = new ArrayList<>();

    public String getLessonName()
    {
        return lessonName;
    }

    public void setLessonName(String lessonName)
    {
        this.lessonName = lessonName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Long getClusterId()
    {
        return clusterId;
    }

    public void setClusterId(Long clusterId)
    {
        this.clusterId = clusterId;
    }

    public List<TranslationDTO> getTranslations()
    {
        return translations;
    }

    public void setTranslations(List<TranslationDTO> translations)
    {
        this.translations = translations;
    }
}
