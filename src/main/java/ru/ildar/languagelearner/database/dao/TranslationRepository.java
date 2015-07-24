package ru.ildar.languagelearner.database.dao;

import org.springframework.data.repository.CrudRepository;
import ru.ildar.languagelearner.database.domain.Translation;

public interface TranslationRepository extends CrudRepository<Translation, Long>
{
}
