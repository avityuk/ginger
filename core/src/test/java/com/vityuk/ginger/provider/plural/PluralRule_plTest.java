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
public class PluralRule_plTest extends BasePluralRuleTest {
    {
        // n is 1
        add(1, "one");

        // n mod 10 in 2..4 and n mod 100 not in 12..14
        add(2, "few");
        add(3, "few");
        add(4, "few");
        add(22, "few");
        add(23, "few");
        add(24, "few");
        add(32, "few");
        add(33, "few");
        add(34, "few");
        add(42, "few");
        add(43, "few");
        add(44, "few");
        add(52, "few");
        add(53, "few");
        add(54, "few");
        add(62, "few");
        add(63, "few");
        add(64, "few");
        add(72, "few");
        add(73, "few");
        add(74, "few");
        add(82, "few");
        add(83, "few");
        add(84, "few");
        add(92, "few");
        add(93, "few");
        add(94, "few");
        add(102, "few");
        add(103, "few");
        add(104, "few");
        add(122, "few");
        add(123, "few");
        add(124, "few");
        add(202, "few");
        add(203, "few");
        add(204, "few");

        // n is not 1 and n mod 10 in 0..1 or n mod 10 in 5..9 or n mod 100 in 12..14
        add(0, "many");
        for (int i = 5; i <= 21; i++) {
            add(i, "many");
        }
        for (int i = 25; i <= 31; i++) {
            add(i, "many");
        }
        for (int i = 35; i <= 41; i++) {
            add(i, "many");
        }
        for (int i = 45; i <= 51; i++) {
            add(i, "many");
        }
        for (int i = 55; i <= 61; i++) {
            add(i, "many");
        }
        for (int i = 75; i <= 81; i++) {
            add(i, "many");
        }
        for (int i = 85; i <= 91; i++) {
            add(i, "many");
        }
        for (int i = 105; i <= 121; i++) {
            add(i, "many");
        }
        for (int i = 125; i <= 131; i++) {
            add(i, "many");
        }
        for (int i = 205; i <= 221; i++) {
            add(i, "many");
        }
    }

    @Override
    protected PluralRule pluralRule() {
        return new PluralRule_pl();
    }
}
