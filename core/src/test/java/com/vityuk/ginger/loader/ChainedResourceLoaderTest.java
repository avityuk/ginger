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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChainedResourceLoaderTest {
    @Mock
    ResourceLoader resourceLoader1;

    @Mock
    ResourceLoader resourceLoader2;

    @Mock
    ResourceLoader resourceLoader3;

    private ChainedResourceLoader resourceLoader;

    @Before
    public void setUp() {
        resourceLoader = new ChainedResourceLoader(Arrays.asList(resourceLoader1, resourceLoader2, resourceLoader3));
    }

    @Test
    public void testIsSupportedWithFirstSupported() throws Exception {
        String location = "schema1:/test/resource";
        when(resourceLoader1.isSupported(location)).thenReturn(true);

        boolean result = resourceLoader.isSupported(location);

        assertThat(result).isTrue();
        InOrder inOrder = resourcesInOrder();
        inOrder.verify(resourceLoader1).isSupported(location);
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void testIsSupportedWithLastSupported() throws Exception {
        String location = "schema3:/test/resource";
        when(resourceLoader1.isSupported(location)).thenReturn(false);
        when(resourceLoader2.isSupported(location)).thenReturn(false);
        when(resourceLoader3.isSupported(location)).thenReturn(true);

        boolean result = resourceLoader.isSupported(location);

        assertThat(result).isTrue();
        InOrder inOrder = resourcesInOrder();
        inOrder.verify(resourceLoader1).isSupported(location);
        inOrder.verify(resourceLoader2).isSupported(location);
        inOrder.verify(resourceLoader3).isSupported(location);
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void testIsSupportedWithNoneSupported() throws Exception {
        String location = "schema4:/test/resource";
        when(resourceLoader1.isSupported(location)).thenReturn(false);
        when(resourceLoader2.isSupported(location)).thenReturn(false);
        when(resourceLoader3.isSupported(location)).thenReturn(false);

        boolean result = resourceLoader.isSupported(location);

        assertThat(result).isFalse();
        InOrder inOrder = resourcesInOrder();
        inOrder.verify(resourceLoader1).isSupported(location);
        inOrder.verify(resourceLoader2).isSupported(location);
        inOrder.verify(resourceLoader3).isSupported(location);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testOpenStreamWithFirstSupported() throws Exception {
        String location = "schema1:/test/resource";
        InputStream inputStream = mock(InputStream.class);
        when(resourceLoader1.isSupported(location)).thenReturn(true);
        when(resourceLoader1.openStream(location)).thenReturn(inputStream);

        InputStream result = resourceLoader.openStream(location);

        assertThat(result).isNotNull().isSameAs(inputStream);
        InOrder inOrder = resourcesInOrder();
        inOrder.verify(resourceLoader1).isSupported(location);
        inOrder.verify(resourceLoader1).openStream(location);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testOpenStreamWithLastSupported() throws Exception {
        String location = "schema3:/test/resource";
        InputStream inputStream = mock(InputStream.class);
        when(resourceLoader1.isSupported(location)).thenReturn(false);
        when(resourceLoader2.isSupported(location)).thenReturn(false);
        when(resourceLoader3.isSupported(location)).thenReturn(true);
        when(resourceLoader3.openStream(location)).thenReturn(inputStream);

        InputStream result = resourceLoader.openStream(location);

        assertThat(result).isNotNull().isSameAs(inputStream);
        InOrder inOrder = resourcesInOrder();
        inOrder.verify(resourceLoader1).isSupported(location);
        inOrder.verify(resourceLoader2).isSupported(location);
        inOrder.verify(resourceLoader3).isSupported(location);
        inOrder.verify(resourceLoader3).openStream(location);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testOpenStreamWithNoneSupported() throws Exception {
        String location = "schema4:/test/resource";
        when(resourceLoader1.isSupported(location)).thenReturn(false);
        when(resourceLoader2.isSupported(location)).thenReturn(false);
        when(resourceLoader3.isSupported(location)).thenReturn(false);

        try {
            resourceLoader.openStream(location);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining("Unsupported");
        }

        InOrder inOrder = resourcesInOrder();
        inOrder.verify(resourceLoader1).isSupported(location);
        inOrder.verify(resourceLoader2).isSupported(location);
        inOrder.verify(resourceLoader3).isSupported(location);
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void testOpenStreamWithFirstSupportedReturnsNull() throws Exception {
        String location = "schema1:/test/resource";
        when(resourceLoader1.isSupported(location)).thenReturn(true);
        when(resourceLoader1.openStream(location)).thenReturn(null);

        InputStream result = resourceLoader.openStream(location);

        assertThat(result).isNull();
        InOrder inOrder = resourcesInOrder();
        inOrder.verify(resourceLoader1).isSupported(location);
        inOrder.verify(resourceLoader1).openStream(location);
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void testOpenStreamWithFirstSupportedThrowsIoException() throws Exception {
        String location = "schema1:/test/resource";
        IOException exception = new IOException();
        when(resourceLoader1.isSupported(location)).thenReturn(true);
        when(resourceLoader1.openStream(location)).thenThrow(exception);

        try {
            resourceLoader.openStream(location);
        } catch (IOException e) {
            assertThat(e).isEqualTo(exception);
        }

        InOrder inOrder = resourcesInOrder();
        inOrder.verify(resourceLoader1).isSupported(location);
        inOrder.verify(resourceLoader1).openStream(location);
        inOrder.verifyNoMoreInteractions();
    }

    private InOrder resourcesInOrder() {
        return inOrder(resourceLoader1, resourceLoader2, resourceLoader3);
    }
}
