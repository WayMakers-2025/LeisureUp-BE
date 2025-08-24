package org.leisureup.global.auth.social;

import java.nio.file.*;
import org.springframework.util.*;

public class Utils {

    public static byte[] supplyResponse(
            String resourceDir,
            String fileName
    ) {

        String classPath = String.format(
                "classpath:%s/%s", resourceDir, fileName
        );

        try {
            return Files.readAllBytes(
                    ResourceUtils.getFile(classPath).toPath()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toBearer(String token) {
        return "Bearer " + token;
    }

}
