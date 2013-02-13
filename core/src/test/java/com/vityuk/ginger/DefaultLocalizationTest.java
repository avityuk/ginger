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

package com.vityuk.ginger;

import com.google.common.collect.ImmutableMap;
import com.vityuk.ginger.provider.LocalizationProvider;
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
import static org.mockito.Mockito.verifyZeroInteractions;
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
    public void testConstantWithInvalidReturnTypeCharacter() {
        thrown.expect(InvalidReturnTypeException.class);
        String expectedMessage = "Invalid return type: java.lang.Character for method: testChar in " +
                TestConstantsWithChar.class.getName();
        thrown.expectMessage(expectedMessage);

        localization.getLocalizable(TestConstantsWithChar.class);

        verifyZeroInteractions(localizationProvider);
    }


    @Test
    public void testConstantWithInvalidReturnTypePrimitive() {
        thrown.expect(InvalidReturnTypeException.class);
        String expectedMessage = "Invalid return type: boolean for method: testBoolean in " +
                TestConstantsWithPrimitiveBoolean.class.getName();
        thrown.expectMessage(expectedMessage);

        localization.getLocalizable(TestConstantsWithPrimitiveBoolean.class);

        verifyZeroInteractions(localizationProvider);
    }

    @Test
    public void testSingleStringArgumentMessage() {
        String arg = "test arg";
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.string.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleStringArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleBooleanArgumentMessage() {
        Boolean arg = Boolean.TRUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.boolean.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleBooleanArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleIntegerArgumentMessage() {
        Integer arg = Integer.MIN_VALUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.integer.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleIntegerArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleLongArgumentMessage() {
        Long arg = Long.MAX_VALUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.long.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleLongArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleFloatArgumentMessage() {
        Float arg = Float.MAX_VALUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.float.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleFloatArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleDoubleArgumentMessage() {
        Double arg = Double.MIN_VALUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.double.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleDoubleArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSinglePrimitiveArgumentMessage() {
        int arg = Integer.MIN_VALUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.primitive.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singlePrimitiveArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testMultipleArgumentsMessage() {
        String arg0 = "test";
        boolean arg1 = false;
        Integer arg2 = 547;
        long arg3 = 23534534;
        Float arg4 = 423543.0533F;
        double arg5 = -1.0432522;

        String message = "Test " + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + ", " + arg4 + ", " + arg5;
        when(localizationProvider.getMessage("multiple.arguments",
                arg0, arg1, arg2, arg3, arg4, arg5)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.multipleArguments(arg0, arg1, arg2, arg3, arg4, arg5);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleSelectorArgumentMessage() {
        String selectorValue = "female";
        String message = "She likes cats";
        when(localizationProvider.getSelectedMessage("single.selector.argument", selectorValue)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleSelectorArgument(new Selector(selectorValue));

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testMultipleSelectorArgumentsMessage() {
        String arg0 = "test";
        boolean arg1 = false;
        Integer arg2 = 547;
        String selector = "male";
        long arg4 = 23534534;
        Float arg5 = 423543.0533F;
        double arg6 = -1.0432522;

        String message = "He likes cats";
        when(localizationProvider.getSelectedMessage("multiple.selector.arguments",
                selector, arg0, arg1, arg2, arg4, arg5, arg6)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.multipleSelectorArguments(arg0, arg1, arg2, selector, arg4, arg5, arg6);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testMessageWithInvalidSelectorParameterType() {
        thrown.expect(InvalidParameterTypeException.class);
        String expectedMessage = "Invalid parameter type: java.lang.Boolean for method: message in " +
                TestMessagesWithIncorrectSelectorParameterType.class.getName();
        thrown.expectMessage(expectedMessage);

        try {
            localization.getLocalizable(TestMessagesWithIncorrectSelectorParameterType.class);
        } finally {
            verifyZeroInteractions(localizationProvider);
        }
    }
    @Test
    public void testSinglePluralCountArgumentMessage() {
        int count = 101;
        String message = "101 horses";
        when(localizationProvider.getPluralMessage("single.plural.count.argument", count)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singlePluralCountArgument(count);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testMultiplePluralCountArgumentsMessage() {
        String arg0 = "test";
        boolean arg1 = false;
        Integer arg2 = 547;
        int pluralCount = 1;
        long arg4 = 23534534;
        Float arg5 = 423543.0533F;
        double arg6 = -1.0432522;

        String message = "One cat";
        when(localizationProvider.getPluralMessage("multiple.plural.count.arguments",
                pluralCount, arg0, arg1, arg2, arg4, arg5, arg6)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.multiplePluralCountArguments(arg0, arg1, arg2, pluralCount, arg4, arg5, arg6);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testMessageWithInvalidPluralCountParameterType() {
        thrown.expect(InvalidParameterTypeException.class);
        String expectedMessage = "Invalid parameter type: long for method: message in " +
                TestMessagesWithIncorrectPluralCountParameterType.class.getName();
        thrown.expectMessage(expectedMessage);

        try {
            localization.getLocalizable(TestMessagesWithIncorrectPluralCountParameterType.class);
        } finally {
            verifyZeroInteractions(localizationProvider);
        }
    }

    @Test
    public void testMessageWithNullMessage() {
        String arg = "test";
        when(localizationProvider.getMessage("single.string.argument", arg)).thenReturn(null);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleStringArgument(arg);

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testMessageWithException() {
        String arg = "test";
        when(localizationProvider.getMessage("single.string.argument", arg)).thenThrow(new RuntimeException());
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        messages.singleStringArgument(arg);
    }

    @Test
    public void testMessageWithInvalidReturnType() {
        thrown.expect(InvalidReturnTypeException.class);
        String expectedMessage = "Invalid return type: java.lang.Boolean for method: message in " +
                TestMessagesWithIncorrectReturnType.class.getName();
        thrown.expectMessage(expectedMessage);

        try {
            localization.getLocalizable(TestMessagesWithIncorrectReturnType.class);
        } finally {
            verifyZeroInteractions(localizationProvider);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocalizableWithNull() {
        localization.getLocalizable(null);
    }

    @Test
    public void testGetLocalizableWithClass() {
        thrown.expect(IllegalArgumentException.class);
        String expectedMessage = "Parameter 'localizable' must be an interface";
        thrown.expectMessage(expectedMessage);

        localization.getLocalizable(TestClass.class);
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testGetLocalizableWithInvalidInterface() {
        thrown.expect(IllegalArgumentException.class);
        String expectedMessage = TestInvalidInterface.class.getName() + " must extend " + Localizable.class.getName();
        thrown.expectMessage(expectedMessage);

        Class<?> localizableClass = TestInvalidInterface.class;
        localization.getLocalizable((Class<Localizable>) localizableClass);
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

    interface TestMessages extends Localizable {
        String singleStringArgument(String arg);

        String singleBooleanArgument(Boolean arg);

        String singleIntegerArgument(Integer arg);

        String singleLongArgument(Long arg);

        String singleFloatArgument(Float arg);

        String singleDoubleArgument(Double arg);

        String singlePrimitiveArgument(int arg);

        String multipleArguments(String arg0, boolean arg1, Integer arg2, long arg3, Float arg4, double arg5);

        String singleSelectorArgument(@Select Selector arg);

        String multipleSelectorArguments(String arg0, boolean arg1, Integer arg2, @Select String arg3,
                                         long arg4, Float arg5, double arg6);

        String singlePluralCountArgument(@PluralCount int arg);

        String multiplePluralCountArguments(String arg0, boolean arg1, Integer arg2, @PluralCount Integer arg3,
                                            long arg4, Float arg5, double arg6);
    }

    interface TestMessagesWithIncorrectReturnType extends Localizable {
        Boolean message(Boolean arg);
    }

    interface TestMessagesWithIncorrectPluralCountParameterType extends Localizable {
        String message(@PluralCount long arg);
    }


    interface TestMessagesWithIncorrectSelectorParameterType extends Localizable {
        String message(@Select Boolean arg);
    }

    static class TestClass implements Localizable {
    }

    interface TestInvalidInterface {
    }

    private static class Selector {
        private final String value;

        public Selector(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}
