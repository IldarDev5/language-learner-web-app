package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ildar.languagelearner.controller.dto.ClusterDTO;
import ru.ildar.languagelearner.controller.dto.LanguagePairDTO;
import ru.ildar.languagelearner.database.dao.ClusterRepository;
import ru.ildar.languagelearner.database.dao.LanguageRepository;
import ru.ildar.languagelearner.database.domain.AppUser;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Language;
import ru.ildar.languagelearner.exception.ClusterAlreadyExistsException;
import ru.ildar.languagelearner.exception.ClusterNotOfThisUserException;
import ru.ildar.languagelearner.exception.LanguageNotFoundException;
import ru.ildar.languagelearner.exception.LanguagesAreEqualException;
import ru.ildar.languagelearner.service.AppUserService;
import ru.ildar.languagelearner.service.ClusterService;

import java.util.List;

@Service
@Transactional
public class ClusterServiceJpaImpl implements ClusterService
{
    @Autowired
    private ClusterRepository clusterRepository;
    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private AppUserService appUserService;

    @Override
    public long addCluster(ClusterDTO clusterDTO, String nickname) throws LanguagesAreEqualException,
            LanguageNotFoundException, ClusterAlreadyExistsException
    {
        if(clusterDTO.getLanguage1().equals(clusterDTO.getLanguage2()))
        {
            throw new LanguagesAreEqualException();
        }

        Language l1 = languageRepository.findByDefaultName(clusterDTO.getLanguage1());
        if(l1 == null)
        {
            throw new LanguageNotFoundException(1);
        }

        Language l2 = languageRepository.findByDefaultName(clusterDTO.getLanguage2());
        if(l2 == null)
        {
            throw new LanguageNotFoundException(2);
        }

        if(clusterRepository.findByLanguagesAndUserNickname(l1, l2, nickname) != null)
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
    {
        Language l1 = languageRepository.findByDefaultName(lang1);
        Language l2 = languageRepository.findByDefaultName(lang2);

        return clusterRepository.findByLanguagesAndUserNickname(l1, l2, userNickname) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public LanguagePairDTO getNonExistentLanguagePair(String userNickname)
    {
        List<LanguagePairDTO> lst = languageRepository.findNonExistentLanguagePair();
        return lst.size() != 0 ? lst.get(0) : null;
    }

    @Override
    public Cluster checkClusterOwner(long clusterId, String nickname)
    {
        Cluster cluster = clusterRepository.findOne(clusterId);
        if(cluster == null || !cluster.getAppUser().getNickname().equals(nickname))
        {
            throw new ClusterNotOfThisUserException();
        }

        return cluster;
    }
}
