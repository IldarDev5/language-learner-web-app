package ru.ildar.languagelearner.database.domain;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"lesson_id", "sentence1", "sentence2"})
})
public class Translation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long translationId;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "sentence1_language_name", nullable = false)
    private Language sentence1Language;

    @Column(length = 500, nullable = false)
    private String sentence1;

    @ManyToOne
    @JoinColumn(name = "sentence2_language_name", nullable = false)
    private Language sentence2Language;

    @Column(length = 500, nullable = false)
    private String sentence2;


    public Long getTranslationId()
    {
        return translationId;
    }

    public void setTranslationId(Long translationId)
    {
        this.translationId = translationId;
    }

    public Lesson getLesson()
    {
        return lesson;
    }

    public void setLesson(Lesson lesson)
    {
        this.lesson = lesson;
    }

    public Language getSentence1Language()
    {
        return sentence1Language;
    }

    public void setSentence1Language(Language sentence1Language)
    {
        this.sentence1Language = sentence1Language;
    }

    public String getSentence1()
    {
        return sentence1;
    }

    public void setSentence1(String sentence1)
    {
        this.sentence1 = sentence1;
    }

    public Language getSentence2Language()
    {
        return sentence2Language;
    }

    public void setSentence2Language(Language sentence2Language)
    {
        this.sentence2Language = sentence2Language;
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
