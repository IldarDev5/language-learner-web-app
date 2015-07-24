package ru.ildar.languagelearner.database.dao;

import org.springframework.data.repository.CrudRepository;
import ru.ildar.languagelearner.database.domain.Language;

public interface LanguageRepository extends CrudRepository<Language, String>
{
}
