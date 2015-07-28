package ru.ildar.languagelearner.database.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.ildar.languagelearner.database.domain.Language;

import java.util.List;

public interface LanguageRepository extends CrudRepository<Language, String>
{
    @Query("select l.defaultName from Language l")
    List<String> findAllGetLanguagesNames();

    Language findByDefaultName(String language);
}
