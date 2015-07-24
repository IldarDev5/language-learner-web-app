package ru.ildar.languagelearner.service.impl.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ildar.languagelearner.database.dao.LanguageRepository;
import ru.ildar.languagelearner.service.LanguageService;

import javax.transaction.Transactional;

@Service
@Transactional
public class LanguageServiceJpaImpl implements LanguageService
{
    @Autowired
    private LanguageRepository languageRepository;
}
