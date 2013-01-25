package com.vityuk.ginger.loader;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.data.MapEntry.entry;

public class PropertiesLocalizationLoaderTest {
    private final LocalizationLoader loader = new PropertiesLocalizationLoader();

    @Test
    public void testLoadWithoutProperties() throws Exception {
        String content = "";

        Map<String, String> resource = load(content);

        assertThat(resource).isEmpty();
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

        Map<String, String> resource = load(content);

        assertThat(resource).isEmpty();
    }

    @Test
    public void testLoadSingleProperty() throws Exception {
        String content = "prop=test-value";

        Map<String, String> resource = load(content);

        assertThat(resource).hasSize(1).contains(entry("prop", "test-value"));
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

        Map<String, String> resource = load(content);

        assertThat(resource).hasSize(5).contains(
                entry("Truth1", "Beauty1"),
                entry("Truth2", "Beauty2"),
                entry("Truth3", "Beauty3"),
                entry("Truth4", "Beauty4"),
                entry("fruits", "apple, banana, pear, cantaloupe, watermelon, kiwi, mango"));
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

        Map<String, String> resource = load(content);

        assertThat(resource).hasSize(2).contains(
                entry("#", "Truth2:Beauty2"),
                entry("fruits#",
                        "apple, banana, pear, #                                  cantaloupe, watermelon, kiwi, mango"));
    }

    private Map<String, String> load(String content) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        return loader.load(inputStream);
    }

}
