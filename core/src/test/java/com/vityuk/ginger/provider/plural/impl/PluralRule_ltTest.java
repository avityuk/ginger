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
import com.vityuk.ginger.provider.plural.impl.PluralRule_lt;

/**
 * @author Andriy Vityuk
 */
public class PluralRule_ltTest extends BasePluralRuleTest {
    {
        for (int i = 0; i <= 1000; i += 100) {
            add(i + 1, "one");
            for (int j = 21; j < 100; j+=10) {
                add(i + j, "one");
            }
        }

        for (int i = 0; i <= 1000; i += 100) {
            for (int k = 2; k <= 9; k++) {
                add(i + k, "few");
            }

            for (int j = 20; j < 100; j += 10) {
                for (int k = 2; k <= 9; k++) {
                    add(i + j + k, "few");
                }
            }
        }

        for (int i = 0; i <= 1000; i += 10) {
            add(i, "other");
        }
        for (int i = 11; i <= 20; i++) {
            add(i, "other");
        }
    }

    @Override
    protected PluralRule pluralRule() {
        return new PluralRule_lt();
    }
}
