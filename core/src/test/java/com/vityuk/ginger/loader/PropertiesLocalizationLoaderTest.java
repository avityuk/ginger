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

package com.vityuk.ginger.loader;

import com.vityuk.ginger.PropertyResolver;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.data.MapEntry.entry;

public class PropertiesLocalizationLoaderTest {
    private final LocalizationLoader loader = new PropertiesLocalizationLoader();

    @Test
    public void testLoadWithoutProperties() throws Exception {
        String content = "";

        PropertyResolver propertyResolver = load(content);

        assertThat(propertyResolver).isNotNull();
        assertThat(propertyResolver.getString("")).isNull();
        assertThat(propertyResolver.getStringList("")).isNull();
        assertThat(propertyResolver.getStringMap("")).isNull();
    }

    @Test
    public void testLoadWithoutPropertiesAndWithComments() throws Exception {
        String content = "" +
                "\n" +
                "# Test comment\t  \n" +
                "#\tLine 2\r" +
                "#Line 3\\\r\n" +
                "\t \n" +
                "\n";

        PropertyResolver propertyResolver = load(content);

        assertThat(propertyResolver).isNotNull();
        assertThat(propertyResolver.getString("")).isNull();
        assertThat(propertyResolver.getString("#")).isNull();
    }

    @Test
    public void testLoadSingleProperty() throws Exception {
        String content = "prop=test-value";

        PropertyResolver propertyResolver = load(content);

        assertThat(propertyResolver.getString("prop")).isNotNull().isEqualTo("test-value");
    }

    @Test
    public void testLoadMultipleProperties() throws Exception {
        String content = "" +
                "Truth1 = Beauty1\n" +
                "       Truth2:Beauty2\r" +
                "Truth3                  :Beauty3\r\n" +
                "Truth4 Beauty4\n" +
                "fruits                           apple, banana, pear, \\\n" +
                "                                  cantaloupe, watermelon, \\\r\n" +
                "                                  kiwi, mango";

        PropertyResolver propertyResolver = load(content);

        assertThat(propertyResolver.getString("Truth1")).isNotNull().isEqualTo("Beauty1");
        assertThat(propertyResolver.getString("Truth2")).isNotNull().isEqualTo("Beauty2");
        assertThat(propertyResolver.getString("Truth3")).isNotNull().isEqualTo("Beauty3");
        assertThat(propertyResolver.getString("Truth4")).isNotNull().isEqualTo("Beauty4");
        assertThat(propertyResolver.getString("fruits")).isNotNull().isEqualTo("apple, banana, pear, cantaloupe, watermelon, kiwi, mango");
    }


    @Test
    public void testLoadMultiplePropertiesAndWithComments() throws Exception {
        String content = "" +
                "#Truth1 = Beauty1\n" +
                " #\t Truth2:Beauty2\r" +
                "!Truth3                  :Beauty3\r\n" +
                "fruits#                           apple, banana, pear, \\\n" +
                "#                                  cantaloupe, watermelon, \\\r\n" +
                "                                  kiwi, mango";

        PropertyResolver propertyResolver = load(content);

        assertThat(propertyResolver.getString("#")).isNotNull().isEqualTo("Truth2:Beauty2");
        assertThat(propertyResolver.getString("fruits#")).isNotNull().
                isEqualTo("apple, banana, pear, #                                  cantaloupe, watermelon, kiwi, mango");
    }


    @Test
    public void testLoadListProperties() throws Exception {
        String content = "" +
                "vegetables=potato,squash,carrot,beat\n" +
                "fruits                           apple,  banana\t, pear, \\\n" +
                "                                  cantaloupe, \twatermelon, \\\r\n" +
                "                                  kiwi , mango";

        PropertyResolver propertyResolver = load(content);

        assertThat(propertyResolver.getStringList("vegetables")).isNotNull().hasSize(4).
                containsExactly("potato", "squash", "carrot", "beat");

        assertThat(propertyResolver.getStringList("fruits")).isNotNull().hasSize(7).
                containsExactly("apple", "banana", "pear", "cantaloupe", "watermelon", "kiwi", "mango");
    }


    @Test
    public void testLoadMapProperties() throws Exception {
        String content = "" +
                "weekdays[1]=Sunday\n" +
                "weekdays[2]=Monday\n" +
                "weekdays[3]=Tuesday\n" +
                "weekdays[4]=Wednesday\n" +
                "weekdays[5]=Thursday\n" +
                "weekdays[6]=Friday\n" +
                "weekdays[7]=Saturday\n" +
                "\n" +
                "colors[red]=#FF0000\n" +
                "colors[cyan]=#00FFFF\n" +
                "colors[white]=#FFFFFF\n" +
                "colors[black]=#000000";

        PropertyResolver propertyResolver = load(content);

        assertThat(propertyResolver.getStringMap("weekdays")).isNotNull().hasSize(7).
                contains(entry("1", "Sunday"), entry("2", "Monday"), entry("3", "Tuesday"), entry("4", "Wednesday"),
                        entry("5", "Thursday"), entry("6", "Friday"), entry("7", "Saturday"));

        assertThat(propertyResolver.getStringMap("colors")).isNotNull().hasSize(4).
                contains(entry("red", "#FF0000"), entry("cyan", "#00FFFF"),
                        entry("white", "#FFFFFF"), entry("black", "#000000"));
    }

    private PropertyResolver load(String content) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        return loader.load(inputStream);
    }

}
