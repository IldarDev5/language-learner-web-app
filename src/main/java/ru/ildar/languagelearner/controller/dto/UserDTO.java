package ru.ildar.languagelearner.controller.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO
{
    @NotNull
    @Size(min = 2, max = 100)
    private String nickname;

    @NotNull
    @Size(min = 6, max = 50)
    private String password;

    @NotNull
    @Size(min = 6, max = 50)
    private String repeatPassword;

    @NotNull
    @Email
    private String email;

    @Size(min = 2, max = 100)
    private String firstName;
    @Size(min = 2, max = 100)
    private String lastName;

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRepeatPassword()
    {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword)
    {
        this.repeatPassword = repeatPassword;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
