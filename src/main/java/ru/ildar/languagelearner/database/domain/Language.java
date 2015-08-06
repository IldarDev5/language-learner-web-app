package ru.ildar.languagelearner.database.domain;

import javax.persistence.*;
import java.io.Serializable;

/** Represents languages in the application. Other entities use this entity to define
 * a sentence and translation languages. */
@Entity
public class Language implements Serializable
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (!defaultName.equals(language.defaultName)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return defaultName.hashCode();
    }
}
