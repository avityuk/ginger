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

package com.vityuk.ginger.proxy;

import com.vityuk.ginger.Localizable;
import com.vityuk.ginger.provider.LocalizationProvider;

public class ProxyBuilderFactory {

    private static final String PROXY_BUILDERS [] = {
            "com.vityuk.ginger.proxy.CgiProxyBuilder",
            "com.vityuk.ginger.proxy.ReflectProxyBuilder"
    };

    private static ProxyBuilder proxyBuilder;

    public static <T extends Localizable> T createProxy(Class<T> object, LocalizationProvider localizationProvider) {
        return getProxyBuilder().createProxy(object, localizationProvider);
    }

    public static ProxyBuilder getProxyBuilder() {
        if (proxyBuilder == null) {
            Throwable lastIgnore = null;
            for (String implementation: PROXY_BUILDERS) {
                try {
                    Class<?> aClass = Class.forName(implementation);
                    proxyBuilder = (ProxyBuilder) aClass.newInstance();
                    return proxyBuilder;
                } catch (LinkageError ex) {
                    lastIgnore = ex;
                } catch (Exception ex) {
                    lastIgnore = ex;
                }
            }
            throw new RuntimeException("Can't find any implementation for beans", lastIgnore);
        }
        return proxyBuilder;
    }
}
