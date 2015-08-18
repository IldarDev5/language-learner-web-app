package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ildar.languagelearner.controller.dto.ClusterDTO;
import ru.ildar.languagelearner.controller.dto.LanguagePairDTO;
import ru.ildar.languagelearner.database.dao.ClusterRepository;
import ru.ildar.languagelearner.database.dao.LanguageRepository;
import ru.ildar.languagelearner.database.dao.LessonRepository;
import ru.ildar.languagelearner.database.domain.AppUser;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Language;
import ru.ildar.languagelearner.database.domain.Lesson;
import ru.ildar.languagelearner.exception.*;
import ru.ildar.languagelearner.exercise.Exerciser;
import ru.ildar.languagelearner.service.AppUserService;
import ru.ildar.languagelearner.service.ClusterService;

import java.util.List;

@Service("clusterService")
@Transactional
public class ClusterServiceJpaImpl implements ClusterService
{
    private ClusterRepository clusterRepository;
    private LanguageRepository languageRepository;
    private AppUserService appUserService;
    private LessonRepository lessonRepository;

    @Autowired
    public ClusterServiceJpaImpl(AppUserService appUserService, LanguageRepository languageRepository,
                                 ClusterRepository clusterRepository, LessonRepository lessonRepository)
    {
        this.appUserService = appUserService;
        this.languageRepository = languageRepository;
        this.clusterRepository = clusterRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Long addCluster(ClusterDTO clusterDTO, String nickname) throws LanguagesAreEqualException,
            LanguageNotFoundException, ClusterAlreadyExistsException
    {
        if(clusterDTO.getLanguage1().equals(clusterDTO.getLanguage2()))
            //Languages of a cluster mustn't be equal
        {
            throw new LanguagesAreEqualException();
        }

        Language l1 = languageRepository.findByDefaultName(clusterDTO.getLanguage1());
        if(l1 == null)
            //The first language is not found in the database
        {
            throw new LanguageNotFoundException(1);
        }

        Language l2 = languageRepository.findByDefaultName(clusterDTO.getLanguage2());
        if(l2 == null)
            //The second language is not found in the database
        {
            throw new LanguageNotFoundException(2);
        }

        if(clusterRepository.findByLanguagesAndUserNickname(l1, l2, nickname) != null)
            //If there's already a cluster with such language pair
        {
            throw new ClusterAlreadyExistsException();
        }

        AppUser appUser = appUserService.getUserByNickname(nickname);
        Cluster cluster = new Cluster();
        cluster.setAppUser(appUser);
        cluster.setLanguage1(l1);
        cluster.setLanguage2(l2);

        return clusterRepository.save(cluster).getClusterId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cluster> getClustersOfUser(String nickname)
    {
        return clusterRepository.findByAppUser_Nickname(nickname);
    }

    @Override
    @Transactional(readOnly = true)
    public Cluster getClusterById(Long id)
    {
        return clusterRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkClusterExistence(String lang1, String lang2, String userNickname)
            throws LanguageNotFoundException
    {
        Language l1 = languageRepository.findByDefaultName(lang1);
        Language l2 = languageRepository.findByDefaultName(lang2);

        if(l1 == null)
        {
            throw new LanguageNotFoundException(1);
        }
        if(l2 == null)
        {
            throw new LanguageNotFoundException(2);
        }

        return clusterRepository.findByLanguagesAndUserNickname(l1, l2, userNickname) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public LanguagePairDTO getNonExistentLanguagePair(String userNickname)
    {
        List<LanguagePairDTO> lst = languageRepository.findNonExistentLanguagePair(userNickname);
        return lst.size() != 0 ? lst.get(0) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public Cluster checkClusterOwner(long clusterId, String nickname)
    {
        Cluster cluster = clusterRepository.findOne(clusterId);
        if(cluster == null || !cluster.getAppUser().getNickname().equals(nickname))
            //Check if the cluster exists and belongs to this user
        {
            throw new ClusterNotOfThisUserException();
        }

        return cluster;
    }

    @Override
    public void setLesson(Exerciser exerciser, Long lessonId, String userNickname)
    {
        Lesson lesson = lessonRepository.findOne(lessonId);
        Cluster cluster = lesson.getCluster();
        if(!cluster.getAppUser().getNickname().equals(userNickname))
            //Check if the lesson belongs to this user
        {
            throw new LessonNotOfThisUserException();
        }

        exerciser.setLesson(lesson);
    }
}
