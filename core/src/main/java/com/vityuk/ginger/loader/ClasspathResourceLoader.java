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

import java.io.InputStream;

/**
 * @author Andriy Vityuk
 */
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
