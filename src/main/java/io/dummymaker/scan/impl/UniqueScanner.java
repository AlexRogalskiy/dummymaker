package io.dummymaker.scan.impl;

import io.dummymaker.scan.IAnnotationScanner;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Scan for field and its unique annotations (without duplicates) And return fields in correct order
 * Core scanner implementation
 *
 * @author GoodforGod
 * @see IAnnotationScanner
 * @see AnnotationScanner
 * @since 08.03.2018
 */
public class UniqueScanner extends AnnotationScanner {

    @Override
    public @NotNull Map<Field, List<Annotation>> scan(final Class target) {
        final Map<Field, List<Annotation>> scanned = new LinkedHashMap<>();

        super.scan(target).forEach((k, v) -> {
            final List<Annotation> annotations = v.stream().distinct().collect(Collectors.toList());
            scanned.put(k, annotations);
        });

        return scanned;
    }
}
