package com.vityuk.ginger.loader;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractResourceLoader implements ResourceLoader {
    private static final String PATTERN_SUFFIX = ":(.+)";
    private final Pattern locationPattern;

    public AbstractResourceLoader(String schema) {
        this.locationPattern = createLocationPattern(schema);
    }

    protected abstract InputStream openResource(String path) throws IOException;

    @Override
    public final boolean isSupported(String location) {
        Preconditions.checkNotNull(location);
        return matcher(location).matches();
    }

    @Override
    public final InputStream openStream(String location) throws IOException {
        Preconditions.checkNotNull(location);
        Matcher matcher = matcher(location);
        Preconditions.checkArgument(matcher.matches(), "Unsupported location");
        String path = matcher.group(1);

        return openResource(path);
    }

    private Matcher matcher(String location) {
        return locationPattern.matcher(location);
    }

    private static Pattern createLocationPattern(String schema) {
        return Pattern.compile(schema + PATTERN_SUFFIX);
    }
}
