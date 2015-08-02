package ru.ildar.languagelearner.database.domain;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

/** Lesson in a cluster - contains pairs "sentence-translation" in cluster defined languages. */
@Entity
public class Lesson
{
    /** Lesson's primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    /** Header of the lesson */
    @Column(length = 100, nullable = false)
    private String lessonName;

    /** Short description of the lesson */
    @Column(length = 300)
    private String description;

    /** Lessons cluster this lesson pertains to */
    @ManyToOne
    @JoinColumn(name = "cluster_id", nullable = false)
    private Cluster cluster;

    /** Date of this lesson creation */
    @Temporal(TemporalType.TIMESTAMP)
    private Date addDate;

    /** Average grade received by the user on all trainings involving this lesson */
    @Column(nullable = false)
    private double averageGrade;

    @Formula("(select count(*) from translation t where t.lesson_id = lesson_id)")
    private int translationsCount;

    @PrePersist
    public void prePersist()
    {
        addDate = new Date();
        averageGrade = 0.0;
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

    public Cluster getCluster()
    {
        return cluster;
    }

    public void setCluster(Cluster cluster)
    {
        this.cluster = cluster;
    }

    public Date getAddDate()
    {
        return addDate;
    }

    public void setAddDate(Date addDate)
    {
        this.addDate = addDate;
    }

    public double getAverageGrade()
    {
        return averageGrade;
    }

    public void setAverageGrade(double averageGrade)
    {
        this.averageGrade = averageGrade;
    }

    public int getTranslationsCount()
    {
        return translationsCount;
    }
}
