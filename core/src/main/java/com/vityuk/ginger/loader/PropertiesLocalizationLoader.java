package com.vityuk.ginger.loader;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Maps;
import com.google.common.primitives.Chars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 * Localization loader for Java properties format. Then only difference from standard Java {@link java.util.Properties}
 * is full UTF-8 support without additional conversion.
 */
public class PropertiesLocalizationLoader implements LocalizationLoader {
    private static final CharMatcher COMMENT_MATCHER = CharMatcher.anyOf("#!");
    private static final CharMatcher LINE_SEPARATOR_MATCHER = CharMatcher.anyOf("\n\r");
    private static final CharMatcher NOT_LINE_SEPARATOR_MATCHER = LINE_SEPARATOR_MATCHER.negate();
    private static final CharMatcher WHITESPACE_MATCHER =
            CharMatcher.BREAKING_WHITESPACE.and(NOT_LINE_SEPARATOR_MATCHER);
    private static final CharMatcher KEY_VALUE_SEPARATOR_MATCHER = CharMatcher.anyOf("=:");
    private static final CharMatcher WHITESPACE_OR_SEPARATOR_MATCHER =
            KEY_VALUE_SEPARATOR_MATCHER.or(CharMatcher.BREAKING_WHITESPACE);

    @Override
    public Map<String, String> load(InputStream inputStream) throws IOException {
        MatchingReader reader = new MatchingReader(new BufferedReader(new InputStreamReader(inputStream)));
        return load(reader);
    }

    private Map<String, String> load(MatchingReader reader) throws IOException {
        final Map<String, String> properties = Maps.newHashMap();

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

                properties.put(key, value);
            }
            reader.skipCharacters(LINE_SEPARATOR_MATCHER);
        }

        return properties;
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
                int digit = Chars.indexOf(HEX_CHARS, Character.toUpperCase(ch));
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
}
