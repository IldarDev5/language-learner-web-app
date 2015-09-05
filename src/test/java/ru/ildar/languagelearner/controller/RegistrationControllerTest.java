package ru.ildar.languagelearner.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import ru.ildar.languagelearner.controller.dto.UserDTO;
import ru.ildar.languagelearner.exception.EmailAlreadyExistsException;
import ru.ildar.languagelearner.exception.NicknameAlreadyExistsException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegistrationControllerTest extends BaseControllerTest
{
    @Test
    public void testRegistrationPage_ShouldReturnRegistrationView() throws Exception
    {
        mockMvc.perform(get("/auth/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registration"))
                .andExpect(model().attribute("user", is(notNullValue())));

        verifyZeroInteractions(appUserServiceMock, userDetailsServiceMock);
    }

    @Test
    public void testRegisterUser_HasFieldErrors_ShouldReturnRegistrationView() throws Exception
    {
        String nickname = "a";
        String password = null;
        String repeatPassword = null;
        String email = "notEmail";

        mockMvc.perform(post("/auth/registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nickname", nickname)
                .param("password", password)
                .param("repeatPassword", repeatPassword)
                .param("email", email)
                .with(csrf())

        )
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registration"))
                .andExpect(model().attributeHasFieldErrors("user", "nickname",
                        "password", "repeatPassword", "email"))
                .andExpect(model().attribute("user", allOf(hasProperty("nickname", is(nickname)), hasProperty("password", is(password)), hasProperty("repeatPassword", is(repeatPassword)), hasProperty("email", is(email)))));

        verifyZeroInteractions(appUserServiceMock, userDetailsServiceMock);
    }

    @Test
    public void testRegisterUser_PasswordsNotMatch_ShouldReturnRegistrationView() throws Exception
    {
        String nickname = "John";
        String password = "johnjohn";
        String repeatPassword = "johnjohnson";
        String email = "email@gmail.com";

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nickname", nickname)
                        .param("password", password)
                        .param("repeatPassword", repeatPassword)
                        .param("email", email)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registration"))
                .andExpect(model().attributeHasFieldErrors("user", "password"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("nickname", is(nickname)),
                        hasProperty("password", is(password)),
                        hasProperty("repeatPassword", is(repeatPassword)),
                        hasProperty("email", is(email))
                )));

        verifyZeroInteractions(appUserServiceMock, userDetailsServiceMock);
    }

    @Test
    public void testRegisterUser_NicknameAlreadyExists_ShouldReturnRegistrationView() throws Exception
    {
        String nickname = "John";
        String password = "johnjohn";
        String repeatPassword = "johnjohn";
        String email = "email@gmail.com";

        doThrow(new NicknameAlreadyExistsException())
                .when(appUserServiceMock).addNewUser(Matchers.any(UserDTO.class));

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nickname", nickname)
                        .param("password", password)
                        .param("repeatPassword", repeatPassword)
                        .param("email", email)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registration"))
                .andExpect(model().attributeHasFieldErrors("user", "nickname"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("nickname", is(nickname)),
                        hasProperty("password", is(password)),
                        hasProperty("repeatPassword", is(repeatPassword)),
                        hasProperty("email", is(email))
                )));

        verify(appUserServiceMock).addNewUser(Matchers.any(UserDTO.class));
        verifyNoMoreInteractions(appUserServiceMock, userDetailsServiceMock);
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists_ShouldReturnRegistrationView() throws Exception
    {
        String nickname = "John";
        String password = "johnjohn";
        String repeatPassword = "johnjohn";
        String email = "email@gmail.com";

        doThrow(new EmailAlreadyExistsException())
                .when(appUserServiceMock).addNewUser(Matchers.any(UserDTO.class));

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nickname", nickname)
                        .param("password", password)
                        .param("repeatPassword", repeatPassword)
                        .param("email", email)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registration"))
                .andExpect(model().attributeHasFieldErrors("user", "email"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("nickname", is(nickname)),
                        hasProperty("password", is(password)),
                        hasProperty("repeatPassword", is(repeatPassword)),
                        hasProperty("email", is(email))
                )));

        verify(appUserServiceMock).addNewUser(Matchers.any(UserDTO.class));
        verify(appUserServiceMock).getTotalUsersCount();
        verify(clusterServiceMock).getMostPopularClusters(Mockito.anyInt());
        verify(clusterServiceMock).getAvgLessonsCountOfClusters(Mockito.anyInt());
        verifyNoMoreInteractions(appUserServiceMock, userDetailsServiceMock);
    }

    @Test
    public void testRegisterUser_EverythingIsOk_ShouldRedirectToLoginPage() throws Exception
    {
        String nickname = "John";
        String password = "johnjohn";
        String repeatPassword = "johnjohn";
        String email = "email@gmail.com";

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nickname", nickname)
                        .param("password", password)
                        .param("repeatPassword", repeatPassword)
                        .param("email", email)
                        .with(csrf())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/auth/login?isRegistered=true"));

        ArgumentCaptor<UserDTO> captor = ArgumentCaptor.forClass(UserDTO.class);
        verify(appUserServiceMock).addNewUser(captor.capture());
        UserDTO val = captor.getValue();
        assertThat(val.getNickname(), is(nickname));
        assertThat(val.getEmail(), is(email));
        assertThat(val.getPassword(), is(password));
        assertThat(val.getRepeatPassword(), is(repeatPassword));

        verifyNoMoreInteractions(appUserServiceMock, userDetailsServiceMock);
    }
}
