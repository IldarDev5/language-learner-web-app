package ru.ildar.languagelearner.service.impl.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ildar.languagelearner.database.dao.TranslationRepository;
import ru.ildar.languagelearner.service.TranslationService;

import javax.transaction.Transactional;

@Service
@Transactional
public class TranslationServiceJpaImpl implements TranslationService
{
    @Autowired
    private TranslationRepository translationRepository;
}
