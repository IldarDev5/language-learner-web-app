package ru.ildar.languagelearner.database.domain;

import javax.persistence.*;

@Entity
public class Language
{
    @Id
    @Column(length = 100)
    private String defaultName;

    public Language(String defaultName)
    {
        this.defaultName = defaultName;
    }
    public Language() { }

    public String getDefaultName()
    {
        return defaultName;
    }

    public void setDefaultName(String defaultName)
    {
        this.defaultName = defaultName;
    }
}
