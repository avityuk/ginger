package com.vityuk.ginger.spring;

import com.vityuk.ginger.LocaleResolver;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.fest.assertions.api.Assertions.assertThat;

public class SpringLocaleResolverTest {
    private LocaleResolver localeResolver = new SpringLocaleResolver();

    @Test
    public void testGetLocale() throws Exception {
        LocaleContextHolder.setLocale(Locale.FRENCH);

        Locale locale = localeResolver.getLocale();

        assertThat(locale).isNotNull().isEqualTo(Locale.FRENCH);
    }

    @Test
    public void testGetLocaleWithNullLocale() throws Exception {
        LocaleContextHolder.setLocale(null);

        Locale locale = localeResolver.getLocale();

        assertThat(locale).isNotNull().isEqualTo(Locale.getDefault());
    }
}
