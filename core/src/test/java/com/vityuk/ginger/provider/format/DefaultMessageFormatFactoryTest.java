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

import org.junit.Test;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    public void testDatetimeWithDefaultStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, datetime}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is Feb 5, 2013 8:47:23 PM");
    }

    @Test
    public void testDatetimeWithShortStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, datetime, short}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 2/5/13 8:47 PM");
    }

    @Test
    public void testDatetimeWithMediumStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, datetime, medium}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is Feb 5, 2013 8:47:23 PM");
    }

    @Test
    public void testDatetimeWithLongStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, datetime, long}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is February 5, 2013 8:47:23 PM PST");
    }

    @Test
    public void testDatetimeWithFullStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH, "Now is {0, datetime, full}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is Tuesday, February 5, 2013 8:47:23 PM PST");
    }

    @Test
    public void testDatetimeWithCustomStyle() throws Exception {
        MessageFormat messageFormat = messageFormatFactory.create(Locale.ENGLISH,
                "Now is {0, datetime, yyyy-MM-dd HH:mm:ss}");


        String result = messageFormat.format(new Object[]{createDate()});

        assertThat(result).isNotNull().isEqualTo("Now is 2013-02-05 20:47:23");
    }

    private static Date createDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 1, 5, 20, 47, 23);
        return calendar.getTime();
    }
}
