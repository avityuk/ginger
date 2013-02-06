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

package com.vityuk.ginger.provider.format;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;

import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Andriy Vityuk
 */
public class DefaultMessageFormatFactory implements MessageFormatFactory {
    private final Map<String, FormatFactory> formatFactoryRegistry;

    public DefaultMessageFormatFactory() {
        formatFactoryRegistry = createFactoryRegistry();
    }

    @Override
    public MessageFormat create(Locale locale, String format) {
        checkNotNull(locale);
        checkNotNull(format);
        return new ExtendedMessageFormat(format, locale, formatFactoryRegistry);
    }

    private static Map<String, FormatFactory> createFactoryRegistry() {
        Map<String, FormatFactory> formatFactoryRegistry = Maps.newHashMap();
        formatFactoryRegistry.put("datetime", new DateTimeFormatFactory());
        return formatFactoryRegistry;
    }

    private static class DateTimeFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            int styleCode = toStyleCode(style);
            if (styleCode == -1) {
                return new SimpleDateFormat(style, locale);
            }
            return DateFormat.getDateTimeInstance(styleCode, styleCode, locale);
        }

        private int toStyleCode(String style) {
            if (style == null) {
                return DateFormat.DEFAULT;
            }
            if ("short".equals(style)) {
                return DateFormat.SHORT;
            }
            if ("medium".equals(style)) {
                return DateFormat.MEDIUM;
            }
            if ("long".equals(style)) {
                return DateFormat.LONG;
            }
            if ("full".equals(style)) {
                return DateFormat.FULL;
            }

            return -1;

        }
    }

}
