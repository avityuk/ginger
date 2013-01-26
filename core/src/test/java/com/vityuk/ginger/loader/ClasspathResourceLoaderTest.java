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
        boolean supported = loader.isSupported("classpath:/test");

        assertThat(supported).isTrue();
    }

    @Test
    public void testIsSupportedWithEmptyLocation() {
        boolean supported = loader.isSupported("");

        assertThat(supported).isFalse();
    }


    @Test
    public void testIsSupportedWithoutSchema() {
        boolean supported = loader.isSupported("/test");

        assertThat(supported).isFalse();
    }


    @Test
    public void testIsSupportedWithInvalidLocation() {
        boolean supported = loader.isSupported("classpath/test");

        assertThat(supported).isFalse();
    }

    @Test
    public void testIsSupportedWithInvalidSchema() {
        boolean supported = loader.isSupported("http:/test");

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


    @Test(expected = IOException.class)
    public void testOpenWithNonExistentLocation() throws IOException {
        loader.openStream("classpath:/test43243958438");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithEmptyLocation() throws IOException {
        loader.openStream("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithoutSchema() throws IOException {
        loader.openStream("/test");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithInvalidLocation() throws IOException {
        loader.openStream("classpath/test");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithInvalidSchema() throws IOException {
        loader.openStream("http:/test");
    }

    @Test(expected = NullPointerException.class)
    public void testOpenWithNullLocation() throws IOException {
        loader.openStream(null);
    }


}
