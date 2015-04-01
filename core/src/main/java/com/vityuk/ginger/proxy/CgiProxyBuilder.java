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
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.NoOp;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CgiProxyBuilder implements ProxyBuilder {


    @Override
    public <T> T createProxy(Class<T> localizable, LocalizationProvider localizationProvider) {
        Method[] methods = localizable.getDeclaredMethods();

        List<Callback> callbacks = new ArrayList<Callback>(methods.length + 1);
        final Map<Method, Integer> method2CallbackIndex = new HashMap<Method, Integer>(methods.length);

        callbacks.add(NoOp.INSTANCE);
        for (Method localizableMethod : methods) {
            callbacks.add(createCallback(localizationProvider, localizableMethod));
            method2CallbackIndex.put(localizableMethod, callbacks.size() - 1);
        }

        CallbackFilter callbackFilter = new CallbackFilter() {
            @Override
            public int accept(Method method) {
                Integer index = method2CallbackIndex.get(method);
                return index == null ? 0 : index;
            }
        };

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{localizable});
        enhancer.setCallbackFilter(callbackFilter);
        enhancer.setCallbacks(callbacks.toArray(new Callback[callbacks.size()]));
        enhancer.setUseFactory(false);

        @SuppressWarnings("unchecked")
        T instance = (T) enhancer.create();
        return instance;
    }

    private Callback createCallback(LocalizationProvider localizationProvider, Method method) {
        String key = GingerUtils.createKeyFromMethod(method);
        Class<?> type = method.getReturnType();
        if (GingerUtils.isConstantMethod(method)) {
            return getConstantCallback(localizationProvider, method, type, key);
        } else {
            return getMessageCallback(localizationProvider, method, type, key);
        }
    }

    private Callback getMessageCallback(LocalizationProvider localizationProvider, Method method, Class<?> type, String key) {
        if (type != String.class) {
            throw new InvalidReturnTypeException(type, method);
        }

        int selectorParameterIndex = GingerUtils.indexOfParameterAnnotation(method, Select.class);
        if (selectorParameterIndex != -1) {
            return createSelectorMessageLookupCallback(localizationProvider, method, key, selectorParameterIndex);
        }

        int pluralCountParameterIndex = GingerUtils.indexOfParameterAnnotation(method, PluralCount.class);
        if (pluralCountParameterIndex != -1) {
            return createPluralMessageLookupCallback(localizationProvider, method, key, pluralCountParameterIndex);
        }

        return new MessageLookupCallback(localizationProvider, key);
    }

    private Callback createSelectorMessageLookupCallback(LocalizationProvider localizationProvider, Method method, String key, int parameterIndex) {
        Class<?> parameterType = method.getParameterTypes()[parameterIndex];
        if (parameterType.isPrimitive() || MiscUtils.isWrapperType(parameterType)) {
            // TODO: consider more informative exception
            throw new InvalidParameterTypeException(parameterType, method);
        }
        return new SelectorMessageLookupCallback(localizationProvider, key, parameterIndex);
    }

    private Callback createPluralMessageLookupCallback(LocalizationProvider localizationProvider, Method method, String key, int parameterIndex) {
        Class<?> parameterType = method.getParameterTypes()[parameterIndex];
        if (parameterType != int.class && parameterType != Integer.class) {
            // TODO: consider more informative exception
            throw new InvalidParameterTypeException(parameterType, method);
        }
        return new PluralMessageLookupCallback(localizationProvider, key, parameterIndex);
    }

    private Callback getConstantCallback(LocalizationProvider localizationProvider, Method method, Class<?> type, String key) {
        if (type == String.class) {
            return new StringConstantLookupCallback(localizationProvider, key);
        }
        if (type == Boolean.class) {
            return new BooleanConstantLookupCallback(localizationProvider, key);
        }
        if (type == Integer.class) {
            return new IntegerConstantLookupCallback(localizationProvider, key);
        }
        if (type == Long.class) {
            return new LongConstantLookupCallback(localizationProvider, key);
        }
        if (type == Float.class) {
            return new FloatConstantLookupCallback(localizationProvider, key);
        }
        if (type == Double.class) {
            return new DoubleConstantLookupCallback(localizationProvider, key);
        }
        if (type == List.class) {
            // TODO: generics support
            return new StringListConstantLookupCallback(localizationProvider, key);
        }
        if (type == Map.class) {
            // TODO: generics support
            return new StringMapConstantLookupCallback(localizationProvider, key);
        }

        throw new InvalidReturnTypeException(type, method);
    }

    private static abstract class AbstractLookupCallback implements Callback {
        protected final LocalizationProvider localizationProvider;
        protected final String key;

        public AbstractLookupCallback(LocalizationProvider localizationProvider, String key) {
            this.localizationProvider = localizationProvider;
            this.key = key;
        }
    }


    private static abstract class AbstractConstantLookupCallback extends AbstractLookupCallback implements FixedValue {
        public AbstractConstantLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }
    }

    private static class StringConstantLookupCallback extends AbstractConstantLookupCallback {
        public StringConstantLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getString(key);
        }
    }

    private static class BooleanConstantLookupCallback extends AbstractConstantLookupCallback {
        public BooleanConstantLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getBoolean(key);
        }
    }

    private static class IntegerConstantLookupCallback extends AbstractConstantLookupCallback {
        public IntegerConstantLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getInteger(key);
        }
    }

    private static class LongConstantLookupCallback extends AbstractConstantLookupCallback {
        public LongConstantLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getLong(key);
        }
    }

    private static class FloatConstantLookupCallback extends AbstractConstantLookupCallback {
        public FloatConstantLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getFloat(key);
        }
    }

    private static class DoubleConstantLookupCallback extends AbstractConstantLookupCallback {
        public DoubleConstantLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getDouble(key);
        }
    }

    private static class StringListConstantLookupCallback extends AbstractConstantLookupCallback {
        public StringListConstantLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getStringList(key);
        }
    }

    private static class StringMapConstantLookupCallback extends AbstractConstantLookupCallback {
        public StringMapConstantLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getStringMap(key);
        }
    }

    private static class MessageLookupCallback extends AbstractLookupCallback implements InvocationHandler {
        public MessageLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            return localizationProvider.getMessage(key, objects);
        }
    }

    private static class SelectorMessageLookupCallback extends AbstractLookupCallback implements InvocationHandler {
        private final int selectorParameterIndex;

        public SelectorMessageLookupCallback(LocalizationProvider localizationProvider, String key,
                                             int selectorParameterIndex) {
            super(localizationProvider, key);
            this.selectorParameterIndex = selectorParameterIndex;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            String selector = extractSelector(objects);
            Object[] parameters = extractParameters(objects);
            return localizationProvider.getSelectedMessage(key, selector, parameters);
        }

        private String extractSelector(Object[] objects) {
            return String.valueOf(objects[selectorParameterIndex]);
        }

        private Object[] extractParameters(Object[] objects) {
            return ArrayUtils.remove(objects, selectorParameterIndex);
        }
    }


    private static class PluralMessageLookupCallback extends AbstractLookupCallback implements InvocationHandler {
        private final int pluralCountParameterIndex;

        public PluralMessageLookupCallback(LocalizationProvider localizationProvider, String key,
                                           int pluralCountParameterIndex) {
            super(localizationProvider, key);
            this.pluralCountParameterIndex = pluralCountParameterIndex;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            int pluralCount = extractPluralCount(objects);
            Object[] parameters = extractParameters(objects);
            return localizationProvider.getPluralMessage(key, pluralCount, parameters);
        }

        private int extractPluralCount(Object[] objects) {
            return (Integer) objects[pluralCountParameterIndex];
        }


        private Object[] extractParameters(Object[] objects) {
            return ArrayUtils.remove(objects, pluralCountParameterIndex);
        }
    }
}
