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

package com.vityuk.ginger.servlet.tags;

import com.vityuk.ginger.Localization;
import com.vityuk.ginger.servlet.ServletLocalizationResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Andriy Vityuk
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageTagTest {
    private MessageTag tag;

    @Mock
    private PageContext pageContext;

    @Mock
    private ServletRequest servletRequest;

    @Mock
    private ServletContext servletContext;

    @Mock
    private JspWriter jspWriter;

    @Mock
    private ServletLocalizationResolver servletLocalizationResolver;

    @Mock
    private Localization localization;

    @Before
    public void setUp() throws Exception {
        MessageTag.setServletLocalizationResolver(servletLocalizationResolver);

        tag = new MessageTag();
        tag.setPageContext(pageContext);

        when(pageContext.getRequest()).thenReturn(servletRequest);
        when(pageContext.getServletContext()).thenReturn(servletContext);
        when(pageContext.getOut()).thenReturn(jspWriter);
        when(servletLocalizationResolver.resolve(servletRequest, servletContext)).thenReturn(localization);
    }

    @Test
    public void testWithNoArguments() throws Exception {
        String code = "test.code";
        String message = "This is test message";
        tag.setCode(code);
        when(localization.getMessage(code)).thenReturn(message);

        renderTag();

        verify(jspWriter).write(message);
    }

    @Test
    public void testWithArguments() throws Exception {
        String code = "hello.code";
        String message = "Hello, {0}. Now is {1}";
        String arg0 = "World";
        Date arg1 = new Date();

        tag.setCode(code);
        tag.setArguments(new Object[]{arg0, arg1});
        when(localization.getMessage(code, arg0, arg1)).thenReturn(message);

        renderTag();

        verify(jspWriter).write(message);
    }

    @Test
    public void testWithDynamicAttributes() throws Exception {
        String code = "hello.code";
        String message = "Hello, {0}. Now is {1}";
        String arg0 = "World";
        Date arg1 = new Date();

        tag.setCode(code);
        tag.setDynamicAttribute(null, "arg1", arg1);
        tag.setDynamicAttribute(null, "arg0", arg0);
        when(localization.getMessage(code, arg0, arg1)).thenReturn(message);

        renderTag();

        verify(jspWriter).write(message);
    }


    @Test
    public void testWithNullMessage() throws Exception {
        String code = "test.code";
        tag.setCode(code);
        when(localization.getMessage(code)).thenReturn(null);

        renderTag();

        verify(jspWriter).write("null");
    }

    @Test
    public void testWithSelector() throws Exception {
        String code = "hello.code";
        String message = "He is {0}. Now is {1}";
        String selector = "male";
        String arg0 = "World";
        Date arg1 = new Date();

        tag.setCode(code);
        tag.setSelector(selector);
        tag.setArguments(new Object[]{arg0, arg1});
        when(localization.getSelectedMessage(code, selector, arg0, arg1)).thenReturn(message);

        renderTag();

        verify(jspWriter).write(message);
    }

    @Test
    public void testWithCount() throws Exception {
        String code = "hello.code";
        String message = "There are {0} ingredients. Now is {1}";
        int count = 108;
        Date arg1 = new Date();

        tag.setCode(code);
        tag.setCount(count);
        tag.setArguments(new Object[]{arg1});
        when(localization.getPluralMessage(code, count, arg1)).thenReturn(message);

        renderTag();

        verify(jspWriter).write(message);
    }

    private void renderTag() throws JspException {
        tag.doStartTag();
        tag.doEndTag();
    }
}
