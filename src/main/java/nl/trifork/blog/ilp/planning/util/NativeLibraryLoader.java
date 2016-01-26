package nl.trifork.blog.ilp.planning.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class NativeLibraryLoader {

    public static void loadOrTools(String os){
        try {
            System.load(new ClassPathResource("lib/" + os + "/libjniortools.so").getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or tools library. Please specify platform as argument. " +
                    "Available platforms are: linux64, mac64 or win64");
        }
    }
}
