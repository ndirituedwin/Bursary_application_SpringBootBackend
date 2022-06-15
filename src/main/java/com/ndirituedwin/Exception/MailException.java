package com.ndirituedwin.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MailException extends RuntimeException {
    public MailException(String s) {
        super(s);
    }
}
