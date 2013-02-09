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

package com.vityuk.ginger.provider;

import com.vityuk.ginger.LocalizationException;

import java.util.Locale;

/**
 * @author Andriy Vityuk
 */
public class ResourceNotFoundException extends LocalizationException {
    private static final long serialVersionUID = 8851420219646701095L;

    public ResourceNotFoundException(String location, Locale locale) {
        super(createMessage(location, locale));
    }

    private static String createMessage(String location, Locale locale) {
        return "Unable to find resource: '" + location + "' for locale: '" + locale + "'";
    }
}
