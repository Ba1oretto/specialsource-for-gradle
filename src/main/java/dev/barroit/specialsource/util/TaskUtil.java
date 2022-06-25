package dev.barroit.specialsource.util;

import java.io.File;
import java.net.URI;

public class TaskUtil {
    private TaskUtil() {
        throw new UnsupportedOperationException("This class cannot be instance.");
    }

    public static File getFile(URI uri) {
        return new File(uri);
    }

    public static File getFile(String path) {
        return new File(path);
    }

    public static File getFile(File parent, String child) {
        return new File(parent, child);
    }

    public static String setVersion(String temp, String version) {
        return String.format(temp, version, version);
    }

    public static boolean illegalVersion(String version) {
        return null == version || version.isBlank();
    }
}
