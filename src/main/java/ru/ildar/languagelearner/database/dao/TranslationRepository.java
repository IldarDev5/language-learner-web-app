package ru.ildar.languagelearner.database.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.ildar.languagelearner.database.domain.Translation;

import java.util.List;

public interface TranslationRepository extends CrudRepository<Translation, Long>
{
    List<Translation> findByLesson_LessonId(long lessonId);

    @Query("select tr from Translation tr where tr.lesson.cluster.appUser.nickname = :username " +
            "and (lower(tr.sentence1) like concat('%', :searchQuery, '%') " +
                    "or lower(tr.sentence2) like concat('%', :searchQuery, '%'))")
    Page<Translation> searchTranslationPairs(@Param("searchQuery")String searchQuery,
                                             @Param("username")String username, Pageable pageable);
}
