package com.zrar.demos;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BarChartExample {
    private static String dir = "/home/echolee/ushares/POI-examples/";
    private static String dataFile = dir + "bar-chart-data.txt";
    private static String templateFile = dir + "bar-chart-template.docx";
    private static Logger logger = LoggerFactory.getLogger(BarChartExample.class);
    public static void main(String[] args) throws IOException {
        try(FileInputStream tempalte = new FileInputStream(templateFile);
            BufferedReader modelReader = new BufferedReader(new FileReader(dataFile))){
            //first line is chart title
            final String title = modelReader.readLine();
            final String[] series = modelReader.readLine().split(",");
            //category axis data
            List<String> languages = new ArrayList<>(16);
            // values
            List<Double> countries = new ArrayList<>(16);
            List<Double> speakers = new ArrayList<>(16);
            //set model
            String ln;
            while ((ln = modelReader.readLine()) != null) {
                final String[] vals = ln.split(",");
                countries.add(Double.valueOf(vals[0]));
                speakers.add(Double.valueOf(vals[1]));
                languages.add(vals[2]);
            }
            final String[] categories = languages.toArray(new String[0]);
            final Double[] values1 = countries.toArray(new Double[0]);
            final Double[] values2 = speakers.toArray(new Double[0]);

            try(final XWPFDocument doc = new XWPFDocument(tempalte)){
                final XWPFChart chart = doc.getCharts().get(0);
                setBarData(chart, title, series, categories, values1, values2);
                setColumnData(chart, "Column variant");
                //save the result
                try(OutputStream out = new FileOutputStream("/home/echolee/ushares/demo.docx")){
                    doc.write(out);
                }
            }


        }
    }

    private static void setBarData(XWPFChart chart, String title, String[] series, String[] categories, Double[] values1, Double[] values2) {
        final List<XDDFChartData> data = chart.getChartSeries();
        logger.info("chart series : {}", data);
        final XDDFBarChartData bar = (XDDFBarChartData) data.get(0);
        final int numOfPoints = categories.length;
        final String categoryDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, 0, 0));
        final String valuesDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, 1, 1));
        final String valuesDataRange2 = chart.formatRange(new CellRangeAddress(1, numOfPoints, 2, 2));
        final XDDFDataSource<?> categoriesData = XDDFDataSourcesFactory.fromArray(categories, categoryDataRange, 0);
        final XDDFNumericalDataSource<? extends Number> valuesData = XDDFDataSourcesFactory.fromArray(values1, valuesDataRange, 1);
        values2[6] = 16.0; // if you ever want to change the underlying data
        final XDDFNumericalDataSource<? extends Number> valuesData2 = XDDFDataSourcesFactory.fromArray(values2, valuesDataRange2, 2);

        XDDFChartData.Series series1 = bar.getSeries().get(0);
        series1.replaceData(categoriesData, valuesData);
        series1.setTitle(series[0], chart.setSheetTitle(series[0], 0));
        XDDFChartData.Series series2 = bar.addSeries(categoriesData, valuesData2);
        series2.setTitle(series[1], chart.setSheetTitle(series[1], 1));

        chart.plot(bar);
        chart.setTitleText(title); // https://stackoverflow.com/questions/30532612
        chart.setTitleOverlay(false);


    }
    private static void setColumnData(XWPFChart chart, String chartTitle) {
        // Series Text
        List<XDDFChartData> series = chart.getChartSeries();
        XDDFBarChartData bar = (XDDFBarChartData) series.get(0);
        logger.info("bar direction : {}", bar.getBarDirection());

        // in order to transform a bar chart into a column chart, you just need to change the bar direction
//        bar.setBarDirection(BarDirection.COL);

        // looking for "Stacked Bar Chart"? uncomment the following line
        logger.info("bar grouping : {}", bar.getBarGrouping());
//        bar.setBarGrouping(BarGrouping.STANDARD);

        // additionally, you can adjust the axes
        logger.info("bar category axis orientation : {}", bar.getCategoryAxis().getOrientation());
//        bar.getCategoryAxis().setOrientation(AxisOrientation.MAX_MIN);
        logger.info("bar value axes position : {}", bar.getValueAxes().get(0).getPosition());
        bar.getValueAxes().get(0).setPosition(AxisPosition.TOP);

    }
}
