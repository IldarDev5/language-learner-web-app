package ru.ildar.languagelearner.service.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ildar.languagelearner.database.dao.LanguageRepository;
import ru.ildar.languagelearner.service.LanguageService;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service("languageService")
@Transactional
public class LanguageServiceJpaImpl implements LanguageService
{
    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public List<String> getLanguagesAsStrings()
    {
        return languageRepository.findAllGetLanguagesNames();
    }
}
