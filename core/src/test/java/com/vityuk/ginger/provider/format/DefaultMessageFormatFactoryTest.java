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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Andriy Vityuk
 */
public class DefaultMessageFormatFactoryTest {
    private MessageFormatFactory messageFormatFactory = new DefaultMessageFormatFactory();

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullLocale() throws Exception {
        messageFormatFactory.create(null, "Test");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullFormat() throws Exception {
        messageFormatFactory.create(Locale.ITALY, null);
    }

    @Test
    public void testDefaultWithString() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "My name is {0}");


        String result = messageFormat.format(new Object[]{"Tamerlan"});

        assertThat(result).isNotNull().isEqualTo("My name is Tamerlan");
    }

    @Test
    public void testDefaultWithInteger() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.US, "I am {0} years old");


        String result = messageFormat.format(new Object[]{3});

        assertThat(result).isNotNull().isEqualTo("I am 3 years old");
    }

    @Test
    public void testDefaultWithDouble() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.US, "My weight is {0} lb");


        String result = messageFormat.format(new Object[]{15.3753});

        assertThat(result).isNotNull().isEqualTo("My weight is 15.375 lb");
    }

    @Test
    public void testDefaultWithDate() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.US, "Now is {0}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 2/5/13 8:47 PM");
    }

    @Test
    public void testDefaultWithAnyObject() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.US, "Weekends: {0}");


        String result = messageFormat.format(new Object[]{Arrays.asList("Sunday", "Saturday")});

        assertThat(result).isNotNull().isEqualTo("Weekends: [Sunday, Saturday]");
    }

    @Test
    public void testTimeWithDefaultStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, time}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 20:47:23");
    }

    @Test
    public void testTimeWithShortStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, time, short}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 20:47");
    }

    @Test
    public void testTimeWithMediumStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, time, medium}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 8:47:23 PM");
    }

    @Test
    public void testTimeWithLongStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, time, long}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 8:47:23 PM " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
    }

    @Test
    public void testTimeWithFullStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ITALY, "Now is {0, time, full}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 20.47.23 " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
    }

    @Test
    public void testTimeWithCustomStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH,
                "Now is {0, datetime, HH:mm:ss}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 20:47:23");
    }

    @Test
    public void testDateWithDefaultStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, date}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 5 févr. 2013");
    }

    @Test
    public void testDateWithShortStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, date, short}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 05/02/13");
    }

    @Test
    public void testDateWithMediumStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, date, medium}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is Feb 5, 2013");
    }

    @Test
    public void testDateWithLongStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, date, long}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is February 5, 2013");
    }

    @Test
    public void testDateWithFullStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ITALY, "Now is {0, date, full}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is martedì 5 febbraio 2013");
    }

    @Test
    public void testDateWithCustomStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH,
                "Now is {0, datetime, yyyy-MM-dd}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 2013-02-05");
    }

    @Test
    public void testDateTimeWithDefaultStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, datetime}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 5 févr. 2013 20:47:23");
    }

    @Test
    public void testDateTimeWithShortStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, datetime, short}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 05/02/13 20:47");
    }

    @Test
    public void testDateTimeWithMediumStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, datetime, medium}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is Feb 5, 2013 8:47:23 PM");
    }

    @Test
    public void testDateTimeWithLongStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, datetime, long}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is February 5, 2013 8:47:23 PM " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
    }

    @Test
    public void testDateTimeWithFullStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ITALY, "Now is {0, datetime, full}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is martedì 5 febbraio 2013 20.47.23 " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
    }

    @Test
    public void testDateTimeWithCustomStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH,
                "Now is {0, datetime, yyyy-MM-dd HH:mm:ss}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 2013-02-05 20:47:23");
    }

    @Test
    public void testJodaTimeWithDefaultStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, time}");


        String result = messageFormat.format(new Object[]{createJodaTime()});

        assertThat(result).isNotNull().isEqualTo("Now is 20:47:23.037");
    }

    @Test
    public void testJodaTimeWithShortStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, time, short}");


        String result = messageFormat.format(new Object[]{createJodaTime()});

        assertThat(result).isNotNull().isEqualTo("Now is 20:47");
    }

    @Test
    public void testJodaTimeWithMediumStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, time, medium}");


        String result = messageFormat.format(new Object[]{createJodaTime()});

        assertThat(result).isNotNull().isEqualTo("Now is 8:47:23 PM");
    }

    @Test
    public void testJodaTimeWithLongStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, time, long}");


        String result = messageFormat.format(new Object[]{createJodaTime()});

        assertThat(result).isNotNull().isEqualTo("Now is 8:47:23 PM ");
    }

    @Test
    public void testJodaTimeWithFullStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ITALY, "Now is {0, time, full}");


        String result = messageFormat.format(new Object[]{createJodaTime()});

        assertThat(result).isNotNull().isEqualTo("Now is 20.47.23 ");
    }

    @Test
    public void testJodaTimeWithCustomStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH,
                "Now is {0, datetime, HH:mm:ss}");


        String result = messageFormat.format(new Object[]{createJodaTime()});

        assertThat(result).isNotNull().isEqualTo("Now is 20:47:23");
    }

    @Test
    public void testJodaDateWithDefaultStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, date}");


        String result = messageFormat.format(new Object[]{createJodaDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 2013-02-05");
    }

    @Test
    public void testJodaDateWithShortStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, date, short}");


        String result = messageFormat.format(new Object[]{createJodaDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 05/02/13");
    }

    @Test
    public void testJodaDateWithMediumStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, date, medium}");


        String result = messageFormat.format(new Object[]{createJodaDate()});

        assertThat(result).isNotNull().isEqualTo("Now is Feb 5, 2013");
    }

    @Test
    public void testJodaDateWithLongStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, date, long}");


        String result = messageFormat.format(new Object[]{createJodaDate()});

        assertThat(result).isNotNull().isEqualTo("Now is February 5, 2013");
    }

    @Test
    public void testJodaDateWithFullStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ITALY, "Now is {0, date, full}");


        String result = messageFormat.format(new Object[]{createJodaDate()});

        assertThat(result).isNotNull().isEqualTo("Now is martedì 5 febbraio 2013");
    }

    @Test
    public void testJodaDateWithCustomStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH,
                "Now is {0, datetime, yyyy-MM-dd}");


        String result = messageFormat.format(new Object[]{createJodaDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 2013-02-05");
    }

    @Test
    public void testJodaDateTimeWithDefaultStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, datetime}");


        String result = messageFormat.format(new Object[]{createJodaDateTime()});

        assertThat(result).isNotNull().isEqualTo("Now is 2013-02-05T20:47:23.037Z");
    }

    @Test
    public void testJodaDateTimeWithShortStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.FRENCH, "Now is {0, datetime, short}");


        String result = messageFormat.format(new Object[]{createJodaDateTime()});

        assertThat(result).isNotNull().isEqualTo("Now is 05/02/13 20:47");
    }

    @Test
    public void testJodaDateTimeWithMediumStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, datetime, medium}");


        String result = messageFormat.format(new Object[]{createJodaDateTime()});

        assertThat(result).isNotNull().isEqualTo("Now is Feb 5, 2013 8:47:23 PM");
    }

    @Test
    public void testJodaDateTimeWithLongStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, datetime, long}");


        String result = messageFormat.format(new Object[]{createJodaDateTime()});

        assertThat(result).isNotNull().isEqualTo("Now is February 5, 2013 8:47:23 PM UTC");
    }

    @Test
    public void testJodaDateTimeWithFullStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ITALY, "Now is {0, datetime, full}");


        String result = messageFormat.format(new Object[]{createJodaDateTime()});

        assertThat(result).isNotNull().isEqualTo("Now is martedì 5 febbraio 2013 20.47.23 UTC");
    }

    @Test
    public void testJodaDateTimeWithCustomStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH,
                "Now is {0, datetime, yyyy-MM-dd HH:mm:ss}");


        String result = messageFormat.format(new Object[]{createJodaDateTime()});

        assertThat(result).isNotNull().isEqualTo("Now is 2013-02-05 20:47:23");
    }

    @Test
    public void testNumberWithDefaultStyleAndInteger() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number} lb");


        String result = messageFormat.format(new Object[]{10003});

        assertThat(result).isNotNull().isEqualTo("10,003 lb");
    }

    @Test
    public void testNumberWithDefaultStyleAndDouble() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number} lb");


        String result = messageFormat.format(new Object[]{10003.751});

        assertThat(result).isNotNull().isEqualTo("10,003.751 lb");
    }

    @Test
    public void testNumberWithDefaultStyleAndBigDecimal() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number} lb");


        String result = messageFormat.format(new Object[]{BigDecimal.valueOf(10001375, 3)});

        assertThat(result).isNotNull().isEqualTo("10,001.375 lb");
    }

    @Test
    public void testNumberWithIntegerStyleAndInteger() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number, integer} lb");


        String result = messageFormat.format(new Object[]{10003});

        assertThat(result).isNotNull().isEqualTo("10,003 lb");
    }

    @Test
    public void testNumberWithIntegerStyleAndDouble() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number, integer} lb");


        String result = messageFormat.format(new Object[]{10003.75});

        assertThat(result).isNotNull().isEqualTo("10,004 lb");
    }

    @Test
    public void testNumberWithIntegerStyleAndBigDecimal() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number, integer} lb");


        String result = messageFormat.format(new Object[]{BigDecimal.valueOf(10001375, 3)});

        assertThat(result).isNotNull().isEqualTo("10,001 lb");
    }

    @Test
    public void testNumberWithCurrencyStyleAndInteger() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.US, "{0, number, currency}/lb");


        String result = messageFormat.format(new Object[]{10003});

        assertThat(result).isNotNull().isEqualTo("$10,003.00/lb");
    }


    @Test
    public void testNumberWithCurrencyStyleAndDouble() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.US, "{0, number, currency}/lb");


        String result = messageFormat.format(new Object[]{10003.751});

        assertThat(result).isNotNull().isEqualTo("$10,003.75/lb");
    }


    @Test
    public void testNumberWithCurrencyStyleAndBigDecimal() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.US, "{0, number, currency}/lb");


        String result = messageFormat.format(new Object[]{BigDecimal.valueOf(10001375, 3)});

        assertThat(result).isNotNull().isEqualTo("$10,001.38/lb");
    }

    @Test
    public void testNumberWithPercentStyleAndInteger() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number, percent} of lb");


        String result = messageFormat.format(new Object[]{79});

        assertThat(result).isNotNull().isEqualTo("7,900% of lb");
    }

    @Test
    public void testNumberWithPercentStyleAndDouble() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number, percent} of lb");


        String result = messageFormat.format(new Object[]{0.751});

        assertThat(result).isNotNull().isEqualTo("75% of lb");
    }

    @Test
    public void testNumberWithPercentStyleAndBigDecimal() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number, percent} of lb");


        String result = messageFormat.format(new Object[]{BigDecimal.valueOf(333, 3)});

        assertThat(result).isNotNull().isEqualTo("33% of lb");
    }

    @Test
    public void testNumberWithCustomStyleAndInteger() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number,#,##.000} lb");


        String result = messageFormat.format(new Object[]{10003});

        assertThat(result).isNotNull().isEqualTo("1,00,03.000 lb");
    }

    @Test
    public void testNumberWithCustomStyleAndDouble() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number,#,##.000} lb");


        String result = messageFormat.format(new Object[]{10003.75});

        assertThat(result).isNotNull().isEqualTo("1,00,03.750 lb");
    }

    @Test
    public void testNumberWithCustomStyleAndBigDecimal() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "{0, number,#,##.000} lb");


        String result = messageFormat.format(new Object[]{BigDecimal.valueOf(10001375, 3)});

        assertThat(result).isNotNull().isEqualTo("1,00,01.375 lb");
    }

    private static Date createDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 1, 5, 20, 47, 23);
        return calendar.getTime();
    }

    private static LocalTime createJodaTime() {
        return new LocalTime(20, 47, 23, 37);
    }

    private static LocalDate createJodaDate() {
        return new LocalDate(2013, 2, 5);
    }

    private static DateTime createJodaDateTime() {
        return new DateTime(2013, 2, 5, 20, 47, 23, 37, DateTimeZone.UTC);
    }
}
