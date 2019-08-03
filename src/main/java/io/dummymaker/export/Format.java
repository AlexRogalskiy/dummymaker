package io.dummymaker.export;

import io.dummymaker.container.FieldContainer.Type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * All available export types for exporters
 *
 * @author GoodforGod
 * @see IExporter
 * @since 25.02.2018
 */
public enum Format {
    CSV(".csv", Type.SIMPLE, Type.SEQUENTIAL),
    JSON(".json", Type.SIMPLE, Type.SEQUENTIAL, Type.ARRAY, Type.ARRAY_2D, Type.COLLECTION, Type.MAP),
    XML(".xml", Type.SIMPLE, Type.SEQUENTIAL),
    SQL(".sql", Type.SIMPLE, Type.SEQUENTIAL, Type.ARRAY, Type.ARRAY_2D, Type.COLLECTION);

    private final Set<Type> supported;
    private final String extension;

    Format(final String extension, Type... type) {
        this.extension = extension;
        this.supported = new HashSet<>(Arrays.asList(type));
    }

    public boolean isTypeSupported(Type type) {
        return supported.contains(type);
    }

    public Set<Type> getSupported() {
        return supported;
    }

    public String getExtension() {
        return extension;
    }
}
