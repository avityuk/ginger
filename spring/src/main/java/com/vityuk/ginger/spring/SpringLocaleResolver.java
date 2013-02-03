package com.vityuk.ginger.spring;

import com.vityuk.ginger.LocaleResolver;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Implementation of {@code LocaleResolver} which resolved current locale from Spring locale context.
 * <p/>
 * It uses {@link org.springframework.context.i18n.LocaleContextHolder#getLocale()} to get current locale.
 */
public class SpringLocaleResolver implements LocaleResolver {
    @Override
    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
