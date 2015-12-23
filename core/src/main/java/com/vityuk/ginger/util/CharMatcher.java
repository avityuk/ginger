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

package com.vityuk.ginger.util;

/**
 * Do the same that Guava code without 6MB jar ...
 */
public abstract class CharMatcher {
    public static final CharMatcher BREAKING_WHITESPACE = new CharMatcher() {
        @Override
        public boolean matches(char c) {
            switch (c) {
                case '\t':
                case '\n':
                case '\013':
                case '\f':
                case '\r':
                case ' ':
                case '\u0085':
                case '\u1680':
                case '\u2028':
                case '\u2029':
                case '\u205f':
                case '\u3000':
                    return true;
                case '\u2007':
                    return false;
                default:
                    return c >= '\u2000' && c <= '\u200a';
            }
        }

        @Override
        public String toString() {
            return "CharMatcher.BREAKING_WHITESPACE";
        }
    };

    public static CharMatcher anyOf(String s) {
        final char chars[] = s.toCharArray();
        return new CharMatcher() {
            @Override
            public boolean matches(char code) {
                return MiscUtils.indexOf(chars, code) >= 0;
            }
        };
    }

    public CharMatcher negate() {
        return new Not(this);
    }

    public CharMatcher and(CharMatcher ...matchers) {
        CharMatcher newMatchers[] = new CharMatcher[matchers.length + 1];
        newMatchers[0] = this;
        System.arraycopy(matchers, 0, newMatchers, 1, matchers.length);
        return new And(newMatchers);
    }

    public CharMatcher or(CharMatcher ...matchers) {
        CharMatcher newMatchers[] = new CharMatcher[matchers.length + 1];
        newMatchers[0] = this;
        System.arraycopy(matchers, 0, newMatchers, 1, matchers.length);
        return new Or(newMatchers);
    }

    public abstract boolean matches(char code);


    private class Or extends CharMatcher {

        private final CharMatcher[] matchers;

        public Or(CharMatcher ...matchers) {
            this.matchers = matchers;
        }

        @Override
        public boolean matches(char code) {
            for (CharMatcher charMatcher: matchers) {
                if (charMatcher.matches(code)) {
                    return true;
                }
            }
            return false;
        }
    }

    private class And extends CharMatcher {

        private final CharMatcher[] matchers;

        public And(CharMatcher ...matchers) {
            this.matchers = matchers;
        }

        @Override
        public boolean matches(char code) {
            for (CharMatcher charMatcher: matchers) {
                if (!charMatcher.matches(code)) {
                    return false;
                }
            }
            return true;
        }
    }

    private class Not extends CharMatcher {

        private final CharMatcher matcher;

        public Not(CharMatcher matcher) {
            this.matcher = matcher;
        }

        @Override
        public boolean matches(char code) {
            return !matcher.matches(code);
        }
    }
}
