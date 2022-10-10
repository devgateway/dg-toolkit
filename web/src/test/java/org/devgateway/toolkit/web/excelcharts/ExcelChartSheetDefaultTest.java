package org.devgateway.toolkit.web.excelcharts;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author idobre
 * @since 9/8/16
 */
public class ExcelChartSheetDefaultTest {
    private static final List<?> CATEGORIES = Arrays.asList("cat 1", "cat 2", "cat 3", "cat 4", "cat 5");

    private static final List<List<? extends Number>> VALUES =
            Arrays.asList(Arrays.asList(5, 7, 10, 12, 6), Arrays.asList(20, 12, 10, 5, 14));

    private Workbook workbook;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
    }

    @Test
    public void writeCell() throws Exception {
        final ExcelChartSheet excelChartSheet = new ExcelChartSheetDefault(workbook, ChartType.bar.toString());
        final Row row = excelChartSheet.createRow();

        excelChartSheet.writeCell(null, row, 0);
        excelChartSheet.writeCell(Boolean.TRUE, row, 1);
        excelChartSheet.writeCell("text", row, 2);
        excelChartSheet.writeCell(1, row, 3);

        Assertions.assertEquals(CellType.BLANK, row.getCell(0).getCellTypeEnum());
        Assertions.assertEquals("Yes", row.getCell(1).getStringCellValue());
        Assertions.assertEquals(CellType.STRING, row.getCell(2).getCellTypeEnum());
        Assertions.assertEquals(CellType.NUMERIC, row.getCell(3).getCellTypeEnum());
    }

    @Test
    public void createRow() throws Exception {
        final ExcelChartSheet excelChartSheet = new ExcelChartSheetDefault(workbook, ChartType.area.toString());

        Row row1 = excelChartSheet.createRow();
        Assertions.assertNotNull(row1, "check row creation");

        Row row2 = excelChartSheet.createRow(2);
        Assertions.assertNotNull(row2, "check row creation");
    }

    @Test
    public void createChartAndLegend() throws Exception {
        final ExcelChartSheet excelChartSheet = new ExcelChartSheetDefault(workbook, ChartType.pie.toString());
        Chart chart = excelChartSheet.createChartAndLegend();

        Assertions.assertNotNull(chart);
    }

    @Test
    public void getCategoryChartDataSource() throws Exception {
        final ExcelChartSheet excelChartSheet = new ExcelChartSheetDefault(workbook, ChartType.barcol.toString());
        addCategories(excelChartSheet);

        ChartDataSource categoryChartDataSource = excelChartSheet.getCategoryChartDataSource();

        Assertions.assertEquals(5, categoryChartDataSource.getPointCount(), "check count of categories");
        Assertions.assertEquals("cat 1", categoryChartDataSource.getPointAt(0), "check first category");
        Assertions.assertEquals("cat 5", categoryChartDataSource.getPointAt(4), "check last category");
    }

    @Test
    public void getValuesChartDataSource() throws Exception {
        final ExcelChartSheet excelChartSheet = new ExcelChartSheetDefault(workbook, ChartType.stackedbar.toString());
        addCategories(excelChartSheet);
        addValues(excelChartSheet);

        List<ChartDataSource<Number>> valuesChartDataSource = excelChartSheet.getValuesChartDataSource();

        Assertions.assertEquals(2, valuesChartDataSource.size(), "numbers of values data source");
        Assertions.assertEquals(5, valuesChartDataSource.get(0).getPointCount(), "check count of values");
        Assertions.assertEquals(5, valuesChartDataSource.get(1).getPointCount(), "check count of values");

        Assertions.assertEquals(5.0, valuesChartDataSource.get(0).getPointAt(0), "check first value");
        Assertions.assertEquals(6.0, valuesChartDataSource.get(0).getPointAt(4), "check last value");

        Assertions.assertEquals(20.0, valuesChartDataSource.get(1).getPointAt(0), "check first value");
        Assertions.assertEquals(14.0, valuesChartDataSource.get(1).getPointAt(4), "check last value");
    }

    /**
     * Add a row with the categories.
     */
    private void addCategories(final ExcelChartSheet excelChartSheet) {
        final Row row = excelChartSheet.createRow();
        int coll = 0;
        for (Object category : CATEGORIES) {
            excelChartSheet.writeCell(category, row, coll);
            coll++;
        }
    }

    /**
     * Add one or multiple rows with the values.
     */
    private void addValues(final ExcelChartSheet excelChartSheet) {
        for (List<? extends Number> value : VALUES) {
            final Row row = excelChartSheet.createRow();
            int coll = 0;
            for (Number val : value) {
                excelChartSheet.writeCell(val, row, coll++);
            }
        }
    }
}
