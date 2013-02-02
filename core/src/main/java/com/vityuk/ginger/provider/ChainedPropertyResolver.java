package com.vityuk.ginger.provider;

import com.google.common.base.Preconditions;
import com.vityuk.ginger.PropertyResolver;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This implementation aggregates multiple underlying property resolvers.
 * <p/>
 * Each lookup methods delegates calls to underlying property resolver until property found.
 */
class ChainedPropertyResolver implements PropertyResolver {
    private final Collection<PropertyResolver> propertyResolvers;

    public ChainedPropertyResolver(Collection<PropertyResolver> propertyResolvers) {
        this.propertyResolvers = Preconditions.checkNotNull(propertyResolvers);
    }

    @Override
    public String getString(String key) {
        for (PropertyResolver propertyResolver : propertyResolvers) {
            String value = propertyResolver.getString(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }


    @Override
    public Boolean getBoolean(String key) {
        for (PropertyResolver propertyResolver : propertyResolvers) {
            Boolean value = propertyResolver.getBoolean(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }


    @Override
    public Integer getInteger(String key) {
        for (PropertyResolver propertyResolver : propertyResolvers) {
            Integer value = propertyResolver.getInteger(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }


    @Override
    public Long getLong(String key) {
        for (PropertyResolver propertyResolver : propertyResolvers) {
            Long value = propertyResolver.getLong(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }


    @Override
    public Float getFloat(String key) {
        for (PropertyResolver propertyResolver : propertyResolvers) {
            Float value = propertyResolver.getFloat(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }


    @Override
    public Double getDouble(String key) {
        for (PropertyResolver propertyResolver : propertyResolvers) {
            Double value = propertyResolver.getDouble(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public List<String> getList(String key) {
        for (PropertyResolver propertyResolver : propertyResolvers) {
            List<String> value = propertyResolver.getList(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getMap(String key) {
        for (PropertyResolver propertyResolver : propertyResolvers) {
            Map<String, String> value = propertyResolver.getMap(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
