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

import org.apache.commons.lang3.text.FormatFactory;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Andriy Vityuk
 */
abstract class JdkDateUtils {
    private JdkDateUtils() {
    }

    public static DateFormat createJdkDateFormat(FormatType formatType, String style,
                                                 Locale locale) {
        DateFormatStyle formatStyle = DateFormatStyle.forStyle(style);
        if (formatStyle == null) {
            return new SimpleDateFormat(style, locale);
        } else {
            return createJdkDateFormat(formatType, formatStyle, locale);
        }
    }

    private static DateFormat createJdkDateFormat(FormatType formatType, DateFormatStyle formatStyle,
                                                  Locale locale) {
        int style = createJdkDateStyleCode(formatStyle);
        switch (formatType) {
            case TIME:
                return DateFormat.getTimeInstance(style, locale);
            case DATE:
                return DateFormat.getDateInstance(style, locale);
            case DATETIME:
                return DateFormat.getDateTimeInstance(style, style, locale);
        }
        throw new IllegalArgumentException();
    }

    private static int createJdkDateStyleCode(DateFormatStyle formatStyle) {
        switch (formatStyle) {
            case SHORT:
                return DateFormat.SHORT;
            case MEDIUM:
                return DateFormat.MEDIUM;
            case LONG:
                return DateFormat.LONG;
            case FULL:
                return DateFormat.FULL;
            case DEFAULT:
                return DateFormat.DEFAULT;
            default:
                throw new IllegalArgumentException();
        }
    }
}

class JdkDateTimeFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String name, String style, Locale locale) {
        FormatType formatType = FormatType.forFormat(name);
        return JdkDateUtils.createJdkDateFormat(formatType, style, locale);
    }
}
