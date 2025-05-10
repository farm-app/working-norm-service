package ru.rtln.workingnormservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Object for reading error messages from application.properties.
 */
@RequiredArgsConstructor
@Component
public class MessageHelper {

    private final MessageSource messageSource;

    public String getMessage(String key) {
        var defaultMessage = "К сожалению, для этой ошибки нет описания на вашем языке";
        return messageSource.getMessage(key, null, defaultMessage,  Locale.forLanguageTag("ru"));
    }
}
