package ru.ildar.languagelearner.service.impl.jpa;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.ildar.languagelearner.controller.dto.UserDTO;
import ru.ildar.languagelearner.database.domain.AppUser;
import ru.ildar.languagelearner.exception.EmailAlreadyExistsException;
import ru.ildar.languagelearner.exception.NicknameAlreadyExistsException;
import ru.ildar.languagelearner.service.BaseServiceTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class AppUserServiceJpaImplTest extends BaseServiceTest
{
    private MessageDigestPasswordEncoder encoder = new Md5PasswordEncoder();

    @Test(expected = UsernameNotFoundException.class)
    public void testUserByUsername_ShouldThrowException()
    {
        appUserService.loadUserByUsername("Ildar");
    }

    @Test
    public void testUserByUsername_ShouldReturnNewUser()
    {
        AppUser user = new AppUser();
        user.setNickname("Ildar");
        user.setPassword("password");
        doReturn(user).when(appUserRepository).findByNickname(user.getNickname());

        UserDetails ret = appUserService.loadUserByUsername(user.getNickname());
        assertThat(ret.getUsername(), is(user.getNickname()));
        assertThat(ret.getPassword(), is(user.getPassword()));
        assertThat(ret.getAuthorities(), contains(new SimpleGrantedAuthority("ROLE_USER")));

        verify(appUserRepository).findByNickname(user.getNickname());
        verifyNoMoreInteractions(repos);
    }

    @Test(expected = NicknameAlreadyExistsException.class)
    public void testAddNewUser_ShouldThrowNicknameAlreadyExistsException() throws Exception
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setNickname("Ildar");
        doReturn(new AppUser()).when(appUserRepository).findByNickname(userDTO.getNickname());
        appUserService.addNewUser(userDTO);
    }

    @Test(expected = EmailAlreadyExistsException.class)
    public void testAddNewUser_ShouldThrowEmailAlreadyExistsException() throws Exception
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setNickname("Ildar");
        userDTO.setEmail("email");
        doReturn(new AppUser()).when(appUserRepository).findByEmail(userDTO.getEmail());
        appUserService.addNewUser(userDTO);
    }

    @Test
    public void testAddNewUser_ShouldAddNewUser() throws Exception
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setNickname("Ildar");
        userDTO.setEmail("email");
        userDTO.setPassword("password");

        doReturn(new AppUser()).when(appUserRepository).save(Matchers.any(AppUser.class));

        appUserService.addNewUser(userDTO);

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(captor.capture());
        AppUser val = captor.getValue();

        assertThat(val.getEmail(), is(userDTO.getEmail()));
        assertThat(val.getNickname(), is(userDTO.getNickname()));
        assertThat(val.getPassword(), is(encoder.encodePassword(userDTO.getPassword(), null)));

        verify(appUserRepository).findByEmail(userDTO.getEmail());
        verify(appUserRepository).findByNickname(userDTO.getNickname());
        verifyNoMoreInteractions(repos);
    }

    @Test
    public void testGetUserByNickname()
    {
        String nickname = "Ildar";
        appUserService.getUserByNickname(nickname);

        verify(appUserRepository).findByNickname(nickname);
    }
}