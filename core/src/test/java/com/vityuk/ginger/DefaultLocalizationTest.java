package com.vityuk.ginger;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultLocalizationTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Localization localization;

    @Mock
    private LocalizationProvider localizationProvider;

    @Before
    public void setUp() throws Exception {
        localization = new DefaultLocalization(localizationProvider);
    }

    @Test
    public void testStringConstant() {
        String testValue = "test value";
        when(localizationProvider.getString("test.string")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        String result = constants.testString();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testStringConstantWithNullValue() {
        when(localizationProvider.getString("test.string")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        String result = constants.testString();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testStringConstantWithException() {
        when(localizationProvider.getString("test.string")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testString();
    }

    @Test
    public void testBooleanConstant() {
        Boolean testValue = Boolean.TRUE;
        when(localizationProvider.getBoolean("test.boolean")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Boolean result = constants.testBoolean();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testBooleanConstantWithNullValue() {
        when(localizationProvider.getBoolean("test.boolean")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Boolean result = constants.testBoolean();

        assertThat(result).isNull();
    }


    @Test(expected = RuntimeException.class)
    public void testBooleanConstantWithException() {
        when(localizationProvider.getBoolean("test.boolean")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testBoolean();
    }

    @Test
    public void testIntegerConstant() {
        Integer testValue = Integer.MAX_VALUE;
        when(localizationProvider.getInteger("test.int")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Integer result = constants.testInt();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testIntegerConstantWithNullValue() {
        when(localizationProvider.getInteger("test.int")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Integer result = constants.testInt();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testIntegerConstantWithException() {
        when(localizationProvider.getInteger("test.int")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testInt();
    }

    @Test
    public void testLongConstant() {
        Long testValue = Long.MIN_VALUE;
        when(localizationProvider.getLong("test.long")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Long result = constants.testLong();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testLongConstantWithNullValue() {
        when(localizationProvider.getLong("test.long")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Long result = constants.testLong();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testLongConstantWithException() {
        when(localizationProvider.getLong("test.long")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testLong();
    }

    @Test
    public void testFloatConstant() {
        Float testValue = Float.MAX_VALUE;
        when(localizationProvider.getFloat("test.float")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Float result = constants.testFloat();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testFloatConstantWithNullValue() {
        when(localizationProvider.getFloat("test.float")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Float result = constants.testFloat();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testFloatConstantWithException() {
        when(localizationProvider.getFloat("test.float")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testFloat();
    }

    @Test
    public void testDoubleConstant() {
        Double testValue = Double.MIN_VALUE;
        when(localizationProvider.getDouble("test.double")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Double result = constants.testDouble();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testDoubleConstantWithNullValue() {
        when(localizationProvider.getDouble("test.double")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Double result = constants.testDouble();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testDoubleConstantWithException() {
        when(localizationProvider.getDouble("test.double")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testDouble();
    }

    @Test
    public void testStringListConstant() {
        List<String> testValue = Arrays.asList("a", "b", "c");
        when(localizationProvider.getStringList("test.string.list")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        List<String> result = constants.testStringList();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testStringListConstantWithNullValue() {
        when(localizationProvider.getStringList("test.string.list")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        List<String> result = constants.testStringList();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testStringListConstantWithException() {
        when(localizationProvider.getStringList("test.string.list")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testStringList();
    }

    @Test
    public void testStringMapConstant() {
        Map<String, String> testValue = ImmutableMap.of("a", "1", "b", "2");
        when(localizationProvider.getStringMap("test.string.map")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Map<String, String> result = constants.testStringMap();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testStringMapConstantWithNullValue() {
        when(localizationProvider.getStringMap("test.string.map")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Map<String, String> result = constants.testStringMap();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testStringMapConstantWithException() {
        when(localizationProvider.getStringMap("test.string.map")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testStringMap();
    }

    @Test
    public void testUnsupportedConstantWithCharacterType() {
        thrown.expect(UnsupportedTypeException.class);
        String expectedMessage = "Unsupported return type: java.lang.Character for method: testChar in " +
                TestConstantsWithChar.class.getName();
        thrown.expectMessage(expectedMessage);

        localization.getLocalizable(TestConstantsWithChar.class);
    }


    @Test
    public void testUnsupportedConstantWithPrimitiveType() {
        thrown.expect(UnsupportedTypeException.class);
        String expectedMessage = "Unsupported return type: boolean for method: testBoolean in " +
                TestConstantsWithPrimitiveBoolean.class.getName();
        thrown.expectMessage(expectedMessage);

        localization.getLocalizable(TestConstantsWithPrimitiveBoolean.class);
    }

    interface TestConstants extends Localizable {
        String testString();

        Boolean testBoolean();

        Integer testInt();

        Long testLong();

        Float testFloat();

        Double testDouble();

        List<String> testStringList();

        Map<String, String> testStringMap();
    }

    interface TestConstantsWithChar extends Localizable {
        Character testChar();
    }

    interface TestConstantsWithPrimitiveBoolean extends Localizable {
        boolean testBoolean();
    }
}
