package com.vityuk.ginger.spring;

import com.vityuk.ginger.LocaleResolver;
import com.vityuk.ginger.Localization;
import com.vityuk.ginger.LocalizationBuilder;
import com.vityuk.ginger.loader.LocalizationLoader;
import com.vityuk.ginger.loader.ResourceLoader;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;
import java.util.List;

/**
 * This {@code FactoryBean} creates instance of {@link Localization} bean.
 * <p/>
 * Properties can be used to configure {@code Localization} settings.
 */
public class LocalizationFactoryBean implements FactoryBean<Localization>, InitializingBean {
    private final LocalizationBuilder localizationBuilder = new LocalizationBuilder()
            .withLocaleResolver(new SpringLocaleResolver());

    private Localization localization;

    @Override
    public Localization getObject() throws Exception {
        return localization;
    }

    @Override
    public Class<Localization> getObjectType() {
        return Localization.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setResourceLocation(String resourceLocation) {
        localizationBuilder.withResourceLocation(resourceLocation);
    }

    public void setResourceLocations(List<String> resourceLocations) {
        localizationBuilder.withResourceLocations(resourceLocations);
    }

    /**
     * Set {@link LocaleResolver} to be used for resolving current {@link java.util.Locale}.
     * Be default {@link SpringLocaleResolver} is used.
     *
     * @param localeResolver, must be not {@code null}
     */
    public void setLocaleResolver(LocaleResolver localeResolver) {
        localizationBuilder.withLocaleResolver(localeResolver);
    }

    /**
     * See {@link LocalizationBuilder#withResourceLoaders(java.util.Collection)}.
     *
     * @param resourceLoaders, must be not {@code null}
     */
    public void setResourceLoaders(Collection<ResourceLoader> resourceLoaders) {
        localizationBuilder.withResourceLoaders(resourceLoaders);
    }

    /**
     * See {@link LocalizationBuilder#withResourceLoader(com.vityuk.ginger.loader.ResourceLoader)}.
     *
     * @param resourceLoader, must be not {@code null}
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        localizationBuilder.withResourceLoader(resourceLoader);
    }

    /**
     * See {@link LocalizationBuilder#withMaxResourcesCachingTimeInSec(int)}.
     *
     * @param maxResourcesCachingTimeInSec, must be not {@code null}
     */
    public void setMaxResourcesCachingTimeInSec(int maxResourcesCachingTimeInSec) {
        localizationBuilder.withMaxResourcesCachingTimeInSec(maxResourcesCachingTimeInSec);
    }

    /**
     * See {@link LocalizationBuilder#withLocalizationLoader(com.vityuk.ginger.loader.LocalizationLoader)}
     *
     * @param localizationLoader, must be not {@code null}
     */
    public void setLocalizationLoader(LocalizationLoader localizationLoader) {
        localizationBuilder.withLocalizationLoader(localizationLoader);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        localization = localizationBuilder.build();
    }
}
