package com.vityuk.ginger.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface LocalizationLoader {
	Map<String, String> load(InputStream inputStream) throws IOException;
}
