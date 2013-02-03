package com.vityuk.ginger;

import java.util.Locale;

/**
 * Implementations responsible for resolving current {@link Locale}.
 * <p/>
 * For example, desktop application might get it just from {@link java.util.Locale#getDefault()}.
 * In contrast web application might need to get it from current request context.
 */
public interface LocaleResolver {
    /**
     * Resolve current {@code Locale}.
     *
     * @return resolved locale or default value, must never return {@code null}
     */
    Locale getLocale();
}
