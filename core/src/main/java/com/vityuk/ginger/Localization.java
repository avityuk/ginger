package com.vityuk.ginger;

/**
 * Localization source
 */
public interface Localization {
    /**
     * Get localized instance of user defined {@link Localizable} interface.
     *
     * @param localizable {@code Localizable} interface, must be not {@code null}
     * @return localized instance of specified {@code localizable} interface implementation
     */
    <T extends Localizable> T getLocalizable(Class<T> localizable);

    /**
     * Get localized message for a given key.
     * <p/>
     * If the message for a given key is not found behavior of this method determined by... TODO: add config
     *
     * @param key message key, must be not {@code null}
     * @return localized message
     */
    String getMessage(String key);
}
