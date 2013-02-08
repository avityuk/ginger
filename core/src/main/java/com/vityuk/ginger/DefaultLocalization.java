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

package com.vityuk.ginger;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.UncheckedExecutionException;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.NoOp;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author Andriy Vityuk
 */
public class DefaultLocalization implements Localization {
    public static final char PROPERTY_KEY_DELIMETER = '.';

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Localizable> T getLocalizable(Class<T> localizable) {
        try {
            T localizableInstance = (T) localizableCache.getUnchecked((Class<Localizable>) localizable);
            return localizableInstance;
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    private final LocalizationProvider localizationProvider;

    private final LoadingCache<Class<Localizable>, Localizable> localizableCache;

    public DefaultLocalization(LocalizationProvider localizationProvider) {
        this.localizationProvider = localizationProvider;

        localizableCache = CacheBuilder.newBuilder().build(new CacheLoader<Class<Localizable>, Localizable>() {
            @Override
            public Localizable load(Class<Localizable> localizable) throws Exception {
                return createLocalizableInstance(localizable);
            }
        });
    }

    @Override
    public String getMessage(String key, Object... parameters) {
        return localizationProvider.getMessage(key, parameters);
    }

    private <T extends Localizable> T createLocalizableInstance(Class<T> localizable) {
        Method[] methods = localizable.getDeclaredMethods();

        List<Callback> callbacks = Lists.newArrayListWithCapacity(methods.length + 1);
        final Map<Method, Integer> method2CallbackIndex = Maps.newHashMapWithExpectedSize(methods.length);

        callbacks.add(NoOp.INSTANCE);
        for (Method localizableMethod : methods) {
            callbacks.add(createCallback(localizableMethod));
            method2CallbackIndex.put(localizableMethod, callbacks.size() - 1);
        }

        CallbackFilter callbackFilter = new CallbackFilter() {
            @Override
            public int accept(Method method) {
                Integer index = method2CallbackIndex.get(method);
                return index == null ? 0 : index;
            }
        };

        T instance = createProxy(localizable, callbackFilter, callbacks);
        return instance;
    }

    private <T extends Localizable> T createProxy(Class<T> localizable, CallbackFilter callbackFilter,
                                                  List<Callback> callbacks) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{localizable});
        enhancer.setCallbackFilter(callbackFilter);
        enhancer.setCallbacks(callbacks.toArray(new Callback[0]));
        enhancer.setUseFactory(false);

        @SuppressWarnings("unchecked")
        T instance = (T) enhancer.create();
        return instance;
    }


    private Callback createCallback(Method method) {
        String key = createKeyFromMethodName(method.getName());
        Class<?> type = method.getReturnType();
        if (isConstantMethod(method)) {
            return getConstantCallback(method, type, key);
        } else {
            return getMessageCallback(method, type, key);
        }
    }

    private boolean isConstantMethod(Method method) {
        return method.getParameterTypes().length == 0;
    }

    private Callback getMessageCallback(Method method, Class<?> type, String key) {
        if (type != String.class) {
            throw new InvalidReturnTypeException(type, method);
        }
        return new MessageLookupCallback(localizationProvider, key);
    }

    private Callback getConstantCallback(Method method, Class<?> type, String key) {
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

    private static String createKeyFromMethodName(String methodName) {
        String[] words = StringUtils.splitByCharacterTypeCamelCase(methodName);
        StringBuilder keyBuilder = new StringBuilder(methodName.length() + words.length - 1);

        boolean first = true;
        for (String word : words) {
            if (first) {
                first = false;
            } else {
                keyBuilder.append(PROPERTY_KEY_DELIMETER);
            }
            keyBuilder.append(word.toLowerCase());
        }

        return keyBuilder.toString();
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
}
