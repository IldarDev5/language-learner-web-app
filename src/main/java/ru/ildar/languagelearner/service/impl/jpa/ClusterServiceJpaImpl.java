package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ildar.languagelearner.controller.dto.ClusterDTO;
import ru.ildar.languagelearner.database.dao.ClusterRepository;
import ru.ildar.languagelearner.database.dao.LanguageRepository;
import ru.ildar.languagelearner.database.domain.AppUser;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Language;
import ru.ildar.languagelearner.exception.ClusterAlreadyExistsException;
import ru.ildar.languagelearner.exception.LanguageNotFoundException;
import ru.ildar.languagelearner.exception.LanguagesAreEqualException;
import ru.ildar.languagelearner.service.AppUserService;
import ru.ildar.languagelearner.service.ClusterService;

import javax.transaction.Transactional;
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
    public long addCluster(ClusterDTO clusterDTO, String name) throws LanguagesAreEqualException,
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

        if(clusterRepository.findByLanguage1AndLanguage2(l1, l2) != null
                || clusterRepository.findByLanguage1AndLanguage2(l2, l1) != null)
        {
            throw new ClusterAlreadyExistsException();
        }

        AppUser appUser = appUserService.getUserByNickname(name);
        Cluster cluster = new Cluster();
        cluster.setAppUser(appUser);
        cluster.setLanguage1(l1);
        cluster.setLanguage2(l2);

        return clusterRepository.save(cluster).getClusterId();
    }

    @Override
    public List<Cluster> getClustersOfUser(String nickname)
    {
        return clusterRepository.findByAppUser_Nickname(nickname);
    }

    @Override
    public Cluster getClusterById(Long id)
    {
        return clusterRepository.findOne(id);
    }

    @Override
    public boolean checkClusterExistence(String lang1, String lang2)
    {
        Language l1 = languageRepository.findByDefaultName(lang1);
        Language l2 = languageRepository.findByDefaultName(lang2);

        return clusterRepository.findByLanguage1AndLanguage2(l1, l2) != null
                || clusterRepository.findByLanguage1AndLanguage2(l2, l1) != null;
    }
}
