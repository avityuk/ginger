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

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.vityuk.ginger.util.Preconditions.checkNotNull;

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
        Map<String, FormatFactory> formatFactoryRegistry = new HashMap<String, FormatFactory>();

        if (isJodaTimeAvailable()) {
            FormatFactory dateTimeFormatFactory = new JdkAndJodaDateTimeFormatFactory();
            formatFactoryRegistry.put(FormatType.TIME.getFormat(), dateTimeFormatFactory);
            formatFactoryRegistry.put(FormatType.DATE.getFormat(), dateTimeFormatFactory);
            formatFactoryRegistry.put(FormatType.DATETIME.getFormat(), dateTimeFormatFactory);
        } else {
            // Only add 'datetime' format support for JDK Date
            formatFactoryRegistry.put(FormatType.DATETIME.getFormat(), new JdkDateTimeFormatFactory());
        }
        return formatFactoryRegistry;
    }

    private static boolean isJodaTimeAvailable() {
        try {
            Class.forName("org.joda.time.DateTime");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
