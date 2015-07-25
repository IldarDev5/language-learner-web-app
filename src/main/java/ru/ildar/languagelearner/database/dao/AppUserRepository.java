package ru.ildar.languagelearner.database.dao;

import org.springframework.data.repository.CrudRepository;
import ru.ildar.languagelearner.database.domain.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long>
{
    AppUser findByNickname(String nickname);

    AppUser findByEmail(String email);
}
