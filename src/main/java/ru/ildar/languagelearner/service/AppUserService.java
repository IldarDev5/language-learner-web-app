package ru.ildar.languagelearner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ildar.languagelearner.database.dao.AppUserRepository;
import ru.ildar.languagelearner.database.domain.AppUser;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@Transactional
public class AppUserService implements UserDetailsService
{
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AppUser appUser = appUserRepository.findByNickname(username);
        if(appUser == null)
            throw new UsernameNotFoundException("User not found.");

        return new User(username, appUser.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
