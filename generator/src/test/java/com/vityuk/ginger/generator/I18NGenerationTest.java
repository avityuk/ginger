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

import org.junit.Test;

import java.io.File;

public class I18NGenerationTest {

    @Test
    public void test() throws Exception {
        String className = "GeneralConstants";
        File resource = new File(I18NGenerationTest.class.getResource(className + ".properties").toURI());

        String fullClassName = "com.test.i18n." + className;
        File directory = File.createTempFile("test", "");
        directory.delete();
        directory.mkdir();
        System.out.println("Generated directory: " + directory);
        InterfaceGenerator ig = new InterfaceGenerator();
        ig.setup(fullClassName, resource, directory);
        ig.generate();
    }
}
