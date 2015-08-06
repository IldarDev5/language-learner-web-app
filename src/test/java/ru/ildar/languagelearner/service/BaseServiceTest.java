package ru.ildar.languagelearner.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.ildar.languagelearner.database.dao.*;
import ru.ildar.languagelearner.service.impl.jpa.AppUserServiceJpaImpl;
import ru.ildar.languagelearner.service.impl.jpa.ClusterServiceJpaImpl;

/** Base class for all service test classes of this application */
@RunWith(MockitoJUnitRunner.class)
public abstract class BaseServiceTest
{
    @Mock
    protected AppUserRepository appUserRepository;
    @Mock
    protected ClusterRepository clusterRepository;
    @Mock
    protected LanguageRepository languageRepository;
    @Mock
    protected LessonRepository lessonRepository;
    @Mock
    protected TranslationRepository translationRepository;
    @Mock
    protected AppUserService appUserServiceMock;

    protected AppUserServiceJpaImpl appUserService;
    protected ClusterServiceJpaImpl clusterService;

    protected Object[] repos;

    @Before
    public void initServices()
    {
        repos = new Object[]{ appUserRepository, clusterRepository, languageRepository,
                lessonRepository, translationRepository, appUserServiceMock };

        appUserService = new AppUserServiceJpaImpl(appUserRepository);
        clusterService = new ClusterServiceJpaImpl(appUserServiceMock, languageRepository,
                clusterRepository, lessonRepository);
    }
}
