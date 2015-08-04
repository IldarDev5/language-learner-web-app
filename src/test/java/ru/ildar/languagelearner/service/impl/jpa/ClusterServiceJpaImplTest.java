package ru.ildar.languagelearner.service.impl.jpa;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import ru.ildar.languagelearner.controller.dto.ClusterDTO;
import ru.ildar.languagelearner.controller.dto.LanguagePairDTO;
import ru.ildar.languagelearner.database.domain.AppUser;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Language;
import ru.ildar.languagelearner.exception.ClusterAlreadyExistsException;
import ru.ildar.languagelearner.exception.ClusterNotOfThisUserException;
import ru.ildar.languagelearner.exception.LanguageNotFoundException;
import ru.ildar.languagelearner.exception.LanguagesAreEqualException;
import ru.ildar.languagelearner.service.BaseServiceTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class ClusterServiceJpaImplTest extends BaseServiceTest
{
    @Test(expected = LanguagesAreEqualException.class)
    public void testAddCluster_ShouldThrowException() throws Exception
    {
        ClusterDTO clusterDTO = new ClusterDTO();
        clusterDTO.setLanguage1("EN");
        clusterDTO.setLanguage2("EN");

        clusterService.addCluster(clusterDTO, "Ildar");
    }

    @Test(expected = LanguageNotFoundException.class)
    public void testAddCluster_FirstLanguageNotFound_ShouldThrowException()
            throws Exception
    {
        ClusterDTO clusterDTO = new ClusterDTO();
        clusterDTO.setLanguage1("EN");
        clusterDTO.setLanguage2("RU");

        doReturn(new Language()).when(languageRepository).findByDefaultName(clusterDTO.getLanguage2());

        clusterService.addCluster(clusterDTO, "Ildar");
    }

    @Test(expected = LanguageNotFoundException.class)
    public void testAddCluster_SecondLanguageNotFound_ShouldThrowException()
            throws Exception
    {
        ClusterDTO clusterDTO = new ClusterDTO();
        clusterDTO.setLanguage1("EN");
        clusterDTO.setLanguage2("RU");

        doReturn(new Language()).when(languageRepository).findByDefaultName(clusterDTO.getLanguage1());

        clusterService.addCluster(clusterDTO, "Ildar");
    }

    @Test(expected = ClusterAlreadyExistsException.class)
    public void testAddCluster_FoundExistingCluster_ShouldThrowException() throws Exception
    {
        String nickname = "Ildar";
        ClusterDTO clusterDTO = new ClusterDTO();
        clusterDTO.setLanguage1("EN");
        clusterDTO.setLanguage2("RU");

        Language l1 = new Language(clusterDTO.getLanguage1());
        Language l2 = new Language(clusterDTO.getLanguage2());

        doReturn(l1).when(languageRepository).findByDefaultName(clusterDTO.getLanguage1());
        doReturn(l2).when(languageRepository).findByDefaultName(clusterDTO.getLanguage2());
        doReturn(new Cluster()).when(clusterRepository).findByLanguagesAndUserNickname(l1, l2, nickname);

        clusterService.addCluster(clusterDTO, nickname);
    }

    @Test
    public void testAddCluster_ShouldAddCluster() throws Exception
    {
        String nickname = "Ildar";
        ClusterDTO clusterDTO = new ClusterDTO();
        clusterDTO.setLanguage1("EN");
        clusterDTO.setLanguage2("RU");

        Language l1 = new Language(clusterDTO.getLanguage1());
        Language l2 = new Language(clusterDTO.getLanguage2());
        AppUser appUser = new AppUser();

        doReturn(l1).when(languageRepository).findByDefaultName(clusterDTO.getLanguage1());
        doReturn(l2).when(languageRepository).findByDefaultName(clusterDTO.getLanguage2());
        doReturn(new Cluster()).when(clusterRepository).save(Matchers.any(Cluster.class));
        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);

        clusterService.addCluster(clusterDTO, nickname);

        ArgumentCaptor<Cluster> captor = ArgumentCaptor.forClass(Cluster.class);
        verify(clusterRepository).save(captor.capture());
        Cluster val = captor.getValue();

        assertThat(val.getLanguage1(), is(l1));
        assertThat(val.getLanguage2(), is(l2));
        assertThat(val.getAppUser(), is(appUser));

        verify(clusterRepository).findByLanguagesAndUserNickname(l1, l2, nickname);
        verify(languageRepository).findByDefaultName(clusterDTO.getLanguage1());
        verify(languageRepository).findByDefaultName(clusterDTO.getLanguage2());
        verify(clusterRepository).save(val);
        verify(appUserServiceMock).getUserByNickname(nickname);

        verifyNoMoreInteractions(repos);
    }

    @Test
    public void testGetClustersOfUser()
    {
        String nickname = "Ildar";
        doReturn(Arrays.asList(new Cluster(), new Cluster())).when(clusterRepository).findByAppUser_Nickname(nickname);
        List<Cluster> cls = clusterService.getClustersOfUser(nickname);
        assertThat(cls.size(), is(2));

        verify(clusterRepository).findByAppUser_Nickname(nickname);
        verifyNoMoreInteractions(repos);
    }

    @Test(expected = LanguageNotFoundException.class)
    public void testCheckClusterExistence_Language1NotFound_ShouldThrowException() throws Exception
    {
        String lang2 = "EN";
        doReturn(new Language()).when(languageRepository).findByDefaultName(lang2);

        clusterService.checkClusterExistence(null, lang2, "Ildar");
    }

    @Test(expected = LanguageNotFoundException.class)
    public void testCheckClusterExistence_Language2NotFound_ShouldThrowException() throws Exception
    {
        String lang1 = "EN";
        doReturn(new Language()).when(languageRepository).findByDefaultName(lang1);

        clusterService.checkClusterExistence(lang1, null, "Ildar");
    }

    @Test
    public void testCheckClusterExistence_EverythingIsOk() throws Exception
    {
        String nickname = "Ildar";
        String lang1 = "RU";
        String lang2 = "EN";

        Language l1 = new Language();
        Language l2 = new Language();

        doReturn(l1).when(languageRepository).findByDefaultName(lang1);
        doReturn(l2).when(languageRepository).findByDefaultName(lang2);
        doReturn(new Cluster()).when(clusterRepository).findByLanguagesAndUserNickname(l1, l2, nickname);

        boolean exists = clusterService.checkClusterExistence(lang1, lang2, nickname);
        assertThat(exists, is(true));

        verify(languageRepository).findByDefaultName(lang1);
        verify(languageRepository).findByDefaultName(lang2);
        verify(clusterRepository).findByLanguagesAndUserNickname(l1, l2, nickname);
        verifyNoMoreInteractions(repos);
    }

    @Test
    public void testGetNonExistentLanguagePair_NotFound_ShouldReturnNull()
    {
        String nickname = "Ildar";

        doReturn(new ArrayList<>()).when(languageRepository).findNonExistentLanguagePair(nickname);

        LanguagePairDTO dto = clusterService.getNonExistentLanguagePair(nickname);
        assertThat(dto, is(nullValue()));

        verify(languageRepository).findNonExistentLanguagePair(nickname);
        verifyNoMoreInteractions(repos);
    }

    @Test
    public void testGetNonExistentLanguagePair_SuchIsFound_ShouldReturnIt()
    {
        String nickname = "Ildar";
        String lang1 = "EN", lang2 = "RU";
        LanguagePairDTO dto = new LanguagePairDTO(lang1, lang2);

        doReturn(Arrays.asList(dto)).when(languageRepository).findNonExistentLanguagePair(nickname);

        LanguagePairDTO ret = clusterService.getNonExistentLanguagePair(nickname);
        assertThat(ret, is(dto));
        assertThat(dto.getLanguage1(), is(lang1));
        assertThat(dto.getLanguage2(), is(lang2));

        verify(languageRepository).findNonExistentLanguagePair(nickname);
        verifyNoMoreInteractions(repos);
    }

    @Test(expected = ClusterNotOfThisUserException.class)
    public void testCheckClusterOwner_ClusterIsNull_ShouldThrowException()
    {
        clusterService.checkClusterOwner(1, "Ildar");
    }

    @Test(expected = ClusterNotOfThisUserException.class)
    public void testCheckClusterOwner_ClusterIsOfAnotherUser_ShouldThrowException()
    {
        String nickname = "Ildar";
        String anotherNick = "James";
        long id = 1;

        Cluster cls = new Cluster();
        AppUser user = new AppUser();
        user.setNickname(anotherNick);
        cls.setAppUser(user);

        doReturn(cls).when(clusterRepository).findOne(id);

        clusterService.checkClusterOwner(1, nickname);
    }

    @Test
    public void testCheckClusterOwner_EverythingIsOk()
    {
        String nickname = "Ildar";
        long id = 1;

        Cluster cls = new Cluster();
        AppUser user = new AppUser();
        user.setNickname(nickname);
        cls.setAppUser(user);

        doReturn(cls).when(clusterRepository).findOne(id);

        Cluster cluster = clusterService.checkClusterOwner(1, nickname);
        assertThat(cluster, is(cls));

        verify(clusterRepository).findOne(id);
        verifyNoMoreInteractions(repos);
    }
}
