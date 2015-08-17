package ru.ildar.languagelearner.exercise;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/** Grade of the current question user gave answer to. It must be filled in the controller
 * in AJAX-method and the grade must be consequently given to the exerciser. */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExerciserGrade
{
    private Long clusterId;
    private Double grade;

    public Long getClusterId()
    {
        return clusterId;
    }

    public void setClusterId(Long clusterId)
    {
        this.clusterId = clusterId;
    }

    public Double getGrade()
    {
        return grade;
    }

    public void setGrade(Double grade)
    {
        this.grade = grade;
    }
}
