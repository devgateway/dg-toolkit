package org.devgateway.toolkit.persistence.excel.info;

import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author idobre
 * @since 10/11/2017
 */
public class ClassFieldsExcelExportTest {
    private class TestClass {
        private static final long serialVersionUID = 1L;

        @ExcelExport(name = "id")
        private int id;

        @ExcelExport
        private String label;

        private String description;
    }

    private class TestClassImproved extends TestClass {
        private static final long serialVersionUID = 1L;

        @ExcelExport
        private Boolean valid;

        private Long amount;
    }


    @Test
    public void getFields() throws Exception {
        final String[] expectedFields = {"id", "label"};

        final ClassFields classFields = new ClassFieldsExcelExport(
                new ClassFieldsDefault(TestClass.class)
        );
        final Iterator<Field> fields = classFields.getFields();

        final List<String> actualFields = new ArrayList<>();
        while (fields.hasNext()) {
            final Field f = fields.next();
            actualFields.add(f.getName());
        }

        Assertions.assertArrayEquals(expectedFields, actualFields.toArray(),
                "Check declared @ExcelExport fields");
    }

    @Test
    public void getInheritedFields() throws Exception {
        final String[] expectedFields = {"id", "label", "valid"};

        final ClassFields classFields = new ClassFieldsExcelExport(
                new ClassFieldsDefault(TestClassImproved.class, true)
        );
        final Iterator<Field> fields = classFields.getFields();

        final List<String> actualFields = new ArrayList<>();
        while (fields.hasNext()) {
            final Field f = fields.next();
            actualFields.add(f.getName());
        }

        Assertions.assertArrayEquals(expectedFields, actualFields.toArray(),
                "Check declared & inherited @ExcelExport fields");
    }
}
