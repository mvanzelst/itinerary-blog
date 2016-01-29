package nl.trifork.blog.ilp.itinerary.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class NativeLibraryLoader {

    private static final String NATIVE_LIB_DIR = "lib/ortools/";

    public static void loadOrTools(){
        String orToolsPath = findOrToolsPath();
        try {
            System.load(new ClassPathResource(orToolsPath).getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or tools library", e);
        }
    }

    public static String findOrToolsPath() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (isWindows(OS)) {
            return NATIVE_LIB_DIR + "win/jniortools.dll";
        } else if (isMac(OS)) {
            return NATIVE_LIB_DIR + "mac/libjniortools.jnilib";
        } else if (isUnix(OS)) {
            return NATIVE_LIB_DIR + "linux/libjniortools.so";
        } else {
            throw new RuntimeException("Could not determine OS");
        }
    }

    public static boolean isWindows(String OS) {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac(String OS) {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix(String OS) {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }
}
