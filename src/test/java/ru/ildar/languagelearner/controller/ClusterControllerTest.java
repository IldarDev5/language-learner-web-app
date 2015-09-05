package ru.ildar.languagelearner.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import ru.ildar.languagelearner.controller.dto.ClusterDTO;
import ru.ildar.languagelearner.controller.dto.LanguagePairDTO;
import ru.ildar.languagelearner.database.domain.AppUser;
import ru.ildar.languagelearner.database.domain.Cluster;
import ru.ildar.languagelearner.database.domain.Language;
import ru.ildar.languagelearner.exception.ClusterAlreadyExistsException;
import ru.ildar.languagelearner.exception.ClusterNotOfThisUserException;
import ru.ildar.languagelearner.exception.LanguageNotFoundException;
import ru.ildar.languagelearner.exception.LanguagesAreEqualException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

public class ClusterControllerTest extends BaseControllerTest
{
    private String nickname = "Ildar";
    private AppUser appUser = new AppUser();

    Language lang1 = new Language("English");
    Language lang2 = new Language("Russian");

    @Before
    public void initUser()
    {
        appUser.setNickname(nickname);
        appUser.setFirstName(nickname);
    }

    @Test
    public void testCreateClusterPage_ShouldReturnCreateView() throws Exception
    {
        doReturn(new LanguagePairDTO()).when(clusterServiceMock).getNonExistentLanguagePair(nickname);
        doReturn(Arrays.<String>asList()).when(languageServiceMock).getLanguagesAsStrings();
        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);

        mockMvc.perform(get("/cluster/create").with(user(nickname)))
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/create"))
                .andExpect(model().attribute("langPair", is(notNullValue())))
                .andExpect(model().attribute("languages", is(notNullValue())))
                .andExpect(model().attribute("cluster", is(notNullValue())));

        verify(clusterServiceMock).getNonExistentLanguagePair(nickname);
        verify(languageServiceMock).getLanguagesAsStrings();
        verify(appUserServiceMock).getUserByNickname(nickname);
        verify(lessonServiceMock).getLessonsNotTakenLongestTime(Mockito.any(Integer.class), Mockito.anyString());
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testCreateClusterPost_HasFieldErrors_ShouldReturnCreateView() throws Exception
    {
        String lang1 = "a";
        String lang2 = null;

        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);
        doReturn(new LanguagePairDTO()).when(clusterServiceMock).getNonExistentLanguagePair(nickname);

