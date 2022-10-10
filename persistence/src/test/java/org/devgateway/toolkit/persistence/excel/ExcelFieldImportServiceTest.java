package org.devgateway.toolkit.persistence.excel;

import org.devgateway.toolkit.persistence.excel.annotation.ExcelImport;
import org.devgateway.toolkit.persistence.excel.info.ClassFields;
import org.devgateway.toolkit.persistence.excel.info.ClassFieldsDefault;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author idobre
 * @since 19/03/2018
 */
public class ExcelFieldImportServiceTest {
    private class TestClass {
        private static final long serialVersionUID = 1L;

        private Long id;

        @ExcelImport
        private OtherClass otherClass1;

        @ExcelImport(importByProp = "id")
        private OtherClass otherClass2;

        public TestClass(final Long id) {
            this.id = id;
        }
    }

    private class OtherClass {
        private Boolean valid;

        @ExcelImport(name = "money")
        private Integer amount;
    }

    @Test
    public void getFieldType() throws Exception {
        final ClassFields classFields = new ClassFieldsDefault(TestClass.class);
        final Iterator<Field> fields = classFields.getFields();

        final Field firstField = fields.next();      // get first element
        Assertions.assertEquals(FieldTypeImport.basic, ExcelFieldImportService.getFieldType(firstField), "Check basic field type");

        final Field thirdField = fields.next();      // get second element
        Assertions.assertEquals(FieldTypeImport.object, ExcelFieldImportService.getFieldType(thirdField), "Check basic field type for Object");

        final Field fourthField = fields.next();      // get third element
        Assertions.assertEquals(FieldTypeImport.objectImportByProp, ExcelFieldImportService.getFieldType(fourthField), "Check importByProp field type Object");
    }

    @Test
    public void getFields() throws Exception {
        final String[] expectedFields = {"otherClass1", "otherClass2"};

        final Iterator<Field> fields = ExcelFieldImportService.getFields(TestClass.class);

        final List<String> actualFields = new ArrayList<>();
        while (fields.hasNext()) {
            final Field f = fields.next();
            actualFields.add(f.getName());
        }

        Assertions.assertArrayEquals(expectedFields, actualFields.toArray(), "Check get fields");
    }
}
