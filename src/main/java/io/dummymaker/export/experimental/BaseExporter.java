package io.dummymaker.export.experimental;

import io.dummymaker.annotation.complex.GenTime;
import io.dummymaker.error.GenException;
import io.dummymaker.export.IExporter;
import io.dummymaker.model.export.DatetimeFieldContainer;
import io.dummymaker.model.export.FieldContainer;
import io.dummymaker.scan.IExportScanner;
import io.dummymaker.scan.impl.ExportScanner;
import io.dummymaker.util.CollectionUtils;
import io.dummymaker.writer.IWriter;
import io.dummymaker.writer.impl.FileWriter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core exporter class with mapping and converting functionality
 *
 * @author Anton Kurako (GoodforGod)
 * @since 22.7.2020
 */
public abstract class BaseExporter implements IExporter {

    protected final IExportScanner scanner = new ExportScanner();

    protected boolean append = false;

    abstract @NotNull String getExtension();

    abstract <T> @NotNull String map(T t, Collection<FieldContainer> containers);

    public @NotNull IExporter withAppend() {
        this.append = true;
        return this;
    }

    <T> @NotNull String getValue(T t, FieldContainer container) {
        if(t == null)
            return "";

        try {
            final Field field = container.getField();
            field.setAccessible(true);
            final Object value = field.get(t);
            if(value == null)
                return convertNull();

            switch (container.getType()) {
                case BOOLEAN:
                    return convertBoolean((Boolean) value);
                case NUMBER:
                case SEQUENTIAL:
                    return convertNumber(value);
                case STRING:
                    return convertString((String) value);
                case DATE:
                    return convertDate(value, (DatetimeFieldContainer) container);
                case ARRAY:
                    return convertArray((Object[]) value);
                case ARRAY_2D:
                    return convertArray2D((Object[][]) value);
                case MAP:
                    return convertMap((Map) value);
                case COLLECTION:
                    return convertCollection((Collection) value);
                case COMPLEX:
                default:
                    return convertComplex(value);
            }
        } catch (Exception ex) {
            throw new GenException(ex);
        }
    }

    String convertNull() {
        return "";
    }

    String convertBoolean(Boolean bool) {
        return bool.toString();
    }

    String convertString(String s) {
        return s;
    }

    String convertNumber(Object number) {
        return String.valueOf(number);
    }

