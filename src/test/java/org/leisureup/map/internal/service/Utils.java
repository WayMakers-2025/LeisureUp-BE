package org.leisureup.map.internal.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.fasterxml.jackson.databind.*;
import com.github.tomakehurst.wiremock.matching.*;
import java.nio.file.*;
import java.util.*;
import org.springframework.util.*;

public class Utils {

    private static final ObjectMapper objMapper = new ObjectMapper();

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

    public static JsonNode readFrom(String resourceDir, String fileName) {
        byte[] bytes = supplyResponse(resourceDir, fileName);
        try {
            return objMapper.readTree(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON from file: " + fileName, e);
        }
    }

    public static Map<String, StringValuePattern> buildParamWith(
            double x, double y, int radius,
            int pageNo, int numOfRows, String arrange,
            String fullCat
    ) {

        Map<String, StringValuePattern> queryParams = new HashMap<>();

        queryParams.put("mapX", equalTo(String.valueOf(x)));
        queryParams.put("mapY", equalTo(String.valueOf(y)));
        queryParams.put("radius", equalTo(String.valueOf(radius)));

        queryParams.put("pageNo", equalTo(String.valueOf(pageNo)));
        queryParams.put("numOfRows", equalTo(String.valueOf(numOfRows)));
        queryParams.put("arrange", equalTo(arrange));

        if (fullCat != null && fullCat.length() == 9) {
            String cat1 = fullCat.substring(0, 3);
            String cat2 = fullCat.substring(0, 5);

            queryParams.put("cat1", equalTo(cat1));
            queryParams.put("cat2", equalTo(cat2));
            queryParams.put("cat3", equalTo(fullCat));
        }

        return queryParams;
    }
}
