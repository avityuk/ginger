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
public class PluralRule_x1_x234_nTest extends BasePluralRuleTest {
    {
        add(1, "one");
        add(21, "one");
        add(31, "one");
        add(41, "one");
        add(51, "one");
        add(61, "one");
        add(71, "one");
        add(81, "one");

        add(2, "few");
        add(22, "few");
        add(42, "few");
        add(52, "few");
        add(62, "few");
        add(72, "few");
        add(92, "few");
        add(102, "few");
        add(122, "few");
        add(202, "few");
        add(3, "few");
        add(23, "few");
        add(33, "few");
        add(43, "few");
        add(53, "few");
        add(63, "few");
        add(73, "few");
        add(83, "few");
        add(93, "few");
        add(103, "few");
        add(123, "few");
        add(203, "few");
        add(4, "few");
        add(24, "few");
        add(34, "few");
        add(44, "few");
        add(54, "few");
        add(64, "few");
        add(74, "few");
        add(84, "few");
        add(94, "few");
        add(104, "few");
        add(124, "few");
        add(204, "few");

        add(0, "many");
        for (int i = 5; i <= 20; i++) {
            add(i, "many");
        }
        for (int i = 25; i <= 30; i++) {
            add(i, "many");
        }
        for (int i = 35; i <= 40; i++) {
            add(i, "many");
        }
        for (int i = 45; i <= 50; i++) {
            add(i, "many");
        }
        for (int i = 55; i <= 60; i++) {
            add(i, "many");
        }
        for (int i = 65; i <= 70; i++) {
            add(i, "many");
        }
        for (int i = 75; i <= 80; i++) {
            add(i, "many");
        }
        for (int i = 85; i <= 90; i++) {
            add(i, "many");
        }
        for (int i = 105; i <= 120; i++) {
            add(i, "many");
        }
        for (int i = 125; i <= 130; i++) {
            add(i, "many");
        }
        for (int i = 205; i <= 220; i++) {
            add(i, "many");
        }
        for (int i = 225; i <= 230; i++) {
            add(i, "many");
        }
    }

    @Override
    protected PluralRule pluralRule() {
        return new PluralRule_x1_x234_n();
    }
}
