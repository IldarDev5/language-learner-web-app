package ru.ildar.languagelearner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "The cluster doesn't belong to this user")
public class ClusterNotOfThisUserException extends RuntimeException
{
}
