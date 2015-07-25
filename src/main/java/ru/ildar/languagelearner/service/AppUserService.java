package ru.ildar.languagelearner.service;

import ru.ildar.languagelearner.controller.dto.UserDTO;
import ru.ildar.languagelearner.database.domain.AppUser;
import ru.ildar.languagelearner.exception.EmailAlreadyExistsException;
import ru.ildar.languagelearner.exception.NicknameAlreadyExistsException;

public interface AppUserService
{
    /**
     * Adds new user to the database
     * @param userDTO DTO object of a user to add
     * @throws ru.ildar.languagelearner.exception.NicknameAlreadyExistsException thrown when entered nickname is duplicate
     * @throws EmailAlreadyExistsException thrown when entered E-Mail is duplicate
     * @return Newly appointed ID of this entity
     */
    Long addNewUser(UserDTO userDTO)
            throws NicknameAlreadyExistsException, EmailAlreadyExistsException;

    AppUser getUserByNickname(String name);
}
