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

package com.vityuk.ginger.provider.plural;

import com.google.common.base.Preconditions;
import com.google.common.reflect.Reflection;
import com.vityuk.ginger.LocalizationException;
import com.vityuk.ginger.provider.plural.impl.DefaultPluralRule;

/**
 * @author Andriy Vityuk
 */
class PluralRuleProvider {
    private static final PluralRule DEFAULT_PLURAL_RULE = new DefaultPluralRule();

    private static final String PLURAL_RULES_PACKAGE = Reflection.getPackageName(DefaultPluralRule.class);
    private static final String PLURAL_RULE_CLASS_PREFIX = PluralRule.class.getSimpleName();

    public PluralRule getPluralRule(String languageCode) {
        Preconditions.checkNotNull(languageCode);
        if (languageCode.isEmpty()) {
            return DEFAULT_PLURAL_RULE;
        }

        Class<?> pluralClass = loadPluralClass(languageCode);
        if (pluralClass == null) {
            return DEFAULT_PLURAL_RULE;
        }

        try {
            @SuppressWarnings("unchecked")
            PluralRule pluralRule = (PluralRule) pluralClass.newInstance();
            return pluralRule;
        } catch (InstantiationException e) {
            throw new LocalizationException(e);
        } catch (IllegalAccessException e) {
            throw new LocalizationException(e);
        }
    }

    private Class<?> loadPluralClass(String languageCode) {
        try {
            return Class.forName(createPluralRuleClassName(languageCode));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static String createPluralRuleClassName(String languageCode) {
        return PLURAL_RULES_PACKAGE + '.' + PLURAL_RULE_CLASS_PREFIX + '_' + languageCode;
    }
}
