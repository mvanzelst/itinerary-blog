package nl.trifork.blog.ilp.planning.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NativeLibraryLoader {

    private static final String NATIVE_LIB_DIR = "lib/ortools/native/";

    private static final Map<String, String> libLocations = new HashMap<String, String>(){{
        put("linux64", NATIVE_LIB_DIR + "linux64/libjniortools.so");
        put("mac64", NATIVE_LIB_DIR + "mac64/libjniortools.jnilib");
        put("win64", NATIVE_LIB_DIR + "win64/jniortools.dll");
    }};

    public static void loadOrTools(String os){
        String orToolsLocation = libLocations.get(os);
        if(orToolsLocation == null){
            throw new RuntimeException("Please specify platform. " +
                    "Available platforms are: linux64, mac64 or win64");
        }
        try {
            System.load(new ClassPathResource(orToolsLocation).getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or tools library", e);
        }
    }
}
