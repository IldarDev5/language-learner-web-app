package ru.ildar.languagelearner.exception;

public class LessonNotFoundException extends RuntimeException
{
    private Long lessonId;

    public LessonNotFoundException() { }
    public LessonNotFoundException(Long lessonId)
    {
        this.lessonId = lessonId;
    }

    public Long getLessonId()
    {
        return lessonId;
    }
}
