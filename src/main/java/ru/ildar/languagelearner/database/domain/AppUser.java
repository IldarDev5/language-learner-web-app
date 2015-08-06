package ru.ildar.languagelearner.database.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity that stores credentials and other information about application user */
@Entity
public class AppUser implements Serializable
{
    /** User's primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /** User's nickname for logging in */
    @Column(length = 100, nullable = false, unique = true)
    private String nickname;
    /** First name */
    @Column(length = 100)
    private String firstName;
    /** Last name */
    @Column(length = 100)
    private String lastName;
    /** User's E-Mail address */
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    /** User's password for logging in */
    @Column(length = 50, nullable = false)
    private String password;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
