package ru.ildar.languagelearner.database.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.ildar.languagelearner.controller.dto.LanguagePairDTO;
import ru.ildar.languagelearner.database.domain.Language;

import java.util.List;

public interface LanguageRepository extends CrudRepository<Language, String>
{
    @Query("select l.defaultName from Language l")
    List<String> getLanguagesNames();

    Language findByDefaultName(String language);

    @Query("select new ru.ildar.languagelearner.controller.dto" +
            ".LanguagePairDTO(l1.defaultName, l2.defaultName) " +
            "from Language l1, Language l2 " +
            "where l1 <> l2 and not exists " +
            "       (select cl from Cluster cl where cl.appUser.nickname = ?1 and" +
            "       (cl.language1 = l1 and cl.language2 = l2 or cl.language1 = l2 and cl.language2 = l1))")
    List<LanguagePairDTO> findNonExistentLanguagePair(String nickname);
}