    String convertDate(Object date, DatetimeFieldContainer container) {
        if(container.isUnixTime())
           return convertDateUnix(date);

        final String formatterPattern = container.getFormatter();
        final DateTimeFormatter formatter = getDateFormatter(date, formatterPattern);
        if (date instanceof Date) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(((Date) date).getTime()), TimeZone.getDefault().toZoneId())
                    .format(formatter);
        } else if (date instanceof LocalDate) {
            return ((LocalDate) date).format(formatter);
        } else if (date instanceof LocalTime) {
            return ((LocalTime) date).format(formatter);
        } else if (date instanceof LocalDateTime) {
            return ((LocalDateTime) date).format(formatter);
        } else {
            return String.valueOf(date);
        }
    }

    String convertDateUnix(Object date) {
        if (date instanceof Date) {
            return String.valueOf(((Date) date).getTime());
        } else if (date instanceof ChronoLocalDate) {
            return String.valueOf(((LocalDate) date).toEpochDay());
        } else if (date instanceof LocalTime) {
            return String.valueOf(LocalDateTime.of(LocalDate.of(1970, 1, 1),
                    ((LocalTime) date)).toEpochSecond(ZoneOffset.UTC));
        } else if (date instanceof LocalDateTime) {
            return String.valueOf(((LocalDateTime) date).toEpochSecond(ZoneOffset.UTC));
        } else {
            return String.valueOf(date);
        }
    }

    DateTimeFormatter getDateFormatter(Object date, String formatter) {
        if (date instanceof Time) {
            return GenTime.DEFAULT_FORMAT.equals(formatter)
                    ? DateTimeFormatter.ISO_TIME
                    : DateTimeFormatter.ofPattern(formatter);
        } else if (date instanceof Date) {
            return GenTime.DEFAULT_FORMAT.equals(formatter)
                    ? DateTimeFormatter.ISO_DATE_TIME
                    : DateTimeFormatter.ofPattern(formatter);
        } else if (date instanceof LocalDate) {
            return GenTime.DEFAULT_FORMAT.equals(formatter)
                    ? DateTimeFormatter.ISO_DATE
                    : DateTimeFormatter.ofPattern(formatter);
        } else if (date instanceof LocalTime) {
            return GenTime.DEFAULT_FORMAT.equals(formatter)
                    ? DateTimeFormatter.ISO_TIME
                    : DateTimeFormatter.ofPattern(formatter);
        } else if (date instanceof LocalDateTime) {
            return GenTime.DEFAULT_FORMAT.equals(formatter)
                    ? DateTimeFormatter.ISO_DATE_TIME
                    : DateTimeFormatter.ofPattern(formatter);
        } else {
            return DateTimeFormatter.ofPattern(formatter);
        }
    }

    String convertArray(Object[] array) {
        return Arrays.stream(array)
                .map(v -> v instanceof String ? convertString((String) v) : v.toString())
                .collect(Collectors.joining(",", "[", "]"));
    }

    String convertArray2D(Object[][] array) {
        return Arrays.deepToString(array);
    }

    String convertCollection(Collection<?> collection) {
        return collection.stream()
                .map(v -> v instanceof String ? convertString((String) v) : v.toString())
                .collect(Collectors.joining(",", "[", "]"));
    }

    String convertMap(Map<?, ?> map) {
        return map.entrySet().stream()
                .map(e -> {
                    final String key = e.getKey() instanceof String ? convertString((String) e.getKey()) : e.getKey().toString();
                    final String value = e.getValue() instanceof String ? convertString((String) e.getValue()) : e.getValue().toString();
                    return key + ":" + value;
                })
                .collect(Collectors.joining(",", "{", "}"));
    }

    String convertComplex(Object object) {
        return "";
    }

    <T> @NotNull String prefix(T t, Collection<FieldContainer> containers) {
        return "";
    }

    <T> @NotNull String suffix(T t, Collection<FieldContainer> containers) {
        return "";
    }

    <T> @NotNull String head(T t, Collection<FieldContainer> containers) {
        return "";
    }

    <T> @NotNull String tail(T t, Collection<FieldContainer> containers) {
        return "";
    }

    <T> @NotNull IWriter getWriter(String typeName) {
        return new FileWriter(typeName, ".", getExtension(), append);
    }

    @Override
    public <T> boolean export(T t) {
        if(t == null)
            return false;

        final Collection<FieldContainer> containers = scanner.scan(t.getClass());
        final IWriter writer = getWriter(t.getClass().getSimpleName());

        final String data = prefix(t, containers) + map(t, containers) + suffix(t, containers);
        return writer.write(head(t, containers))
                && writer.write(data)
                && writer.write(tail(t, containers));
    }

    @Override
    public <T> boolean export(Collection<T> collection) {
        if(CollectionUtils.isEmpty(collection))
            return false;

        final T t = collection.iterator().next();
        final Collection<FieldContainer> containers = scanner.scan(t.getClass());
        final IWriter writer = getWriter(t.getClass().getSimpleName());

        final String data = collection.stream()
                .filter(Objects::nonNull)
                .map(v -> prefix(v, containers) + map(v, containers) + suffix(v, containers))
                .collect(Collectors.joining());

        return writer.write(head(t, containers))
                && writer.write(data)
                && writer.write(tail(t, containers));
    }

    @Override
    public <T> @NotNull String convert(T t) {
        if(t == null)
            return "";

        final Collection<FieldContainer> containers = scanner.scan(t.getClass());
        final String data = prefix(t, containers) + map(t, containers) + suffix(t, containers);
        return head(t, containers) + data + tail(t, containers);
    }

    @Override
    public <T> @NotNull String convert(@NotNull Collection<T> collection) {
        if(CollectionUtils.isEmpty(collection))
            return "";

        final T t = collection.iterator().next();
        final Collection<FieldContainer> containers = scanner.scan(t.getClass());

        final String data = collection.stream()
                .filter(Objects::nonNull)
                .map(v -> prefix(v, containers) + map(v, containers) + suffix(v, containers))
                .collect(Collectors.joining());

        return head(t, containers) + data + tail(t, containers);
    }
}
