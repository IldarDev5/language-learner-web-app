package ru.ildar.languagelearner.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.springframework.http.MediaType;
import ru.ildar.languagelearner.controller.dto.LanguagePairDTO;
import ru.ildar.languagelearner.database.domain.AppUser;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

public class ClusterControllerTest extends BaseControllerTest
{
    @Test
    public void testCreateClusterPage_ShouldReturnCreateView() throws Exception
    {
        String nickname = "Ildar";
        AppUser appUser = new AppUser();
        appUser.setFirstName(nickname);

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
}
