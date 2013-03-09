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

import com.google.common.io.Closeables;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.fest.assertions.api.Assertions.assertThat;

public class ClasspathResourceLoaderTest {
    private ResourceLoader loader = new ClasspathResourceLoader();


    @Test
    public void testIsSupportedWithSchema() {
        boolean supported = loader.isSupported("classpath:test");

        assertThat(supported).isTrue();
    }

    @Test
    public void testIsSupportedWithEmptyLocation() {
        boolean supported = loader.isSupported("");

        assertThat(supported).isFalse();
    }


    @Test
    public void testIsSupportedWithoutSchema() {
        boolean supported = loader.isSupported("test");

        assertThat(supported).isFalse();
    }


    @Test
    public void testIsSupportedWithInvalidLocation() {
        boolean supported = loader.isSupported("classpathtest");

        assertThat(supported).isFalse();
    }

    @Test
    public void testIsSupportedWithInvalidSchema() {
        boolean supported = loader.isSupported("http:test");

        assertThat(supported).isFalse();
    }

    @Test(expected = NullPointerException.class)
    public void testIsSupportedWithNullLocation() {
        loader.isSupported(null);
    }

    @Test
    public void testOpenWithExistingLocation() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = loader.openStream("classpath:" + ClasspathResourceLoaderTest.class.getCanonicalName()
                    .replace('.', '/') + ".class");

            assertThat(inputStream).isNotNull();
        } finally {
            Closeables.closeQuietly(inputStream);
        }
    }


    @Test
    public void testOpenWithNonExistentLocation() throws IOException {
        InputStream inputStream = loader.openStream("classpath:test43243958438");

        assertThat(inputStream).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithEmptyLocation() throws IOException {
        loader.openStream("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithoutSchema() throws IOException {
        loader.openStream("test");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithInvalidLocation() throws IOException {
        loader.openStream("classpathtest");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithInvalidSchema() throws IOException {
        loader.openStream("http:test");
    }

    @Test(expected = NullPointerException.class)
    public void testOpenWithNullLocation() throws IOException {
        loader.openStream(null);
    }


}
