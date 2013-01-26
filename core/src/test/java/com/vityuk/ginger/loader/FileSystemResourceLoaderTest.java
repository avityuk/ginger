package com.vityuk.ginger.loader;

import com.google.common.io.Closeables;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.fest.util.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.fest.assertions.api.Assertions.assertThat;

public class FileSystemResourceLoaderTest {
    private ResourceLoader loader = new FileSystemResourceLoader();


    @Test
    public void testIsSupportedWithSchema() {
        boolean supported = loader.isSupported("file:/tmp/test");

        assertThat(supported).isTrue();
    }

    @Test
    public void testIsSupportedWithEmptyLocation() {
        boolean supported = loader.isSupported("");

        assertThat(supported).isFalse();
    }


    @Test
    public void testIsSupportedWithoutSchema() {
        boolean supported = loader.isSupported("/tmp/test");

        assertThat(supported).isFalse();
    }


    @Test
    public void testIsSupportedWithInvalidLocation() {
        boolean supported = loader.isSupported("file/tmp/test");

        assertThat(supported).isFalse();
    }

    @Test
    public void testIsSupportedWithInvalidSchema() {
        boolean supported = loader.isSupported("http:/tmp/test");

        assertThat(supported).isFalse();
    }

    @Test(expected = NullPointerException.class)
    public void testIsSupportedWithNullLocation() {
        loader.isSupported(null);
    }

    @Test
    public void testOpenWithExistingLocation() throws IOException {
        String data = "test data";

        File file = File.createTempFile(FileSystemResourceLoaderTest.class.getSimpleName(), ".data");
        InputStream inputStream = null;
        try {
            FileUtils.write(file, data);

            inputStream = loader.openStream("file:" + file.getAbsolutePath());

            String actualData = IOUtils.toString(inputStream);
            assertThat(actualData).isEqualTo(data);
        } finally {
            Closeables.closeQuietly(inputStream);
            file.delete();
        }
    }


    @Test(expected = IOException.class)
    public void testOpenWithNonExistentLocation() throws IOException {
        loader.openStream("file:/test43243958438");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithEmptyLocation() throws IOException {
        loader.openStream("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithoutSchema() throws IOException {
        loader.openStream("/tmp/test");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithInvalidLocation() throws IOException {
        loader.openStream("file/tmp/test");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithInvalidSchema() throws IOException {
        loader.openStream("http:/tmp/test");
    }

    @Test(expected = NullPointerException.class)
    public void testOpenWithNullLocation() throws IOException {
        loader.openStream(null);
    }


}
