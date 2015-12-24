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

package com.vityuk.ginger.spring;

import com.vityuk.ginger.LocaleResolver;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Implementation of {@code LocaleResolver} which resolved current locale from Spring locale context.
 *
 * It uses {@link org.springframework.context.i18n.LocaleContextHolder#getLocale()} to get current locale.
 *
 * @author Andriy Vityuk
 */
public class SpringLocaleResolver implements LocaleResolver {
    @Override
    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
