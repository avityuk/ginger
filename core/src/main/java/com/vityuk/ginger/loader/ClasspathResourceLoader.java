package com.vityuk.ginger.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClasspathResourceLoader extends AbstractResourceLoader {
    private static final String SCHEMA = "classpath";

    private final ClassLoader classLoader;


    public ClasspathResourceLoader(ClassLoader classLoader) {
        super(SCHEMA);
        this.classLoader = classLoader;
    }

    public ClasspathResourceLoader() {
        this(getDefaultClassLoader());
    }

    @Override
    protected InputStream openResource(String path) throws IOException {
        InputStream inputStream = classLoader.getResourceAsStream(path);
        if (inputStream == null) {
            throw new FileNotFoundException("Unable to find classpath resource: '" + path + "'");
        }
        return inputStream;
    }

    private static ClassLoader getDefaultClassLoader() {
        ClassLoader contextClassLoader = getContextClassLoader();
        if (contextClassLoader != null) {
            return contextClassLoader;
        }

        return ClasspathResourceLoader.class.getClassLoader();
    }

    private static ClassLoader getContextClassLoader() {
        try {
            return Thread.currentThread().getContextClassLoader();
        } catch (SecurityException e) {
            // No access to context class loader
            return null;
        }
    }
}
