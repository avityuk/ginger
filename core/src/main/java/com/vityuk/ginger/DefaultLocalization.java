package com.vityuk.ginger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.NoOp;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class DefaultLocalization implements Localization {
    public static final char PROPERTY_KEY_DELIMETER = '.';
    private final LocalizationProvider localizationProvider;

    public DefaultLocalization(LocalizationProvider localizationProvider) {
        this.localizationProvider = localizationProvider;
    }

    @Override
    public <T extends Localizable> T get(Class<T> localizable) {
        return createLocalizable(localizable);
    }

    @Override
    public String getMessage(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private <T extends Localizable> T createLocalizable(Class<T> localizable) {
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

    private <T extends Localizable> T createProxy(Class<T> localizable, CallbackFilter callbackFilter, List<Callback> callbacks) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{localizable});
        enhancer.setCallbackFilter(callbackFilter);
        enhancer.setCallbacks(callbacks.toArray(new Callback[0]));
        enhancer.setUseFactory(false);

        return (T) enhancer.create();
    }


    private Callback createCallback(Method method) {
        String key = createKeyFromMethodName(method.getName());
        Class<?> type = method.getReturnType();
        return getCallbackByType(key, type, method);
    }

    private Callback getCallbackByType(String key, Class<?> type, Method method) {
        if (type == String.class) {
            return new StringLookupCallback(localizationProvider, key);
        }
        if (type == Boolean.class) {
            return new BooleanLookupCallback(localizationProvider, key);
        }
        if (type == Integer.class) {
            return new IntLookupCallback(localizationProvider, key);
        }
        if (type == Long.class) {
            return new LongLookupCallback(localizationProvider, key);
        }
        if (type == Float.class) {
            return new FloatLookupCallback(localizationProvider, key);
        }
        if (type == Double.class) {
            return new DoubleLookupCallback(localizationProvider, key);
        }
        if (type == List.class) {
            // TODO: generics support
            return new StringListLookupCallback(localizationProvider, key);
        }
        if (type == Map.class) {
            // TODO: generics support
            return new StringMapLookupCallback(localizationProvider, key);
        }

        throw new UnsupportedTypeException(type, method);
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

    private static abstract class AbstractLookupCallback implements FixedValue {
        protected final LocalizationProvider localizationProvider;
        protected final String key;

        public AbstractLookupCallback(LocalizationProvider localizationProvider, String key) {
            this.localizationProvider = localizationProvider;
            this.key = key;
        }
    }

    private static class StringLookupCallback extends AbstractLookupCallback {
        public StringLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getString(key);
        }
    }


    private static class BooleanLookupCallback extends AbstractLookupCallback {
        public BooleanLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getBoolean(key);
        }
    }

    private static class IntLookupCallback extends AbstractLookupCallback {
        public IntLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getInt(key);
        }
    }


    private static class LongLookupCallback extends AbstractLookupCallback {
        public LongLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getLong(key);
        }
    }

    private static class FloatLookupCallback extends AbstractLookupCallback {
        public FloatLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getFloat(key);
        }
    }


    private static class DoubleLookupCallback extends AbstractLookupCallback {
        public DoubleLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getDouble(key);
        }
    }


    private static class StringListLookupCallback extends AbstractLookupCallback {
        public StringListLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getStringList(key);
        }
    }


    private static class StringMapLookupCallback extends AbstractLookupCallback {
        public StringMapLookupCallback(LocalizationProvider localizationProvider, String key) {
            super(localizationProvider, key);
        }

        @Override
        public Object loadObject() throws Exception {
            return localizationProvider.getStringMap(key);
        }
    }
}
