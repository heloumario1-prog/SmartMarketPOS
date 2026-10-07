package Util;

import java.util.UUID;

public class VoidCodeGenerator {

    public static String generateVoidCode() {
        // Generate a short hex UUID section
        String hex = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        return "ADM-" + hex;
    }
}
