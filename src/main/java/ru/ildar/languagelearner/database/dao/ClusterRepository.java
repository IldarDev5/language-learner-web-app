package ru.ildar.languagelearner.database.dao;

import org.springframework.data.repository.CrudRepository;
import ru.ildar.languagelearner.database.domain.Cluster;

public interface ClusterRepository extends CrudRepository<Cluster, Long>
{
}
