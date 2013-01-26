package com.vityuk.ginger.loader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class FileSystemResourceLoader extends AbstractResourceLoader {
    private static final String SCHEMA = "file";

    public FileSystemResourceLoader() {
        super(SCHEMA);
    }

    @Override
    protected InputStream openResource(String path) throws IOException {
        return new FileInputStream(path);
    }
}
