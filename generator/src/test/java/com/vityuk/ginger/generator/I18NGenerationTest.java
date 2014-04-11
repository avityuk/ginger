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
