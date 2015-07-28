package ru.ildar.languagelearner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClusterNotOfThisUserException extends RuntimeException
{
}
