package com.vityuk.ginger.locale;

import com.vityuk.ginger.LocaleResolver;

import java.util.Locale;

public class DefaultLocaleResolver implements LocaleResolver {
    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }
}
