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

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * @author Andriy Vityuk
 */
public abstract class BasePluralRuleTest {
    private final Map<Integer, String> count2Qualifier = Maps.newHashMap();

    protected abstract PluralRule pluralRule();

    @Test
    public void testRule() {
        PluralRule pluralRule = pluralRule();

        for (Map.Entry<Integer, String> entry : count2Qualifier.entrySet()) {
            Integer count = entry.getKey();
            String qualifier = entry.getValue();

            assertEquals("For count " + count, qualifier, pluralRule.select(count));
        }
    }

    public void add(int count, String qualifier) {
        count2Qualifier.put(count, qualifier);
    }
}
