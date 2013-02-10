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
 * @author Andriy Vityuk
 */
public class PluralRule_1_x1_19_nTest extends BasePluralRuleTest {
    {
        add(1, "one");

        add(0, "few");
        for (int i = 2; i <= 19; i++) {
            add(i, "few");
        }
        for (int i = 100; i <= 1000; i += 100) {
            for (int j = 1; j <= 19; j++) {
                add(i + j, "few");
            }
        }

        for (int  i = 0; i < 1000; i+=100) {
            for (int  j = 20; j <= 100; j++) {
                add(i + j, "other");
            }
        }
    }

    @Override
    protected PluralRule pluralRule() {
        return new PluralRule_1_x1_19_n();
    }
}
