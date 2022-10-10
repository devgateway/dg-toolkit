package org.devgateway.toolkit.web.excelcharts;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.charts.XSSFCategoryAxis;
import org.apache.poi.xssf.usermodel.charts.XSSFChartAxis;
import org.apache.poi.xssf.usermodel.charts.XSSFValueAxis;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.STLegendPos;

import java.util.Arrays;
import java.util.List;

/**
 * @author idobre
 * @since 9/8/16
 */
public class ExcelChartDefaultTest {
    private static final List<?> CATEGORIES = Arrays.asList("cat 1", "cat 2", "cat 3", "cat 4", "cat 5");

    private static final List<List<? extends Number>> VALUES =
            Arrays.asList(Arrays.asList(5, 7, 10, 12, 6), Arrays.asList(20, 12, 10, 5, 14));

    @Test
    public void createWorkbook() throws Exception {
        final ExcelChart excelChart = new ExcelChartDefault("line chart", ChartType.line, CATEGORIES, VALUES);
        excelChart.configureSeriesTitle(Arrays.asList("foo", "bar"));
        final Workbook workbook = excelChart.createWorkbook();
        Assertions.assertNotNull(workbook);

        final Sheet sheet = workbook.getSheet(ChartType.line.toString());
        Assertions.assertNotNull(sheet);

        final XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
        final List<XSSFChart> charts = drawing.getCharts();
        Assertions.assertEquals(1, charts.size(), "number of charts");

        final XSSFChart chart = charts.get(0);
        Assertions.assertEquals("line chart", chart.getTitleText().getString(), "chart title");

        final CTChart ctChart = chart.getCTChart();
        Assertions.assertEquals(0, ctChart.getPlotArea().getAreaChartArray().length, "We should not have any area chart");
        Assertions.assertEquals(1, ctChart.getPlotArea().getLineChartArray().length, "Check if we have 1 line chart");
        Assertions.assertEquals(STLegendPos.B, ctChart.getLegend().getLegendPos().getVal(),
                "Check that we have a legend and that it's position is bottom");

        // check the actual chart data
        final CTLineChart ctLineChart = ctChart.getPlotArea().getLineChartArray()[0];
        final CTLineSer[] ctLineSer = ctLineChart.getSerArray();
        Assertions.assertEquals(2, ctLineSer.length, "Check number of CTLineSer");
        Assertions.assertEquals("foo", ctLineSer[0].getTx().getV(), "check first series title");
        Assertions.assertEquals("bar", ctLineSer[1].getTx().getV(), "check second series title");

        final CTAxDataSource cat1 = ctLineSer[0].getCat();
        Assertions.assertEquals("cat 1", cat1.getStrRef().getStrCache().getPtArray()[0].getV(), "check first category");
        Assertions.assertEquals("cat 5", cat1.getStrRef().getStrCache().getPtArray()[4].getV(), "check last category");
        final CTAxDataSource cat2 = ctLineSer[1].getCat();
        Assertions.assertEquals("cat 1", cat2.getStrRef().getStrCache().getPtArray()[0].getV(), "check first category");
        Assertions.assertEquals("cat 5", cat2.getStrRef().getStrCache().getPtArray()[4].getV(), "check last category");

        final CTNumDataSource val1 = ctLineSer[0].getVal();
        Assertions.assertEquals("5.0", val1.getNumRef().getNumCache().getPtArray()[0].getV(), "check first value");
        Assertions.assertEquals("6.0", val1.getNumRef().getNumCache().getPtArray()[4].getV(), "check last value");
        final CTNumDataSource val2 = ctLineSer[1].getVal();
        Assertions.assertEquals("20.0", val2.getNumRef().getNumCache().getPtArray()[0].getV(), "check first value");
        Assertions.assertEquals("14.0", val2.getNumRef().getNumCache().getPtArray()[4].getV(), "check last value");

        final List<? extends XSSFChartAxis> axis = chart.getAxis();
        Assertions.assertEquals(2, axis.size(), "number of axis");
        Assertions.assertTrue(axis.get(0) instanceof XSSFCategoryAxis, "category axis");
        Assertions.assertTrue(axis.get(1) instanceof XSSFValueAxis, "category axis");
    }
}
