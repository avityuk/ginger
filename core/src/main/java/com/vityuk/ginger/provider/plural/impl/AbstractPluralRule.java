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

package com.vityuk.ginger.provider.plural.impl;

import com.vityuk.ginger.provider.plural.PluralRule;

/**
 * @author Andriy Vityuk
 */
public abstract class AbstractPluralRule implements PluralRule {
    protected abstract String[] qualifiers();

    protected abstract int selectQualifier(int count);

    @Override
    public final String select(int count) {
        int qualifier = selectQualifier(count);
        String[] qualifiers = qualifiers();
        return qualifiers[qualifier];
    }
}
