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
import com.vityuk.ginger.provider.plural.impl.PluralRule_br;

/**
 * @author Andriy Vityuk
 */
public class PluralRule_brTest extends BasePluralRuleTest {
    {
        for (int i = 0; i <= 1000; i += 100) {
            add(i + 0, "other");
            add(i + 1, "one");
            add(i + 2, "two");
            add(i + 3, "few");
            add(i + 4, "few");
            add(i + 5, "other");
            add(i + 6, "other");
            add(i + 7, "other");
            add(i + 8, "other");
            add(i + 9, "few");
            for (int j = 10; j <= 20; j++) {
                add(i + j, "other");
            }

            add(i + 21, "one");
            add(i + 22, "two");
            add(i + 23, "few");
            add(i + 24, "few");
            add(i + 25, "other");
            add(i + 26, "other");
            add(i + 27, "other");
            add(i + 28, "other");
            add(i + 29, "few");
            add(i + 30, "other");
            add(i + 31, "one");
            add(i + 32, "two");
            add(i + 33, "few");
            add(i + 34, "few");
            add(i + 35, "other");
            add(i + 36, "other");
            add(i + 37, "other");
            add(i + 38, "other");
            add(i + 39, "few");
            add(i + 40, "other");
            add(i + 41, "one");
            add(i + 42, "two");
            add(i + 43, "few");
            add(i + 44, "few");
            add(i + 45, "other");
            add(i + 46, "other");
            add(i + 47, "other");
            add(i + 48, "other");
            add(i + 49, "few");
            add(i + 50, "other");
            add(i + 51, "one");
            add(i + 52, "two");
            add(i + 53, "few");
            add(i + 54, "few");
            add(i + 55, "other");
            add(i + 56, "other");
            add(i + 57, "other");
            add(i + 58, "other");
            add(i + 59, "few");
            add(i + 60, "other");
            add(i + 61, "one");
            add(i + 62, "two");
            add(i + 63, "few");
            add(i + 64, "few");
            add(i + 65, "other");
            add(i + 66, "other");
            add(i + 67, "other");
            add(i + 68, "other");
            add(i + 69, "few");
            for (int j = 70; j <= 80; j++) {
                add(i + j, "other");
            }
            add(i + 81, "one");
            add(i + 82, "two");
            add(i + 83, "few");
            add(i + 84, "few");
            add(i + 85, "other");
            add(i + 86, "other");
            add(i + 87, "other");
            add(i + 88, "other");
            add(i + 89, "few");
            for (int j = 90; j < 100; j++) {
                add(i + j, "other");
            }
        }

        for (int i = 1; i <= 9; i++) {
            add(i * 1000, "other");
            add(i * 10000, "other");
            add(i * 100000, "other");
            add(i * 1000000, "many");
            add(i * 10000000, "many");
            add(i * 100000000, "many");
        }
    }

    @Override
    protected PluralRule pluralRule() {
        return new PluralRule_br();
    }
}
