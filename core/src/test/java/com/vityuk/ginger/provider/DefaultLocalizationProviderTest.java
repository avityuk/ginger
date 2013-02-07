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

import com.google.common.collect.ImmutableMap;
import com.vityuk.ginger.LocaleResolver;
import com.vityuk.ginger.LocalizationProvider;
import com.vityuk.ginger.PropertyResolver;
import com.vityuk.ginger.loader.LocalizationLoader;
import com.vityuk.ginger.loader.ResourceLoader;
import com.vityuk.ginger.provider.format.MessageFormatFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultLocalizationProviderTest {
    private static String LOCATION = "test:/TestResources.data";
    private static String LOCATION_ITALIAN = "test:/TestResources_it.data";
    private static String LOCATION_ITALY = "test:/TestResources_it_IT.data";
    private static String LOCATION_FRENCH = "test:/TestResources_fr.data";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private LocaleResolver localeResolver;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private LocalizationLoader localizationLoader;

    @Mock
    MessageFormatFactory messageFormatFactory;

    @Mock
    private InputStream inputStream;

    @Mock
    private PropertyResolver propertyResolver;

    @Test(expected = NullPointerException.class)
    public void testWithNullLocale() throws Exception {
        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(null);

        try {
            localizationProvider.getString("str.key");
        } finally {
            InOrder inOrder = inOrder();
            inOrder.verify(localeResolver).getLocale();
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testWithNotSupportedLocation() throws Exception {
        thrown.expect(UnsupportedLocation.class);
        String expectedMessage = "Unsupported location: '" + LOCATION + "'";
        thrown.expectMessage(expectedMessage);

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(false);

        try {
            localizationProvider.getString("str.key");
        } finally {
            InOrder inOrder = inOrder();
            inOrder.verify(localeResolver).getLocale();
            inOrder.verify(resourceLoader).isSupported(LOCATION);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testWithNotFoundResource() throws Exception {
        thrown.expect(ResourceNotFound.class);
        String expectedMessage = "Unable to find resource: '" + LOCATION + "' for locale: '" + Locale.ITALY + "'";
        thrown.expectMessage(expectedMessage);

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION)).thenReturn(null);

        try {
            localizationProvider.getString("str.key");
        } finally {
            InOrder inOrder = inOrder();
            inOrder.verify(localeResolver).getLocale();
            inOrder.verify(resourceLoader).isSupported(LOCATION);
            inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
            inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
            inOrder.verify(resourceLoader).openStream(LOCATION);
            inOrder.verifyNoMoreInteractions();
        }
    }


    @Test
    public void testWithNotPresentKey() throws Exception {
        String key = "str.key";

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getString(key)).thenReturn(null);

        String result = localizationProvider.getString(key);

        assertThat(result).isNull();
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getString(key);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetString() throws Exception {
        String key = "str.key";
        String value = "test value";

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getString(key)).thenReturn(value);

        String result = localizationProvider.getString(key);

        assertThat(result).isNotNull().isEqualTo(value);
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getString(key);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetBoolean() throws Exception {
        String key = "boolean.key";
        Boolean value = Boolean.TRUE;

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getBoolean(key)).thenReturn(value);

        Boolean result = localizationProvider.getBoolean(key);

        assertThat(result).isNotNull().isEqualTo(value);
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getBoolean(key);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetInteger() throws Exception {
        String key = "integer.key";
        Integer value = Integer.MAX_VALUE;

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getInteger(key)).thenReturn(value);

        Integer result = localizationProvider.getInteger(key);

        assertThat(result).isNotNull().isEqualTo(value);
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getInteger(key);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetLong() throws Exception {
        String key = "long.key";
        Long value = Long.MIN_VALUE;

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getLong(key)).thenReturn(value);

        Long result = localizationProvider.getLong(key);

        assertThat(result).isNotNull().isEqualTo(value);
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getLong(key);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetFloat() throws Exception {
        String key = "float.key";
        Float value = Float.MIN_VALUE;

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getFloat(key)).thenReturn(value);

        Float result = localizationProvider.getFloat(key);

        assertThat(result).isNotNull().isEqualTo(value);
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getFloat(key);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetDouble() throws Exception {
        String key = "double.key";
        Double value = Double.MAX_VALUE;

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getDouble(key)).thenReturn(value);

        Double result = localizationProvider.getDouble(key);

        assertThat(result).isNotNull().isEqualTo(value);
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getDouble(key);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetStringList() throws Exception {
        String key = "string.list.key";
        List<String> value = Arrays.asList("Saturday", "Sunday");

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getStringList(key)).thenReturn(value);

        List<String> result = localizationProvider.getStringList(key);

        assertThat(result).isNotNull().isEqualTo(value);
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getStringList(key);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetStringMap() throws Exception {
        String key = "boolean.key";
        Map<String, String> value = ImmutableMap.<String, String>builder()
                .put("red", "#FF0000")
                .put("cyan", "#00FFFF")
                .put("white", "#FFFFFF")
                .put("black", "#000000")
                .build();

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getStringMap(key)).thenReturn(value);

        Map<String, String> result = localizationProvider.getStringMap(key);

        assertThat(result).isNotNull().isEqualTo(value);
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getStringMap(key);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetMessageWithZeroParameters() throws Exception {
        String key = "message.key";
        String value = "Hello, World!";

        MessageFormat messageFormat = new MessageFormat(value, Locale.ITALY);

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getString(key)).thenReturn(value);
        when(messageFormatFactory.create(Locale.ITALY, value)).thenReturn(messageFormat);

        String result = localizationProvider.getMessage(key);

        assertThat(result).isNotNull().isEqualTo(value);
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getString(key);
        inOrder.verify(messageFormatFactory).create(Locale.ITALY, value);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetMessageWithMultipleParameters() throws Exception {
        String key = "message.key";
        String value = "Hello, {0}. Today is {1,date}. Current time is {1,time,short}";

        MessageFormat messageFormat = new MessageFormat(value, Locale.ITALY);

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(null);
        when(resourceLoader.openStream(LOCATION_ITALIAN)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getString(key)).thenReturn(value);
        when(messageFormatFactory.create(Locale.ITALY, value)).thenReturn(messageFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 1, 5, 20, 47);
        String result = localizationProvider.getMessage(key, "Bob", calendar.getTime());

        assertThat(result).isNotNull().isEqualTo("Hello, Bob. Today is 5-feb-2013. Current time is 20.47");
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALIAN);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getString(key);
        inOrder.verify(messageFormatFactory).create(Locale.ITALY, value);
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void testGetMessageWithNullFormat() throws Exception {
        String key = "message.key";

        LocalizationProvider localizationProvider = createDefault();
        when(localeResolver.getLocale()).thenReturn(Locale.ITALY);
        when(resourceLoader.isSupported(LOCATION)).thenReturn(true);
        when(resourceLoader.openStream(LOCATION_ITALY)).thenReturn(inputStream);
        when(localizationLoader.load(inputStream)).thenReturn(propertyResolver);
        when(propertyResolver.getString(key)).thenReturn(null);

        String result = localizationProvider.getMessage(key);

        assertThat(result).isNull();
        InOrder inOrder = inOrder();
        inOrder.verify(localeResolver).getLocale();
        inOrder.verify(resourceLoader).isSupported(LOCATION);
        inOrder.verify(resourceLoader).openStream(LOCATION_ITALY);
        inOrder.verify(localizationLoader).load(inputStream);
        inOrder.verify(propertyResolver).getString(key);
        inOrder.verifyNoMoreInteractions();
    }


    private LocalizationProvider createDefault() {
        return createBaseBuilder().withLocations(Arrays.asList(LOCATION)).build();
    }

    private InOrder inOrder() {
        return Mockito.inOrder(localeResolver, resourceLoader, localizationLoader, messageFormatFactory, inputStream,
                propertyResolver);
    }

    private DefaultLocalizationProvider.Builder createBaseBuilder() {
        return DefaultLocalizationProvider.builder()
                .withLocaleResolver(localeResolver)
                .withResourceLoader(resourceLoader)
                .withLocalizationLoader(localizationLoader)
                .withMessageFormatFactory(messageFormatFactory);
    }
}