        mockMvc.perform(post("/cluster/create").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("language1", lang1).param("language2", lang2).with(user(nickname)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/create"))
                .andExpect(model().attributeHasFieldErrors("cluster", "language1", "language2"));

        verify(appUserServiceMock).getUserByNickname(nickname);
        verify(clusterServiceMock).getNonExistentLanguagePair(nickname);
        verify(languageServiceMock).getLanguagesAsStrings();
        verify(lessonServiceMock).getLessonsNotTakenLongestTime(Mockito.any(Integer.class), Mockito.anyString());
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testCreateClusterPost_LanguagesAreEqual_ShouldReturnCreateView() throws Exception
    {
        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);
        doReturn(new LanguagePairDTO()).when(clusterServiceMock).getNonExistentLanguagePair(nickname);
        doThrow(new LanguagesAreEqualException()).when(clusterServiceMock)
                .addCluster(Mockito.any(ClusterDTO.class), anyString());

        mockMvc.perform(post("/cluster/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("language1", lang1.getDefaultName())
                .param("language2", lang2.getDefaultName())
                .with(user(nickname)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/create"))
                .andExpect(model().attributeHasFieldErrors("cluster", "language1"));

        verify(appUserServiceMock).getUserByNickname(nickname);
        verify(clusterServiceMock).getNonExistentLanguagePair(nickname);
        verify(languageServiceMock).getLanguagesAsStrings();
        verify(clusterServiceMock).addCluster(Mockito.any(ClusterDTO.class), anyString());
        verify(lessonServiceMock).getLessonsNotTakenLongestTime(Mockito.any(Integer.class), Mockito.anyString());
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testCreateClusterPost_Language2NotFound_ShouldReturnCreateView() throws Exception
    {
        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);
        doReturn(new LanguagePairDTO()).when(clusterServiceMock).getNonExistentLanguagePair(nickname);
        doThrow(new LanguageNotFoundException(2)).when(clusterServiceMock)
                .addCluster(Mockito.any(ClusterDTO.class), anyString());

        mockMvc.perform(post("/cluster/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("language1", lang1.getDefaultName())
                        .param("language2", lang2.getDefaultName())
                        .with(user(nickname))
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/create"))
                .andExpect(model().attributeHasFieldErrors("cluster", "language2"));

        verify(appUserServiceMock).getUserByNickname(nickname);
        verify(clusterServiceMock).getNonExistentLanguagePair(nickname);
        verify(languageServiceMock).getLanguagesAsStrings();
        verify(clusterServiceMock).addCluster(Mockito.any(ClusterDTO.class), anyString());
        verify(lessonServiceMock).getLessonsNotTakenLongestTime(Mockito.any(Integer.class), Mockito.anyString());
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testCreateClusterPost_ClusterAlreadyExists_ShouldReturnCreateView() throws Exception
    {
        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);
        doReturn(new LanguagePairDTO()).when(clusterServiceMock).getNonExistentLanguagePair(nickname);
        doThrow(new ClusterAlreadyExistsException()).when(clusterServiceMock)
                .addCluster(Mockito.any(ClusterDTO.class), anyString());

        mockMvc.perform(post("/cluster/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("language1", lang1.getDefaultName())
                        .param("language2", lang2.getDefaultName())
                        .with(user(nickname))
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/create"))
                .andExpect(model().attributeHasErrors("cluster"));

        verify(appUserServiceMock).getUserByNickname(nickname);
        verify(clusterServiceMock).getNonExistentLanguagePair(nickname);
        verify(languageServiceMock).getLanguagesAsStrings();
        verify(clusterServiceMock).addCluster(Mockito.any(ClusterDTO.class), anyString());
        verify(lessonServiceMock).getLessonsNotTakenLongestTime(Mockito.any(Integer.class), Mockito.anyString());
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testCreateClusterPost_EverythingIsOk_ShouldRedirectToViewCluster() throws Exception
    {
        long clusterId = 1;

        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);
        doReturn(clusterId).when(clusterServiceMock)
                .addCluster(Mockito.any(ClusterDTO.class), anyString());

        mockMvc.perform(post("/cluster/create").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("language1", lang1.getDefaultName()).param("language2", lang2.getDefaultName()).with(user(nickname)).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("viewCluster/" + clusterId + "?created=true"));

        ArgumentCaptor<ClusterDTO> captor = ArgumentCaptor.forClass(ClusterDTO.class);
        ArgumentCaptor<String> strCaptor = ArgumentCaptor.forClass(String.class);
        verify(clusterServiceMock).addCluster(captor.capture(), strCaptor.capture());
        ClusterDTO clusterDTO = captor.getValue();
        assertThat(clusterDTO.getLanguage1(), is(lang1.getDefaultName()));
        assertThat(clusterDTO.getLanguage2(), is(lang2.getDefaultName()));
        assertThat(strCaptor.getValue(), is(nickname));

        verify(appUserServiceMock).getUserByNickname(nickname);
        verify(lessonServiceMock).getLessonsNotTakenLongestTime(Mockito.any(Integer.class), Mockito.anyString());
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testViewClusters() throws Exception
    {
        List<Cluster> clusters = Arrays.asList(cluster(lang1, lang2), cluster(lang1, lang2));

        doReturn(clusters).when(clusterServiceMock).getClustersOfUser(nickname);
        doReturn(new AppUser(nickname)).when(appUserServiceMock).getUserByNickname(nickname);

        mockMvc.perform(get("/cluster/viewClusters")
                    .with(user(nickname).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/viewClusters"))
                .andExpect(model().attributeExists("clusters"))
                .andExpect(model().attribute("clusters", contains(clusters.get(0), clusters.get(1))));

        verify(clusterServiceMock).getClustersOfUser(nickname);
        verify(appUserServiceMock).getUserByNickname(nickname);
        verify(lessonServiceMock).getLessonsNotTakenLongestTime(Mockito.any(Integer.class), Mockito.anyString());
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testViewCluster_CreatedParamIsNotPresent() throws Exception
    {
        Cluster toRet = cluster(lang1, lang2);
        long clusterId = 1l;

        doReturn(toRet).when(clusterServiceMock).checkClusterOwner(clusterId, nickname);
        doReturn(new AppUser(this.nickname)).when(appUserServiceMock).getUserByNickname(this.nickname);

        mockMvc.perform(get("/cluster/viewCluster/" + clusterId)
                    .with(user(nickname).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/viewCluster"))
                .andExpect(model().attributeDoesNotExist("created"))
                .andExpect(model().attributeExists("cls"))
                .andExpect(model().attribute("cls", is(toRet)));

        verify(clusterServiceMock).checkClusterOwner(clusterId, nickname);
        verify(appUserServiceMock).getUserByNickname(this.nickname);
        verify(lessonServiceMock).getLessonsNotTakenLongestTime(Mockito.any(Integer.class), Mockito.anyString());
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testViewCluster_CreatedParamIsPresent() throws Exception
    {
        Cluster toRet = cluster(lang1, lang2);
        long clusterId = 1l;

        doReturn(toRet).when(clusterServiceMock).checkClusterOwner(clusterId, nickname);
        doReturn(new AppUser(this.nickname)).when(appUserServiceMock).getUserByNickname(this.nickname);

        mockMvc.perform(get("/cluster/viewCluster/" + clusterId + "?created=true")
                .with(user(nickname).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/viewCluster"))
                .andExpect(model().attributeExists("cls", "created"))
                .andExpect(model().attribute("cls", is(toRet)))
                .andExpect(model().attribute("created", is(true)));

        verify(clusterServiceMock).checkClusterOwner(clusterId, nickname);
        verify(appUserServiceMock).getUserByNickname(this.nickname);
        verify(lessonServiceMock).getLessonsNotTakenLongestTime(Mockito.any(Integer.class), Mockito.anyString());
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testViewCluster_ClusterNotOfThisUser_ShouldReturnForbidden() throws Exception
    {
        long clusterId = 1l;

        doThrow(new ClusterNotOfThisUserException()).when(clusterServiceMock).checkClusterOwner(clusterId, nickname);
        doReturn(new AppUser(nickname)).when(appUserServiceMock).getUserByNickname(nickname);

        mockMvc.perform(get("/cluster/viewCluster/" + clusterId).with(user(nickname).roles("USER")))
                .andExpect(status().isForbidden());

        verify(clusterServiceMock).checkClusterOwner(clusterId, nickname);
        verify(appUserServiceMock).getUserByNickname(this.nickname);
        verify(lessonServiceMock).getLessonsNotTakenLongestTime(Mockito.any(Integer.class), Mockito.anyString());
        verifyNoMoreInteractions(services);
    }
    
    private Cluster cluster(Language lang1, Language lang2)
    {
        Cluster cluster = new Cluster();
        cluster.setLanguage1(lang1);
        cluster.setLanguage2(lang2);
        return cluster;
    }
}
