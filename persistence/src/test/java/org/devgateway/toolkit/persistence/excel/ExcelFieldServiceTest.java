package org.devgateway.toolkit.persistence.excel;

import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.excel.info.ClassFields;
import org.devgateway.toolkit.persistence.excel.info.ClassFieldsDefault;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Persistable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author idobre
 * @since 10/11/2017
 */
public class ExcelFieldServiceTest {
    private class TestClass implements Persistable<Long> {
        private static final long serialVersionUID = 1L;

        private Long id;

        private List<String> label;

        @ExcelExport(justExport = true)
        private OtherClass otherClass1;

        @ExcelExport(separateSheet = true)
        private OtherClass otherClass2;

        public TestClass(final Long id) {
            this.id = id;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return false;
        }
    }

    private class OtherClass {
        private Boolean valid;

        @ExcelExport(name = "money")
        private Integer amount;
    }

    @Test
    public void getFieldType() throws Exception {
        final ClassFields classFields = new ClassFieldsDefault(TestClass.class);
        final Iterator<Field> fields = classFields.getFields();

        final Field firstField = fields.next();      // get first element
        Assertions.assertEquals(FieldType.basic, ExcelFieldService.getFieldType(firstField), "Check basic field type");

        final Field secondField = fields.next();      // get second element
        Assertions.assertEquals(FieldType.basic, ExcelFieldService.getFieldType(secondField), "Check type class for a List");

        final Field thirdField = fields.next();      // get third element
        Assertions.assertEquals(FieldType.basic, ExcelFieldService.getFieldType(thirdField), "Check basic field type for Object");

        final Field fourthField = fields.next();      // get fourth element
        Assertions.assertEquals(FieldType.objectSeparateSheet, ExcelFieldService.getFieldType(fourthField), "Check objectSeparateSheet field type Object");
    }

    @Test
    public void getFieldClass() throws Exception {
        final ClassFields classFields = new ClassFieldsDefault(TestClass.class);
        final Iterator<Field> fields = classFields.getFields();

        final Field firstField = fields.next();      // get first element
        Assertions.assertEquals(Long.class, ExcelFieldService.getFieldClass(firstField), "Check basic field class");


        final Field secondField = fields.next();      // get second element
        Assertions.assertEquals(String.class, ExcelFieldService.getFieldClass(secondField), "Check field class for a List");
    }

    @Test
    public void getFields() throws Exception {
        final String[] expectedFields = {"otherClass1", "otherClass2"};

        final Iterator<Field> fields = ExcelFieldService.getFields(TestClass.class);

        final List<String> actualFields = new ArrayList<>();
        while (fields.hasNext()) {
            final Field f = fields.next();
            actualFields.add(f.getName());
        }

        Assertions.assertArrayEquals(expectedFields, actualFields.toArray(), "Check get fields");
    }

    @Test
    public void getObjectID() throws Exception {
        final OtherClass otherClass = new OtherClass();
        Assertions.assertEquals(Long.valueOf(-1), ExcelFieldService.getObjectID(otherClass), "Check object ID");

        final TestClass testclass = new TestClass((long) 10);
        Assertions.assertEquals(Long.valueOf(10), ExcelFieldService.getObjectID(testclass), "Check object ID");
    }

    @Test
    public void getFieldName() throws Exception {
        final ClassFields classFields = new ClassFieldsDefault(OtherClass.class);
        final Iterator<Field> fields = classFields.getFields();

        final Field firstField = fields.next();      // get first element
        Assertions.assertEquals("valid", ExcelFieldService.getFieldName(null, firstField, null), "Check field name");

        final Field secondField = fields.next();      // get second element
        Assertions.assertEquals("money", ExcelFieldService.getFieldName(null, secondField, null), "Check field name");
    }
}
