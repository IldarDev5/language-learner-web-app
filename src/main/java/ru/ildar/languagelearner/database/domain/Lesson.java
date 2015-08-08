package ru.ildar.languagelearner.database.domain;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/** Lesson in a cluster - contains pairs "sentence-translation" in cluster defined languages. */
@Entity
public class Lesson implements Serializable
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

    /** How many times this lesson was taken as exercise */
    @Column(nullable = false)
    private long timesLessonTaken;

    /** The sum of all grades received by this lesson exercises */
    @Column(nullable = false)
    private long sumGrade;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lesson")
    private List<Translation> translations;

    @Formula("(select count(*) from translation t where t.lesson_id = lesson_id)")
    private int translationsCount;

    public Lesson() { }
    public Lesson(Cluster cluster)
    {
        this.cluster = cluster;
    }

    @PrePersist
    public void prePersist()
    {
        addDate = new Date();
        timesLessonTaken = 0;
        sumGrade = 0;
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

    public List<Translation> getTranslations()
    {
        return translations;
    }

    public void setTranslations(List<Translation> translations)
    {
        this.translations = translations;
    }

    public int getTranslationsCount()
    {
        return translationsCount;
    }

    public long getTimesLessonTaken()
    {
        return timesLessonTaken;
    }

    public void setTimesLessonTaken(long timesLessonTaken)
    {
        this.timesLessonTaken = timesLessonTaken;
    }

    public long getSumGrade()
    {
        return sumGrade;
    }

    public void setSumGrade(long sumGrade)
    {
        this.sumGrade = sumGrade;
    }
}
