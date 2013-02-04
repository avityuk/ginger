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
public interface Localization {
    /**
     * Get localized instance of user defined {@link Localizable} interface.
     *
     * @param localizable {@code Localizable} interface, must be not {@code null}
     * @return localized instance of specified {@code localizable} interface implementation
     */
    <T extends Localizable> T getLocalizable(Class<T> localizable);

    /**
     * Get localized message for a given key.
     * <p/>
     * If the message for a given key is not found behavior of this method determined by... TODO: add config
     *
     * @param key message key, must be not {@code null}
     * @return localized message
     */
    String getMessage(String key);
}
