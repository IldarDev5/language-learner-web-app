package ru.ildar.languagelearner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "This translation is not from the correct cluster")
public class NotThatClusterException extends RuntimeException
{
}
