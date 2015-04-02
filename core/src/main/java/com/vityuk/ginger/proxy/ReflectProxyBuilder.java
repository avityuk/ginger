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

import com.vityuk.ginger.InvalidParameterTypeException;
import com.vityuk.ginger.InvalidReturnTypeException;
import com.vityuk.ginger.Localizable;
import com.vityuk.ginger.PluralCount;
import com.vityuk.ginger.Select;
import com.vityuk.ginger.provider.LocalizationProvider;
import com.vityuk.ginger.util.GingerUtils;
import com.vityuk.ginger.util.MiscUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

public class ReflectProxyBuilder implements ProxyBuilder {

    @Override
    public <T> T createProxy(Class<T> object, LocalizationProvider localizationProvider) {
        Localizable localizable = (Localizable) Proxy.newProxyInstance(object.getClassLoader(),
                new Class[]{object},
                new MyInvocationHandler(localizationProvider));
        return object.cast(localizable);
    }

    private class MyInvocationHandler implements InvocationHandler {

        private final LocalizationProvider localizationProvider;

        public MyInvocationHandler(LocalizationProvider localizationProvider) {
            this.localizationProvider = localizationProvider;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String key = GingerUtils.createKeyFromMethod(method);
            Class<?> type = method.getReturnType();
            if (GingerUtils.isConstantMethod(method)) {
                return getConstant(localizationProvider, method, type, key);
            } else {
                return getMessage(localizationProvider, method, type, key, args);
            }
        }

        private Object getConstant(LocalizationProvider localizationProvider, Method method, Class<?> type, String key) {
            if (type == String.class) {
                return localizationProvider.getString(key);
            }
            if (type == Boolean.class) {
                return localizationProvider.getBoolean(key);
            }
            if (type == Integer.class) {
                return localizationProvider.getInteger(key);
            }
            if (type == Long.class) {
                return localizationProvider.getLong(key);
            }
            if (type == Float.class) {
                return localizationProvider.getFloat(key);
            }
            if (type == Double.class) {
                return localizationProvider.getDouble(key);
            }
            if (type == List.class) {
                // TODO: generics support
                return localizationProvider.getStringList(key);
            }
            if (type == Map.class) {
                // TODO: generics support
                return localizationProvider.getStringMap(key);
            }

            throw new InvalidReturnTypeException(type, method);
        }


        private Object getMessage(LocalizationProvider localizationProvider, Method method, Class<?> type, String key, Object[] args) {
            if (type != String.class) {
                throw new InvalidReturnTypeException(type, method);
            }

            int selectorParameterIndex = GingerUtils.indexOfParameterAnnotation(method, Select.class);
            if (selectorParameterIndex != -1) {
                return createSelectorMessageLookupCallback(localizationProvider, method, key, selectorParameterIndex, args);
            }

            int pluralCountParameterIndex = GingerUtils.indexOfParameterAnnotation(method, PluralCount.class);
            if (pluralCountParameterIndex != -1) {
                return createPluralMessageLookupCallback(localizationProvider, method, key, pluralCountParameterIndex, args);
            }

            return localizationProvider.getMessage(key, args);
        }

        private Object createSelectorMessageLookupCallback(LocalizationProvider localizationProvider, Method method, String key, int parameterIndex, Object[] args) {
            Class<?> parameterType = method.getParameterTypes()[parameterIndex];
            if (parameterType.isPrimitive() || MiscUtils.isWrapperType(parameterType)) {
                // TODO: consider more informative exception
                throw new InvalidParameterTypeException(parameterType, method);
            }
            String selector = String.valueOf(args[parameterIndex]);
            Object[] parameters = ArrayUtils.remove(args, parameterIndex);
            return localizationProvider.getSelectedMessage(key, selector, parameters);
        }

        private Object createPluralMessageLookupCallback(LocalizationProvider localizationProvider, Method method, String key, int parameterIndex, Object[] args) {
            Class<?> parameterType = method.getParameterTypes()[parameterIndex];
            if (!GingerUtils.isIntNumericType(parameterType)) {
                // TODO: consider more informative exception
                throw new InvalidParameterTypeException(parameterType, method);
            }
            Number pluralCount = (Number) args[parameterIndex];
            Object[] parameters = ArrayUtils.remove(args, parameterIndex);
            return localizationProvider.getPluralMessage(key, pluralCount, parameters);
        }
    }
}
