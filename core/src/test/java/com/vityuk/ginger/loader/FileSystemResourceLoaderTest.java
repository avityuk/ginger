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

import com.vityuk.ginger.util.MiscUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
            MiscUtils.closeQuietly(inputStream);
            file.delete();
        }
    }


    @Test
    public void testOpenWithNonExistentLocation() throws IOException {
        InputStream inputStream = loader.openStream("file:/test43243958438");

        assertThat(inputStream).isNull();
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
