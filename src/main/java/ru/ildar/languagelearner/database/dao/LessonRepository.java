package ru.ildar.languagelearner.database.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Lesson;

import java.util.List;

public interface LessonRepository extends CrudRepository<Lesson, Long>
{
    List<Lesson> findByCluster(Cluster cluster);

    List<Lesson> findByCluster(Cluster cluster, Pageable pageable);

    @Query("select count(t) from Translation t where t.lesson.lessonId = ?1")
    Long findTranslationsCount(long lessonId);

    @Query("select l from Lesson l " +
            "where l.cluster.appUser.nickname = ?1 " +
            "order by l.lastTaken asc")
    Slice<Lesson> findLessonsNotTakenLongestTime(String username, Pageable pageable);

    @Query("select l from Lesson l where l.cluster.appUser.nickname = :username " +
            "and (lower(l.lessonName) like concat('%', :searchQuery, '%') " +
            "      or lower(l.description) like concat('%', :searchQuery, '%'))")
    Page<Lesson> searchLessonsOfUser(@Param("searchQuery") String searchQuery,
                                     @Param("username") String username, Pageable pageable);
}
