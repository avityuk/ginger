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
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

/**
 * @author Andriy Vityuk
 */
class JodaTimeUtils {
    public static DateTimeFormatter createJodaDateFormatter(FormatType formatType, String style, Locale locale) {
        DateFormatStyle formatStyle = DateFormatStyle.forStyle(style);
        final DateTimeFormatter formatter;
        if (formatStyle == null) {
            formatter = DateTimeFormat.forPattern(style);
        } else {
            formatter = createJodaDateFormatter(formatType, formatStyle);
        }
        return formatter.withLocale(locale);
    }

    private static DateTimeFormatter createJodaDateFormatter(FormatType formatType, DateFormatStyle formatStyle) {
        switch (formatType) {
            case TIME:
                switch (formatStyle) {
                    case SHORT:
                        return DateTimeFormat.shortTime();
                    case MEDIUM:
                        return DateTimeFormat.mediumTime();
                    case LONG:
                        return DateTimeFormat.longTime();
                    case FULL:
                        return DateTimeFormat.fullTime();
                    case DEFAULT:
                        return ISODateTimeFormat.time();
                }
            case DATE:
                switch (formatStyle) {
                    case SHORT:
                        return DateTimeFormat.shortDate();
                    case MEDIUM:
                        return DateTimeFormat.mediumDate();
                    case LONG:
                        return DateTimeFormat.longDate();
                    case FULL:
                        return DateTimeFormat.fullDate();
                    case DEFAULT:
                        return ISODateTimeFormat.date();
                }
            case DATETIME:
                switch (formatStyle) {
                    case SHORT:
                        return DateTimeFormat.shortDateTime();
                    case MEDIUM:
                        return DateTimeFormat.mediumDateTime();
                    case LONG:
                        return DateTimeFormat.longDateTime();
                    case FULL:
                        return DateTimeFormat.fullDateTime();
                    case DEFAULT:
                        return ISODateTimeFormat.dateTime();
                }
        }

        throw new IllegalArgumentException();
    }
}


class JdkAndJodaDateTimeFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String name, String style, Locale locale) {
        FormatType formatType = FormatType.forFormat(name);

        DateTimeFormatter jodaFormatter = JodaTimeUtils.createJodaDateFormatter(formatType, style, locale);
        DateFormat jdkFormatter = JdkDateUtils.createJdkDateFormat(formatType, style, locale);

        return new JdkAndJodaDateFormat(jdkFormatter, jodaFormatter);
    }

}

class JdkAndJodaDateFormat extends Format {
    private final DateFormat jdkFormatter;
    private final DateTimeFormatter jodaFormatter;

    public JdkAndJodaDateFormat(DateFormat jdkFormatter, DateTimeFormatter jodaFormatter) {
        this.jdkFormatter = jdkFormatter;
        this.jodaFormatter = jodaFormatter;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj instanceof ReadableInstant) {
            ReadableInstant readableInstant = (ReadableInstant) obj;
            jodaFormatter.printTo(toAppendTo, readableInstant);
        } else if (obj instanceof ReadablePartial) {
            ReadablePartial readablePartial = (ReadablePartial) obj;
            jodaFormatter.printTo(toAppendTo, readablePartial);
        } else if (obj instanceof Date) {
            Date date = (Date) obj;
            jdkFormatter.format(date, toAppendTo, pos);
        } else {
            throw new IllegalArgumentException("Cannot format given " + obj.getClass() + " as a date");
        }

        return toAppendTo;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }
}
