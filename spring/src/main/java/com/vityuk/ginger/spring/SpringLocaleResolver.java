package com.vityuk.ginger.spring;

import com.vityuk.ginger.LocaleResolver;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class SpringLocaleResolver implements LocaleResolver {
    @Override
    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
