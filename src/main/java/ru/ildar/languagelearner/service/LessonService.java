package ru.ildar.languagelearner.service;

import ru.ildar.languagelearner.controller.dto.LessonDTO;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.exception.*;

import java.util.List;

public interface LessonService
{
    /** Returns all lessons of this cluster */
    List<Lesson> getLessons(Cluster cluster);

    /**
     * Get lessons of the specified cluster using pagination
     * @param cluster Cluster lessons of which to get
     * @param page 1-based page number
     */
    List<Lesson> getLessonsForPage(Cluster cluster, int page);

    /** Returns total cound of pages containing lessons,
     * number of lessons per page is set in service implementation */
    int totalLessonPages();

    /**
     * Deletes the lesson. If the lesson is not found, does nothing.
     * @param lesson the lesson to delete
     * @param nickname Nickname of the user who performs the operation
     * @throws LessonNotOfThisUserException If the user specified by this nickname
     * is not the owner of this lesson
     */
    void deleteLesson(Lesson lesson, String nickname);

    /**
     * Returns the lesson specified by this ID
     * @param lessonId ID of the lesson to find
     */
    Lesson getLessonById(long lessonId);

    /**
     * Creates new lesson as part of this cluster
     * @param cluster Cluster which owns the new lesson
     * @param lessonDTO DTO object containing lesson data
     * @return Newly created Lesson object from this DTO
     */
    Lesson addLesson(Cluster cluster, LessonDTO lessonDTO);

    /**
     * Updates the sum exercise grade value of this lesson by adding this new value.
     * The method checks if the lesson belongs to this user.
     * @param lessonId ID of the lesson whose grade field to update
     * @param grade Grade value
     * @param nickname Nickname of the user
     * @throws LessonNotOfThisUserException Lesson does not belong to this user
     */
    void addTestGrade(long lessonId, double grade, String nickname);
}
