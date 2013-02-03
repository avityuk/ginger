package com.vityuk.ginger.locale;

import com.vityuk.ginger.LocaleResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.fest.assertions.api.Assertions.assertThat;

public class DefaultLocaleResolverTest {
    private LocaleResolver localeResolver = new DefaultLocaleResolver();

    private Locale defaultLocale;

    @Before
    public void setUp() throws Exception {
        defaultLocale = Locale.getDefault();
    }

    @After
    public void tearDown() throws Exception {
        Locale.setDefault(defaultLocale);
    }

    @Test
    public void testGetLocale() throws Exception {
        Locale.setDefault(Locale.FRENCH);

        Locale locale = localeResolver.getLocale();

        assertThat(locale).isNotNull().isEqualTo(Locale.FRENCH);
    }
}
