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

import com.vityuk.ginger.provider.plural.impl.DefaultPluralRule;
import com.vityuk.ginger.provider.plural.impl.PluralRule_en;
import com.vityuk.ginger.provider.plural.impl.PluralRule_es;
import com.vityuk.ginger.provider.plural.impl.PluralRule_ru;
import com.vityuk.ginger.provider.plural.impl.PluralRule_shi;
import com.vityuk.ginger.provider.plural.impl.PluralRule_tig;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Andriy Vityuk
 */
public class PluralRuleProviderTest {
    private final PluralRuleProvider pluralRuleProvider = new PluralRuleProvider();

    @Test
    public void testGetPluralRuleWithUnknownLanguageCode() throws Exception {
        PluralRule pluralRule = pluralRuleProvider.getPluralRule("blablabla");

        assertThat(pluralRule).isNotNull().isInstanceOf(DefaultPluralRule.class);
    }

    @Test
    public void testGetPluralRuleWithEmptyLanguageCode() throws Exception {
        PluralRule pluralRule = pluralRuleProvider.getPluralRule("");

        assertThat(pluralRule).isNotNull().isInstanceOf(DefaultPluralRule.class);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPluralRuleWithNullLanguageCode() throws Exception {
        pluralRuleProvider.getPluralRule(null);
    }

    @Test
    public void testGetPluralRuleWithValidLanguageCode() throws Exception {
        assertThat(pluralRuleProvider.getPluralRule("en")).isNotNull().isInstanceOf(PluralRule_en.class);
        assertThat(pluralRuleProvider.getPluralRule("es")).isNotNull().isInstanceOf(PluralRule_es.class);
        assertThat(pluralRuleProvider.getPluralRule("ru")).isNotNull().isInstanceOf(PluralRule_ru.class);
        assertThat(pluralRuleProvider.getPluralRule("shi")).isNotNull().isInstanceOf(PluralRule_shi.class);
        assertThat(pluralRuleProvider.getPluralRule("tig")).isNotNull().isInstanceOf(PluralRule_tig.class);
    }
}
