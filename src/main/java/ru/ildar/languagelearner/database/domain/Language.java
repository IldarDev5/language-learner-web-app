package ru.ildar.languagelearner.database.domain;

import javax.persistence.*;

/** Represents languages in the application. Other entities use this entity to define
 * a sentence and translation languages. */
@Entity
public class Language
{
    /** Entity's primary key - name of the language in English */
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
