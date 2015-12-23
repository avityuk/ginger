/*
 * Copyright 2013 Andriy Vityuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vityuk.ginger;

/**
 * Localization source
 */
public interface Localization<T> {
    /**
     * Get localized instance of user defined {@link Localizable} interface.
     *
     * @param localizable {@code Localizable} interface, must be not {@code null}
     * @return localized instance of specified {@code localizable} interface implementation
     */
    <U extends T> U getLocalizable(Class<U> localizable);

    /**
     * Get localized message for a given key.
     * <p/>
     *
     * @param key        message key, must be not {@code null}
     * @param parameters {@link java.text.MessageFormat} parameters
     * @return localized message or {@code null} if message not found
     */
    String getMessage(String key, Object... parameters);

    /**
     * Get localized message for a given key and selector. Selector used for resolving the most specific message.
     * <p/>
     *
     * @param key        message key, must be not {@code null}
     * @param selector   used for choosing message from group
     * @param parameters {@link java.text.MessageFormat} parameters
     * @return localized message or {@code null} if message not found
     */
    String getSelectedMessage(String key, String selector, Object... parameters);

    /**
     * Get localized message for a given key and count. It applies plural rules for specific locale and tries to
     * resolve the most specific message.
     * <p/>
     *
     * @param key        message key, must be not {@code null}
     * @param count      used for resolving plural form
     * @param parameters {@link java.text.MessageFormat} parameters
     * @return localized message or {@code null} if message not found
     */
    String getPluralMessage(String key, int count, Object... parameters);
}
