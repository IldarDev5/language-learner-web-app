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
import ru.ildar.languagelearner.exception.ClusterAlreadyExistsException;
import ru.ildar.languagelearner.exception.LanguageNotFoundException;
import ru.ildar.languagelearner.exception.LanguagesAreEqualException;

import java.util.Arrays;

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
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testCreateClusterPost_HasFieldErrors_ShouldReturnCreateView() throws Exception
    {
        String lang1 = "a";
        String lang2 = null;

        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);
        doReturn(new LanguagePairDTO()).when(clusterServiceMock).getNonExistentLanguagePair(nickname);

        mockMvc.perform(post("/cluster/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("language1", lang1)
                .param("language2", lang2)
                .with(user(nickname))
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/create"))
                .andExpect(model().attributeHasFieldErrors("cluster", "language1", "language2"));

        verify(appUserServiceMock).getUserByNickname(nickname);
        verify(clusterServiceMock).getNonExistentLanguagePair(nickname);
        verify(languageServiceMock).getLanguagesAsStrings();
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testCreateClusterPost_LanguagesAreEqual_ShouldReturnCreateView() throws Exception
    {
        String lang1 = "English";
        String lang2 = "Russian";

        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);
        doReturn(new LanguagePairDTO()).when(clusterServiceMock).getNonExistentLanguagePair(nickname);
        doThrow(new LanguagesAreEqualException()).when(clusterServiceMock)
                .addCluster(Mockito.any(ClusterDTO.class), anyString());

        mockMvc.perform(post("/cluster/create").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("language1", lang1).param("language2", lang2).with(user(nickname)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/create"))
                .andExpect(model().attributeHasFieldErrors("cluster", "language1"));

        verify(appUserServiceMock).getUserByNickname(nickname);
        verify(clusterServiceMock).getNonExistentLanguagePair(nickname);
        verify(languageServiceMock).getLanguagesAsStrings();
        verify(clusterServiceMock).addCluster(Mockito.any(ClusterDTO.class), anyString());
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testCreateClusterPost_Language2NotFound_ShouldReturnCreateView() throws Exception
    {
        String lang1 = "English";
        String lang2 = "Russian";

        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);
        doReturn(new LanguagePairDTO()).when(clusterServiceMock).getNonExistentLanguagePair(nickname);
        doThrow(new LanguageNotFoundException(2)).when(clusterServiceMock)
                .addCluster(Mockito.any(ClusterDTO.class), anyString());

        mockMvc.perform(post("/cluster/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("language1", lang1)
                        .param("language2", lang2)
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
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testCreateClusterPost_ClusterAlreadyExists_ShouldReturnCreateView() throws Exception
    {
        String lang1 = "English";
        String lang2 = "Russian";

        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);
        doReturn(new LanguagePairDTO()).when(clusterServiceMock).getNonExistentLanguagePair(nickname);
        doThrow(new ClusterAlreadyExistsException()).when(clusterServiceMock)
                .addCluster(Mockito.any(ClusterDTO.class), anyString());

        mockMvc.perform(post("/cluster/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("language1", lang1)
                        .param("language2", lang2)
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
        verifyNoMoreInteractions(services);
    }

    @Test
    public void testCreateClusterPost_EverythingIsOk_ShouldRedirectToViewCluster() throws Exception
    {
        String lang1 = "English";
        String lang2 = "Russian";
        long clusterId = 1;

        doReturn(appUser).when(appUserServiceMock).getUserByNickname(nickname);
        doReturn(clusterId).when(clusterServiceMock)
                .addCluster(Mockito.any(ClusterDTO.class), anyString());

        mockMvc.perform(post("/cluster/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("language1", lang1)
                        .param("language2", lang2)
                        .with(user(nickname))
                        .with(csrf())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("viewCluster/" + clusterId + "?created=true"));

        ArgumentCaptor<ClusterDTO> captor = ArgumentCaptor.forClass(ClusterDTO.class);
        ArgumentCaptor<String> strCaptor = ArgumentCaptor.forClass(String.class);
        verify(clusterServiceMock).addCluster(captor.capture(), strCaptor.capture());
        ClusterDTO clusterDTO = captor.getValue();
        assertThat(clusterDTO.getLanguage1(), is(lang1));
        assertThat(clusterDTO.getLanguage2(), is(lang2));
        assertThat(strCaptor.getValue(), is(nickname));

        verify(appUserServiceMock).getUserByNickname(nickname);
        verifyNoMoreInteractions(services);
    }
}
