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

/**
 * Plural rules for Breton language.
 *
 * @author Andriy Vityuk
 */
public final class PluralRule_br extends AbstractPluralRule {
    private static final String[] QUALIFIERS = new String[]{"other", "one", "two", "few", "many"};

    @Override
    protected String[] qualifiers() {
        return QUALIFIERS;
    }

    @Override
    protected int selectQualifier(int count) {
        return count % 10 == 1 && (count % 100 != 11 && count % 100 != 71 && count % 100 != 91) ? 1
                : count % 10 == 2 && (count % 100 != 12 && count % 100 != 72 && count % 100 != 92) ? 2
                : (count % 10 == 3) && (count % 100 != 13 && count % 100 != 73 && count % 100 != 93) ? 3
                : (count % 10 == 4) && (count % 100 != 14 && count % 100 != 74 && count % 100 != 94) ? 3
                : (count % 10 == 9) && (count % 100 != 19 && count % 100 != 79 && count % 100 != 99) ? 3
                : count != 0 && count % 1000000 == 0 ? 4
                : 0;
    }
}