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
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andriy Vityuk
 */
public class MessageTag extends TagSupport implements DynamicAttributes {
    private static final long serialVersionUID = -1307402276661466682L;

    private static final String DEFAULT_HTML_ESCAPE_PARAMETER = "defaultHtmlEscape";
    private static final String DYNAMIC_ATTRIBUTE_ARGUMENT_PREFIX = "arg";

    private String code;
    private Object arguments;
    private Boolean htmlEscape;
    private final Map<String, Object> dynamicAttributes = new HashMap<String, Object>(4);

    @Override
    public int doStartTag() throws JspException {
        ServletRequest request = pageContext.getRequest();
        ServletContext servletContext = pageContext.getServletContext();
        Localization localization = getLocalization(request, servletContext);
        boolean escapeHtml = resolveEscapeHtml(servletContext);

        String message = localization.getMessage(code, resolveParameters());
        String messageOut = escapeHtml ? StringEscapeUtils.escapeHtml4(message) : message;
        try {
            pageContext.getOut().write(String.valueOf(messageOut));
        } catch (IOException e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setArguments(Object arguments) {
        this.arguments = arguments;
    }

    public void setHtmlEscape(Boolean htmlEscape) {
        this.htmlEscape = htmlEscape;
    }

    @Override
    public void setDynamicAttribute(String uri, String name, Object value) throws JspException {
        dynamicAttributes.put(name, value);
    }

    private Localization getLocalization(ServletRequest request, ServletContext servletContext) {
        WebApplicationContext applicationContext = RequestContextUtils.getWebApplicationContext(request, servletContext);
        return applicationContext.getBean(Localization.class);
    }

    private boolean resolveEscapeHtml(ServletContext servletContext) {
        if (htmlEscape == null) {
            return resolveDefaultEscapeHtml(servletContext);
        }
        return htmlEscape;
    }

    private boolean resolveDefaultEscapeHtml(ServletContext servletContext) {
        return Boolean.valueOf(servletContext.getInitParameter(DEFAULT_HTML_ESCAPE_PARAMETER));
    }

    private Object[] resolveParameters() {
        if (arguments == null) {
            return resolveDynamicAttributes();
        }

        return resolveArguments();
    }

    private Object[] resolveDynamicAttributes() {
        Object[] arguments = new Object[dynamicAttributes.size()];

        for (Map.Entry<String, Object> e : dynamicAttributes.entrySet()) {
            String key = e.getKey();
            Object value = e.getValue();

            if (key.startsWith(DYNAMIC_ATTRIBUTE_ARGUMENT_PREFIX)) {
                int argumentIndex = getArgumentIndex(key);
                arguments[argumentIndex] = value;
            }
        }

        return arguments;
    }

    private int getArgumentIndex(String attributeName) {
        String argumentIndex = attributeName.substring(DYNAMIC_ATTRIBUTE_ARGUMENT_PREFIX.length());
        return Integer.parseInt(argumentIndex);
    }

    private Object[] resolveArguments() {
        if (arguments.getClass().isArray()) {
            return (Object[]) arguments;
        }
        return new Object[]{arguments};
    }
}
