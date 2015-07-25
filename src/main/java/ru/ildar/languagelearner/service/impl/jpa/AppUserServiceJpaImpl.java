package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ildar.languagelearner.controller.dto.UserDTO;
import ru.ildar.languagelearner.database.dao.AppUserRepository;
import ru.ildar.languagelearner.database.domain.AppUser;
import ru.ildar.languagelearner.exception.EmailAlreadyExistsException;
import ru.ildar.languagelearner.exception.NicknameAlreadyExistsException;
import ru.ildar.languagelearner.service.AppUserService;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@Transactional
public class AppUserServiceJpaImpl implements UserDetailsService, AppUserService
{
    @Autowired
    private AppUserRepository appUserRepository;

    private MessageDigestPasswordEncoder encoder = new Md5PasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AppUser appUser = appUserRepository.findByNickname(username);
        if(appUser == null)
            throw new UsernameNotFoundException("User not found.");

        return new User(username, appUser.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Override
    public Long addNewUser(UserDTO userDTO)
            throws NicknameAlreadyExistsException, EmailAlreadyExistsException
    {
        if(appUserRepository.findByNickname(userDTO.getNickname()) != null)
        {
            throw new NicknameAlreadyExistsException();
        }
        if(appUserRepository.findByEmail(userDTO.getEmail()) != null)
        {
            throw new EmailAlreadyExistsException();
        }

        AppUser appUser = new AppUser();
        appUser.setFirstName(userDTO.getFirstName());
        appUser.setLastName(userDTO.getLastName());
        appUser.setNickname(userDTO.getNickname());
        appUser.setPassword(encoder.encodePassword(userDTO.getPassword(), null));
        appUser.setEmail(userDTO.getEmail());

        return appUserRepository.save(appUser).getUserId();
    }

    @Override
    public AppUser getUserByNickname(String nickname)
    {
        return appUserRepository.findByNickname(nickname);
    }
}
