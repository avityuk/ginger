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

package com.vityuk.ginger.generator;

import com.sun.codemodel.*;
import com.vityuk.ginger.DefaultLocalization;
import com.vityuk.ginger.Localizable;
import com.vityuk.ginger.PropertyResolver;
import com.vityuk.ginger.loader.PropertiesLocalizationLoader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class InterfaceGenerator {
    private File resourceFile;
    private File sourcesDirectory;
    private String className;

    public InterfaceGenerator() {
    }

    public void setup(String className, File resourceFile, File sourcesDirectory) throws FileNotFoundException {
        this.className = className;
        this.resourceFile = resourceFile;
        this.sourcesDirectory = sourcesDirectory;
    }

    private void generateClass(JCodeModel codeModel) throws Exception {
        JDefinedClass definedClass = codeModel._class(className, ClassType.INTERFACE);
        definedClass._implements(Localizable.class);
        generateFromPropertiesFile(definedClass);
    }

    private void generateFromPropertiesFile(JDefinedClass definedClass) throws IOException {
        InputStream propStream = new FileInputStream(resourceFile);
        PropertiesLocalizationLoader localizationLoader = new PropertiesLocalizationLoader();
        PropertyResolver propertyResolver = localizationLoader.load(propStream);
        Set<String> keys = propertyResolver.getKeys();
        for (String key: keys) {
            generateMethod(definedClass, key, propertyResolver.getString(key));
        }
    }

    private void generateMethod(JDefinedClass definedClass, String keyName, String value) {
        /* Build method */
        String methodName = createMethodNameFromKey(keyName);
        Class<?> returnClazz = String.class;
        JType returnType = definedClass.owner()._ref(returnClazz);
        returnType = returnType.unboxify();
        JMethod method = definedClass.method(JMod.PUBLIC, returnType, methodName);

        /* build args */
        MessageFormat messageFormat = new MessageFormat(value);
        Format[] formats = messageFormat.getFormats();
        for (int i = 0; i < formats.length; ++i) {
            Format format = formats[i];
            Class<?> argClazz;
            if (format instanceof NumberFormat) {
                argClazz = Integer.class;
            } else {
                argClazz = String.class;
            }
            JType argType = definedClass.owner()._ref(argClazz);
            argType = argType.unboxify();
            method.param(argType, "arg" + Integer.toString(i));
        }
    }

    private static String createMethodNameFromKey(String keyName) {
        String[] words = StringUtils.splitByWholeSeparator(keyName, ".");
        StringBuilder keyBuilder = new StringBuilder(keyName.length() - words.length + 1);

        boolean first = true;
        for (String word : words) {
            word = word.toLowerCase();
            if (first) {
                first = false;
            } else {
                word = StringUtils.capitalize(word);
            }
            keyBuilder.append(word);
        }

        return keyBuilder.toString();
    }

    public void generate() throws Exception {
        generate(System.out);
    }

    public void generate(PrintStream ps) throws Exception {
        JCodeModel codeModel = new JCodeModel();
        generateClass(codeModel);
        codeModel.build(sourcesDirectory, ps);
    }
}
