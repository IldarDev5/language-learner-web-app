package ru.ildar.languagelearner.database.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Language;

import java.util.List;

public interface ClusterRepository extends CrudRepository<Cluster, Long>
{
    @Query("select c from Cluster c where c.appUser.nickname = ?3 " +
            "and (c.language1 = ?1 and c.language2 = ?2 or c.language1 = ?2 and c.language2 = ?1)")
    Cluster findByLanguagesAndUserNickname(Language l1, Language l2, String nickname);

    List<Cluster> findByAppUser_Nickname(String nickname);
}
