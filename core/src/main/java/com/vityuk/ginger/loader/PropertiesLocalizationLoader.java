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

package com.vityuk.ginger.loader;

import com.vityuk.ginger.PropertyResolver;
import com.vityuk.ginger.util.CharMatcher;
import com.vityuk.ginger.util.MiscUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vityuk.ginger.util.Preconditions.checkNotNull;

/**
 * Localization loader for Java properties format. Then only difference from standard Java {@link java.util.Properties}
 * is full UTF-8 support without additional conversion.
 *
 * @author Andriy Vityuk
 */
public class PropertiesLocalizationLoader implements LocalizationLoader {
    private static final CharMatcher COMMENT_MATCHER = CharMatcher.anyOf("#!");
    private static final CharMatcher LINE_SEPARATOR_MATCHER = CharMatcher.anyOf("\n\r");
    private static final CharMatcher NOT_LINE_SEPARATOR_MATCHER = LINE_SEPARATOR_MATCHER.negate();
    private static final CharMatcher WHITESPACE_MATCHER = CharMatcher.BREAKING_WHITESPACE.and(NOT_LINE_SEPARATOR_MATCHER);
    private static final CharMatcher KEY_VALUE_SEPARATOR_MATCHER = CharMatcher.anyOf("=:");
    private static final CharMatcher WHITESPACE_OR_SEPARATOR_MATCHER =  KEY_VALUE_SEPARATOR_MATCHER.or(CharMatcher.BREAKING_WHITESPACE);

    private static final Pattern MAP_KEY_PATTERN = Pattern.compile("([^\\[\\]]+)\\[([^\\[\\]]+)\\]");

    @Override
    public PropertyResolver load(InputStream inputStream) throws IOException {
        MatchingReader reader = new MatchingReader(new BufferedReader(new InputStreamReader(inputStream)));
        return load(reader);
    }

    private PropertyResolver load(MatchingReader reader) throws IOException {
        final Map<String, Map<String, String>> mapProperties = new HashMap<String, Map<String, String>>();

        while (!reader.isEndOfStream()) {
            int code = reader.peek();
            if (COMMENT_MATCHER.matches((char) code)) {
                reader.skipCharacters(NOT_LINE_SEPARATOR_MATCHER);
                continue;
            }

            reader.skipCharacters(WHITESPACE_MATCHER);
            if (!reader.isEndOfLine()) {
                String key = reader.readLineUntil(WHITESPACE_OR_SEPARATOR_MATCHER);

                reader.skipCharacters(WHITESPACE_MATCHER);

                code = reader.peek();
                if (code != -1 && KEY_VALUE_SEPARATOR_MATCHER.matches((char) code)) {
                    reader.read();
                    reader.skipCharacters(WHITESPACE_MATCHER);
                }

                String value = reader.readLineUntil(LINE_SEPARATOR_MATCHER);

                Matcher matcher = MAP_KEY_PATTERN.matcher(key);
                String propertyKey;
                String mapKey;
                if (matcher.matches()) {
                    /*
                     This is map property of format: propertyKey[mapKey]=value
                    */
                    propertyKey = matcher.group(1);
                    mapKey = matcher.group(2);
                } else {
                    propertyKey = key;
                    mapKey = "";
                }
                Map<String, String> propertyMap = mapProperties.get(propertyKey);
                if (propertyMap == null) {
                    propertyMap = new HashMap<String, String>(4);
                    mapProperties.put(propertyKey, propertyMap);
                }
                propertyMap.put(mapKey, value);
            }
            reader.skipCharacters(LINE_SEPARATOR_MATCHER);
        }

        return createPropertyResolver(mapProperties);
    }

    protected PropertyResolver createPropertyResolver(Map<String, Map<String, String>> mapProperties) {
        return new ResourcePropertyResolver(mapProperties);
    }

    private static class MatchingReader extends Reader {
        private static final char QUOTATION_CHAR = '\\';
        private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        private final BufferedReader reader;
        private int bufferedChar = -1;

        public MatchingReader(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            boolean hasBufferedChar = bufferedChar != -1;
            if (hasBufferedChar) {
                cbuf[off] = (char) bufferedChar;
                bufferedChar = -1;
                off++;
                len--;
            }
            int read = reader.read(cbuf, off, len);
            if (hasBufferedChar) {
                read++;
            }
            return read;
        }

