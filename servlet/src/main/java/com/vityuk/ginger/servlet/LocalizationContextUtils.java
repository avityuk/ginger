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

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

/**
 * @author Andriy Vityuk
 */
public abstract class LocalizationContextUtils {
    final static String ATTRIBUTE_NAME = Localization.class.getName();

    private LocalizationContextUtils() {
    }

    public static void setLocalization(ServletRequest servletRequest, Localization localization) {
        servletRequest.setAttribute(ATTRIBUTE_NAME, localization);
    }

    public static Localization getLocalization(ServletRequest servletRequest) {
        return (Localization) servletRequest.getAttribute(ATTRIBUTE_NAME);
    }

    public static void setLocalization(ServletContext servletContext, Localization localization) {
        servletContext.setAttribute(ATTRIBUTE_NAME, localization);
    }

    public static Localization getLocalization(ServletContext servletContext) {
        return (Localization) servletContext.getAttribute(ATTRIBUTE_NAME);
    }
}
