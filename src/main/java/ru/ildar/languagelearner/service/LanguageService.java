package ru.ildar.languagelearner.service;

import java.util.List;

public interface LanguageService
{
    /** Returns all languages names as strings from database */
    List<String> getLanguagesAsStrings();
}
