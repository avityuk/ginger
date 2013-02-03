package com.vityuk.ginger.locale;

import com.vityuk.ginger.LocaleResolver;

import java.util.Locale;

/**
 * The basic implementation of {@code LocaleResolver} which uses {@link java.util.Locale#getDefault()} to resolve
 * current locale. It can be used by desktop applications.
 */
public class DefaultLocaleResolver implements LocaleResolver {
    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }
}
