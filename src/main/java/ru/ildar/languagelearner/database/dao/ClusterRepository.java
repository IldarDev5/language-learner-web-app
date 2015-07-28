package ru.ildar.languagelearner.database.dao;

import org.springframework.data.repository.CrudRepository;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Language;

import java.util.List;

public interface ClusterRepository extends CrudRepository<Cluster, Long>
{
    Cluster findByLanguage1AndLanguage2(Language l1, Language l2);

    List<Cluster> findByAppUser_Nickname(String nickname);
}
