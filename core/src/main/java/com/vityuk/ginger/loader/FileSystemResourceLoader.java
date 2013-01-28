package com.vityuk.ginger.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class FileSystemResourceLoader extends AbstractResourceLoader {
    private static final String SCHEMA = "file";

    public FileSystemResourceLoader() {
        super(SCHEMA);
    }

    @Override
    protected InputStream openResource(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            // return null according to interface contract
            return null;
        }
    }
}
