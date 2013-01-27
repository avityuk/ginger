package com.vityuk.ginger.loader;

import com.vityuk.ginger.PropertyResolver;
import org.fest.assertions.api.Assertions;
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

        PropertyResolver propertyResolver = load(content);

        assertThat(propertyResolver).isNotNull();
        assertThat(propertyResolver.get("")).isNull();
        assertThat(propertyResolver.getList("")).isNull();
        assertThat(propertyResolver.getMap("")).isNull();
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
        assertThat(propertyResolver.get("")).isNull();
        assertThat(propertyResolver.get("#")).isNull();
    }

    @Test
    public void testLoadSingleProperty() throws Exception {
        String content = "prop=test-value";

        PropertyResolver propertyResolver = load(content);

        assertThat(propertyResolver.get("prop")).isNotNull().isEqualTo("test-value");
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

        assertThat(propertyResolver.get("Truth1")).isNotNull().isEqualTo("Beauty1");
        assertThat(propertyResolver.get("Truth2")).isNotNull().isEqualTo("Beauty2");
        assertThat(propertyResolver.get("Truth3")).isNotNull().isEqualTo("Beauty3");
        assertThat(propertyResolver.get("Truth4")).isNotNull().isEqualTo("Beauty4");
        assertThat(propertyResolver.get("fruits")).isNotNull().isEqualTo("apple, banana, pear, cantaloupe, watermelon, kiwi, mango");
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

        assertThat(propertyResolver.get("#")).isNotNull().isEqualTo("Truth2:Beauty2");
        assertThat(propertyResolver.get("fruits#")).isNotNull().
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

        assertThat(propertyResolver.getList("vegetables")).isNotNull().hasSize(4).
                containsExactly("potato", "squash", "carrot", "beat");

        assertThat(propertyResolver.getList("fruits")).isNotNull().hasSize(7).
                containsExactly("apple", "banana", "pear", "cantaloupe", "watermelon", "kiwi", "mango");
    }


    @Test
    public void testLoadMapProperties() throws Exception {
        String content = "" +
                "weekdays=1:Sunday, 2:Monday, 3:Tuesday, 4:Wednesday, 5:Thursday, 6:Friday, 7:Saturday\n" +
                "colors=red:#FF0000 ,  cyan:#00FFFF, \\\n" +
                "white:#FFFFFF\t, \tblack:#000000";

        PropertyResolver propertyResolver = load(content);

        assertThat(propertyResolver.getMap("weekdays")).isNotNull().hasSize(7).
                contains(entry("1", "Sunday"), entry("2", "Monday"), entry("3", "Tuesday"), entry("4", "Wednesday"),
                        entry("5", "Thursday"), entry("6", "Friday"), entry("7", "Saturday"));

        assertThat(propertyResolver.getMap("colors")).isNotNull().hasSize(4).
                contains(entry("red", "#FF0000"), entry("cyan", "#00FFFF"),
                        entry("white", "#FFFFFF"), entry("black", "#000000"));
    }

    private PropertyResolver load(String content) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        return loader.load(inputStream);
    }

}
