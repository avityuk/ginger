package com.vityuk.ginger;

/**
 * Localization source
 */
public interface Localization {
    /**
     * Get localized instance of user defined {@link Constants} or {@link Messages} interface.
     *
     * @param localizable {@code Constants} or {@code Messages} interface, must be not {@code null}
     * @return localized instance of specified {@code localizable} interface implementation
     */
    <T extends Localizable> T get(Class<T> localizable);

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
