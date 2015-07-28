package ru.ildar.languagelearner.service;

import ru.ildar.languagelearner.controller.dto.ClusterDTO;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.exception.ClusterAlreadyExistsException;
import ru.ildar.languagelearner.exception.LanguageNotFoundException;
import ru.ildar.languagelearner.exception.LanguagesAreEqualException;

import java.util.List;

public interface ClusterService
{
    long addCluster(ClusterDTO clusterDTO, String name) throws LanguagesAreEqualException,
            LanguageNotFoundException, ClusterAlreadyExistsException;

    List<Cluster> getClustersOfUser(String nickname);

    Cluster getClusterById(Long id);
}
