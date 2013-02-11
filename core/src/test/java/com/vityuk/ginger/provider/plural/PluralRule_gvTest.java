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
public class PluralRule_gvTest extends BasePluralRuleTest {
    {
        for (int i = 0; i <= 1000; i += 20) {
            add(i, "one");
        }
        for (int i = 0; i <= 1000; i += 10) {
            add(i + 1, "one");
            add(i + 2, "one");
        }

        for (int i = 10; i <= 1000; i += 20) {
            add(i, "other");
        }
        for (int i = 0; i <= 1000; i += 10) {
            for (int j = 3; j <= 9; j++) {
                add(i + j, "other");
                add(i + j, "other");
            }
        }
    }

    @Override
    protected PluralRule pluralRule() {
        return new PluralRule_gv();
    }
}
