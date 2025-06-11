package com.techsophy.tsf.form.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GlobalMessageSource {
    private final MessageSource messageSource;

    public String get(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public String get(String key, String args) {
        return messageSource.getMessage(key, new Object[]{args}, LocaleContextHolder.getLocale());
    }
}
