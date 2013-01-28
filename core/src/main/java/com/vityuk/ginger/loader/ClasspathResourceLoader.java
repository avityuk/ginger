package com.vityuk.ginger.loader;

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
    protected InputStream openResource(String path) {
        return classLoader.getResourceAsStream(path);
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
