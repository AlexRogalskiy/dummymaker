package io.dummymaker.export;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Export objects is XML format
 *
 * @author GoodforGod
 * @since 26.05.2017
 */
public class XmlExporter<T> extends OriginExporter<T> {

    private enum Mode {
        SINGLE,
        LIST
    }

    public XmlExporter(final Class<T> primeClass) {
        this(primeClass, null);
    }

    public XmlExporter(final Class<T> primeClass, final String path) {
        super(primeClass, path, ExportFormat.XML);
    }

    private String wrapOpenXmlTag(final String value) {
        return "<" + value + ">";
    }

    private String wrapCloseXmlTag(final String value) {
        return "</" + value + ">";
    }

    private String objectToXml(final T t, final Mode mode) {
        final Iterator<Map.Entry<String, String>> iterator = getExportValues(t).entrySet().iterator();

        final StringBuilder builder = new StringBuilder("");

        final String tabObject = (mode == Mode.SINGLE)
                ? ""
                : "\t";

        final String tabField = (mode == Mode.SINGLE)
                ? "\t"
                : "\t\t";

        if(iterator.hasNext()) {
            builder.append(tabObject).append(wrapOpenXmlTag(exportClass.getSimpleName()));

            while (iterator.hasNext()) {
                Map.Entry<String, String> field = iterator.next();
                builder.append("\n").append(tabField)
                                    .append(wrapOpenXmlTag(field.getKey()))
                                    .append(field.getValue())
                                    .append(wrapCloseXmlTag(field.getKey()));
            }
            builder.append("\n").append(tabObject).append(wrapCloseXmlTag(exportClass.getSimpleName()));
        }

        return builder.toString();
    }

    @Override
    public boolean export(final T t) {
        return writeLine(objectToXml(t, Mode.SINGLE)) && flush();
    }

    @Override
    public boolean export(final List<T> list) {
        final String superList = exportClass.getSimpleName() + "List";
        writeLine(wrapOpenXmlTag(superList));

        for (T t : list)
            writeLine(objectToXml(t, Mode.LIST));

        writeLine(wrapCloseXmlTag(superList));

        return flush();
    }

    @Override
    public String exportAsString(final T t) {
        return objectToXml(t, Mode.SINGLE);
    }

    @Override
    public String exportAsString(final List<T> list) {
        final String superList = exportClass.getSimpleName() + "List";
        final StringBuilder result = new StringBuilder();
        result.append(wrapOpenXmlTag(superList));

        for (T t : list)
            result.append(objectToXml(t, Mode.LIST));

        result.append(wrapCloseXmlTag(superList));

        return result.toString();
    }
}