        public int peek() throws IOException {
            if (bufferedChar == -1) {
                bufferedChar = read();
            }
            return bufferedChar;
        }

        public boolean isEndOfStream() throws IOException {
            return peek() == -1;
        }

        private boolean isEndOfLine() throws IOException {
            int code = peek();
            if (code == -1) {
                return true;
            }
            return LINE_SEPARATOR_MATCHER.matches((char) code);
        }

        public void skipCharacters(CharMatcher charMatcher) throws IOException {
            for (; ; ) {
                int code = peek();
                if (code == -1 || !charMatcher.matches((char) code)) {
                    return;
                }
                read();
            }
        }

        public String readLineUntil(CharMatcher charMatcher) throws IOException {
            final StringBuilder sb = new StringBuilder();
            for (; ; ) {
                int code = peek();
                if (code == -1 || charMatcher.matches((char) code)) {
                    break;
                }

                int quotedCode = readQuotedCharacter();
                if (quotedCode != -1) {
                    sb.append((char) quotedCode);
                }
            }
            return sb.toString();
        }

        private int readQuotedCharacter() throws IOException {
            int code = read();
            if (code == -1) {
                return -1;
            }

            char ch = (char) code;
            if (ch != QUOTATION_CHAR) {
                return ch;
            }

            return readCharacter();
        }

        private int readCharacter() throws IOException {
            int code = read();
            if (code == -1) {
                return -1;
            }

            char ch = (char) code;
            switch (ch) {
                case 'u':
                    return readEscapedUnicodeCharacter();
                case '\r':
                    if (peek() == '\n') {
                        // skip it
                        read();
                    }
                case '\n':
                    skipCharacters(WHITESPACE_MATCHER);
                    return -1;
                case 't':
                    return '\t';
                case 'n':
                    return '\n';
                case 'r':
                    return '\r';
                default:
                    return ch;
            }
        }

        private char readEscapedUnicodeCharacter() throws IOException {
            int result = 0;
            for (int i = 0; i < 4; i++) {
                int code = read();
                if (code == -1) {
                    throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                }
                char ch = (char) code;
                int digit = MiscUtils.indexOf(HEX_CHARS, Character.toUpperCase(ch));
                if (digit == -1) {
                    throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                }

                result = result * 16 + digit;
            }
            return (char) result;
        }

        @Override
        public void close() throws IOException {
            reader.close();
        }
    }

    private static class ResourcePropertyResolver implements PropertyResolver {
        private final Map<String, Map<String, String>> mapProperties;

        public ResourcePropertyResolver(Map<String, Map<String, String>> mapProperties) {
            this.mapProperties = mapProperties;
        }

        @Override
        public String getString(String key) {
            return get(key);
        }

        @Override
        public Boolean getBoolean(String key) {
            String value = get(key);
            return value == null ? null : Boolean.valueOf(value);
        }

        @Override
        public Integer getInteger(String key) {
            String value = get(key);
            return value == null ? null : Integer.valueOf(value);
        }

        @Override
        public Long getLong(String key) {
            String value = get(key);
            return value == null ? null : Long.valueOf(value);
        }

        @Override
        public Float getFloat(String key) {
            String value = get(key);
            return value == null ? null : Float.valueOf(value);
        }

        @Override
        public Double getDouble(String key) {
            String value = get(key);
            return value == null ? null : Double.valueOf(value);
        }

        @Override
        public List<String> getStringList(String key) {
            String value = get(key);
            if (value == null) {
                return null;
            }
            String[] split = value.split(",");
            List<String> list = new ArrayList<String>(split.length);
            for (String item: split) {
                String it = item.trim();
                if (!it.isEmpty()) {
                    list.add(it);
                }
            }
            return Collections.unmodifiableList(list);
        }

        @Override
        public Map<String, String> getStringMap(String key) {
            return mapProperties.get(key);
        }

        @Override
        public Set<String> getKeys() {
            return mapProperties.keySet();
        }

        private String get(String key) {
            checkNotNull(key);
            Map<String, String> map = mapProperties.get(key);
            if (map != null) {
                return map.get("");
            }
            return null;
        }
    }
}
