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

/**
 * Default {@link PluralFormSelectorResolver} implementation which looks up {@link PluralRule}
 * using {@code languageCode} and using it resolves selector for specified {@code count}.
 *
 * @author Andriy Vityuk
 */
public class DefaultPluralFormSelectorResolver implements PluralFormSelectorResolver {
    private final PluralRuleProvider pluralRuleProvider = new PluralRuleProvider();

    @Override
    public String resolve(String languageCode, int count) {
        return pluralRuleProvider.getPluralRule(languageCode).select(count);
    }
}
