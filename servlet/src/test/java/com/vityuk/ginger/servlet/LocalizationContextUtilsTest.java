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

package com.vityuk.ginger.servlet;

import com.vityuk.ginger.Localization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import static com.vityuk.ginger.servlet.LocalizationContextUtils.ATTRIBUTE_NAME;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Andriy Vityuk
 */
@RunWith(MockitoJUnitRunner.class)
public class LocalizationContextUtilsTest {
    @Mock
    private Localization localization;

    @Test
    public void testSetLocalizationWithServletRequest() throws Exception {
        ServletRequest servletRequest = mock(ServletRequest.class);

        LocalizationContextUtils.setLocalization(servletRequest, localization);

        verify(servletRequest).setAttribute(ATTRIBUTE_NAME, localization);
    }

    @Test
    public void testGetLocalizationWithServletRequest() throws Exception {
        ServletRequest servletRequest = mock(ServletRequest.class);
        when(servletRequest.getAttribute(ATTRIBUTE_NAME)).thenReturn(localization);

        Localization result = LocalizationContextUtils.getLocalization(servletRequest);

        assertThat(result).isNotNull().isEqualTo(localization);
    }

    @Test
    public void testSetLocalizationWithServletContext() throws Exception {
        ServletContext servletContext = mock(ServletContext.class);

        LocalizationContextUtils.setLocalization(servletContext, localization);

        verify(servletContext).setAttribute(ATTRIBUTE_NAME, localization);
    }

    @Test
    public void testGetLocalizationWithServletContext() throws Exception {
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getAttribute(ATTRIBUTE_NAME)).thenReturn(localization);

        Localization result = LocalizationContextUtils.getLocalization(servletContext);

        assertThat(result).isNotNull().isEqualTo(localization);
    }
}
