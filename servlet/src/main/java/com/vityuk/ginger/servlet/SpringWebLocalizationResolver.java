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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

/**
 * @author Andriy Vityuk
 */
public class SpringWebLocalizationResolver implements ServletLocalizationResolver {
    @Override
    public Localization resolve(ServletRequest servletRequest, ServletContext servletContext) {
        WebApplicationContext applicationContext =
                RequestContextUtils.getWebApplicationContext(servletRequest, servletContext);
        Localization localization = applicationContext.getBean(Localization.class);

        if (localization == null) {
            String message = "Unable to find " + Localization.class.getName() + " bean in Spring context";
            throw new IllegalStateException(message);
        }
        return localization;
    }
}
