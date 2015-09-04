package ru.ildar.languagelearner.controller.dto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LessonDTO
{
    @NotNull
    @Min(1)
    private Long clusterId;
    @NotNull
    @Size(min = 3, max = 100)
    private String lessonName;
    @Size(min = 3, max = 300)
    private String description;

    @Valid
    private List<TranslationDTO> translations = new ArrayList<>();

    private Long lessonId;
    private double averageGrade;
    private Date addDate;
    private int translationsCount;
    private String addDateStr;

    public LessonDTO() { }
    public LessonDTO(Long lessonId, String lessonName, String description, double averageGrade, String addDateStr, Date addDate, int translationsCount)
    {
        this.lessonName = lessonName;
        this.description = description;
        this.lessonId = lessonId;
        this.averageGrade = averageGrade;
        this.addDateStr = addDateStr;
        this.addDate = addDate;
        this.translationsCount = translationsCount;
    }

    public Long getLessonId()
    {
        return lessonId;
    }

    public void setLessonId(Long lessonId)
    {
        this.lessonId = lessonId;
    }

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

    public double averageGrade()
    {
        return averageGrade;
    }

    public double getAverageGrade()
    {
        return averageGrade;
    }

    public void setAverageGrade(double averageGrade)
    {
        this.averageGrade = averageGrade;
    }

    public Date getAddDate()
    {
        return addDate;
    }

    public void setAddDate(Date addDate)
    {
        this.addDate = addDate;
    }

    public int getTranslationsCount()
    {
        return translationsCount;
    }

    public void setTranslationsCount(int translationsCount)
    {
        this.translationsCount = translationsCount;
    }

    public String getAddDateStr()
    {
        return addDateStr;
    }

    public void setAddDateStr(String addDateStr)
    {
        this.addDateStr = addDateStr;
    }
}
