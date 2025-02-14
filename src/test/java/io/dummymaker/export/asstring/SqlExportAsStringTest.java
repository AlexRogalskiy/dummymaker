package io.dummymaker.export.asstring;

import io.dummymaker.export.Cases;
import io.dummymaker.export.ICase;
import io.dummymaker.export.IExporter;
import io.dummymaker.export.impl.SqlExporter;
import io.dummymaker.export.validators.SqlValidator;
import io.dummymaker.factory.impl.GenFactory;
import io.dummymaker.model.Dummy;
import io.dummymaker.model.DummyArray;
import java.util.List;
import org.junit.Test;

/**
 * "Default Description"
 *
 * @author GoodforGod
 * @since 20.08.2017
 */
public class SqlExportAsStringTest extends StringExportAssert {

    private final GenFactory factory = new GenFactory();
    private final SqlValidator validation = new SqlValidator();

    public SqlExportAsStringTest() {
        super(new SqlExporter(), new SqlValidator(), 8, 9);
    }

    @Test
    public void exportListOfDummiesInSqlWithNamingStrategy() {
        final ICase strategy = Cases.SNAKE_CASE.value();

        final List<Dummy> dummies = factory.build(Dummy.class, 2);
        final IExporter exporter = new SqlExporter().withCase(strategy);

        final String dummyAsString = exporter.convert(dummies);
        assertNotNull(dummyAsString);

        final String[] sqlArray = dummyAsString.split("\n");
        assertEquals(9, sqlArray.length);

        validation.isTwoDummiesValidWithNamingStrategy(sqlArray, strategy);
    }

    @Test
    public void exportDummiesWithArrays() {
        final ICase strategy = Cases.SNAKE_CASE.value();

        final List<DummyArray> dummies = factory.build(DummyArray.class, 2);
        final IExporter exporter = new SqlExporter()
                .withCase(strategy);

        final String dummyAsString = exporter.convert(dummies);
        assertNotNull(dummyAsString);

        final String[] sqlArray = dummyAsString.split("\n");
        assertEquals(10, sqlArray.length);

        validation.isTwoDummiesArrayValidWithNamingStrategy(sqlArray, strategy);
    }
}
