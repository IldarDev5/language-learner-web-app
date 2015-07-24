package ru.ildar.languagelearner.service.impl.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ildar.languagelearner.database.dao.ClusterRepository;
import ru.ildar.languagelearner.service.ClusterService;

import javax.transaction.Transactional;

@Service
@Transactional
public class ClusterServiceJpaImpl implements ClusterService
{
    @Autowired
    private ClusterRepository clusterRepository;
}
