package org.leisureup.global.logging.aspect;

import java.lang.reflect.*;
import java.util.*;
import lombok.extern.slf4j.*;
import org.aspectj.lang.*;
import org.aspectj.lang.reflect.*;
import org.leisureup.*;
import org.leisureup.global.logging.*;
import org.springframework.stereotype.*;

@Slf4j
@Component
public class MethodLoggingUtils {

    private static final String MASK_REPRESENTATION = "[*****]";
    private static final String UNABLE_TO_EXTRACT = "<?>";
    private static final String BASE_PACKAGE_NAME
            = LeisureUp.class.getPackage().getName();

    public Method cast(JoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }

    public String representMethodReturnObj(Method method, Object returnObj) {
        try {
            method.setAccessible(true);

            String representation;

            if (isMethodReturnMasked(method)) {
                representation = MASK_REPRESENTATION;
            } else {
                representation = representObj(returnObj);
            }

            return representation;
        } catch (Exception e) {
            log.warn("Failed to log method returns value", e);
            return UNABLE_TO_EXTRACT;
        }
    }

    public String representMethodArguments(Method method, Object[] args) {
        try {
            method.setAccessible(true);

            StringBuilder sb = new StringBuilder().append("(");

            Parameter[] parameters = method.getParameters();

            if (parameters.length != args.length) {
                throw new IllegalArgumentException("Argument & parameter count must same");
            }

            for (int i = 0; i < args.length; i++) {

                Parameter parameter = parameters[i];
                Object arg = args[i];

                sb.append(representSingleArg(parameter, arg));

                if (i < parameters.length - 1) {
                    sb.append(", ");
                }
            }

            return sb.append(")").toString();
        } catch (Exception e) {
            log.warn("Failed to log method arguments", e);
            return UNABLE_TO_EXTRACT;
        }
    }

    private String representSingleArg(Parameter parameter, Object arg) {
        String name = parameter.getName();
        String representation;

        if (isMasked(parameter)) {
            representation = MASK_REPRESENTATION;
        } else {
            representation = representObj(arg);
        }

        return String.format("%s=%s", name, representation);
    }

    private boolean isMasked(Parameter parameter) {
        return Arrays.stream(parameter.getAnnotations())
                .anyMatch(annotation -> annotation instanceof Masked);
    }

    private boolean isMasked(Object object) {
        return Arrays.stream(object.getClass().getAnnotations())
                .anyMatch(annotation -> annotation instanceof Masked);
    }

    private boolean isMethodReturnMasked(Method method) {
        return Arrays.stream(method.getAnnotations())
                .anyMatch(annotation -> annotation instanceof MaskedReturn);
    }

    private String representObj(Object obj) {

        if (obj == null) {
            return "null";
        }

        // has Masked annotation on class definition?
        if (isMasked(obj)) {
            return MASK_REPRESENTATION;
        }

        if (
                obj instanceof String || obj instanceof Character ||
                obj instanceof Number || obj instanceof Boolean ||
                obj instanceof Enum
        ) {
            return obj.toString();
        }

        if (obj instanceof Collection<?> col) {
            return col.stream().map(this::representObj)
                    .toList()
                    .toString();
        }

        if (obj instanceof Map<?, ?> map) {
            Map<String, String> representation = new HashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = representObj(entry.getKey());
                String value = representObj(entry.getValue());
                representation.put(key, value);
            }
            return representation.toString();
        }

        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            List<String> representation = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                representation.add(representObj(Array.get(obj, i)));
            }
            return representation.toString();
        }

        // some other complex clazz

        // not our clazz
        String packageName = obj.getClass().getPackage().getName();
        if (!packageName.startsWith(BASE_PACKAGE_NAME)) {
            return obj.toString();
        }

        StringBuilder sb = new StringBuilder()
                .append(obj.getClass().getSimpleName())
                .append("{");
        Field[] fields = obj.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            String name = field.getName();
            sb.append(name).append("=");

            String valRepresentation;

            if (field.isAnnotationPresent(Masked.class)) {
                valRepresentation = MASK_REPRESENTATION;
            } else {
                try {
                    valRepresentation = representObj(field.get(obj));
                } catch (Exception e) {
                    valRepresentation = UNABLE_TO_EXTRACT;
                }
            }

            sb.append(valRepresentation);

            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }

        return sb.append("}").toString();
    }
}
