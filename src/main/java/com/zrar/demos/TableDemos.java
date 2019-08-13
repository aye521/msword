package com.zrar.demos;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class TableDemos {
    private static Logger logger = LoggerFactory.getLogger(TableDemos.class);

    public static void main(String[] args) {
        final File demoDoc = new File("/home/echolee/ushares/demo.docx");
        try(final XWPFDocument doc = new XWPFDocument();
            final FileOutputStream fileOutputStream = new FileOutputStream(demoDoc)) {
//            createSimpleTable(doc);
            createStyledTable(doc);
            doc.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void createSimpleTable(XWPFDocument doc) {
        final XWPFTable table = doc.createTable(4, 4);
        // table cells have a list of paragraphs; there is an initial
        // paragraph created when the cell is created. If you create a
        // paragraph in the document to put in the cell, it will also
        // appear in the document following the table, which is probably
        // not the desired result.
        final XWPFParagraph p = table.getRow(0).getCell(0).getParagraphs().get(0);
        final XWPFRun r = p.createRun();
        r.setBold(true);
        r.setText("initial paragraph when the cell is created!");
        r.setUnderline(UnderlinePatterns.DASH_DOT_DOT_HEAVY);
        r.setTextPosition(50);
        final XWPFTableRow row = table.getRow(1);
        final XWPFTableCell cell = row.addNewTableCell();
        logger.info("new created cell's default paragraphs size : {}", cell.getParagraphs().size());
        cell.setText("added new cell first, ");
        //will be appended
        cell.setText("added new cell twice");
        cell.addParagraph().createRun().setText("new created paragraph");
        logger.info("new created cell's paragraphs size : {} (after created new paragraph)", cell.getParagraphs().size());
        XWPFTableRow r2 = table.createRow();
        r2.getCell(0).setText("created new row, column one");
        r2.getCell(1).setText("created new row , column two");
        r2.getCell(2).setText("created new row , column three");
        r2.getCell(3).setText("created new row , column four");


    }

    /**
     * Create a table with some row and column styling. I "manually" add the
     * style name to the table, but don't check to see if the style actually
     * exists in the document. Since I'm creating it from scratch, it obviously
     * won't exist. When opened in MS Word, the table style becomes "Normal".
     * I manually set alternating row colors. This could be done using Themes,
     * but that's left as an exercise for the reader. The cells in the last
     * column of the table have 10pt. "Courier" font.
     * I make no claims that this is the "right" way to do it, but it worked
     * for me. Given the scarcity of XWPF examples, I thought this may prove
     * instructive and give you ideas for your own solutions.
     */
    private static void createStyledTable(XWPFDocument doc){
        int nRows = 6;
        int nCols = 3;
        final XWPFTable table = doc.createTable(nRows, nCols);
        logger.info("default table layout : {}", table.getCTTbl().getTblPr().getTblLayout());
        //set the table style,if the style is not defined, the table style will become "Normal";
        //The CTx classes are XMLBeans wrappers. They are auto-generated from the published OOXML specification XML Schema files
        //If it's table, then it'll be CTTable, paragraph will be CTP
        final CTTblPr tblPr = table.getCTTbl().getTblPr();
        logger.info("table properties: {} \n style: {}", tblPr, tblPr.getTblStyle());
        final CTString styleStr = tblPr.addNewTblStyle();
        styleStr.setVal("StyledTable");
        logger.info("after set new style value : {}", tblPr.getTblStyle());
        //get a list of table rows
        final List<XWPFTableRow> rows = table.getRows();
        int rowCt = 0;
        int colCt = 0;
        for (XWPFTableRow row : rows) {
            //get table row properties (trPr)
            final CTTrPr trPr = row.getCtRow().addNewTrPr();
            //set row height ; units = twentieth of a point, 360 = 0.25(inch), 1 inch = 2.5cm
            final CTHeight height = trPr.addNewTrHeight();
            height.setVal(BigInteger.valueOf(720));
            //get the cells in this row
            final List<XWPFTableCell> cells = row.getTableCells();
            //add content to each cell
            for (XWPFTableCell cell : cells) {
                //get a table cell properties element (tcPr)
                final CTTcPr tcPr = cell.getCTTc().addNewTcPr();
                //set vertical alignment to "center"
                final CTVerticalJc va = tcPr.addNewVAlign();
                va.setVal(STVerticalJc.CENTER);
                //create cell color element
                final CTShd ctShd = tcPr.addNewShd();
                ctShd.setColor("auto");
                ctShd.setVal(STShd.CLEAR);
                if (rowCt == 0) {
                    //header row
                    ctShd.setFill("A7BFDE");
                } else if (rowCt % 2 == 0) {
                    //even row
                    ctShd.setFill("D3DFEE");
                } else {
                    //odd row
                    ctShd.setFill("EDF2F8");
                }
                // get the 1st paragraph
                final XWPFParagraph para = cell.getParagraphs().get(0);
                //create a run to contain the content
                final XWPFRun run = para.createRun();
                //style cell as desired
                if (colCt == nCols - 1) {
                    //last column is 10 pt
                    run.setFontSize(10);
                }
                if (rowCt == 0) {
                    //header row
                    run.setText("header row, col " + colCt);
                    run.setBold(true);
                    run.setFontSize(18);
                    run.setFontFamily("Hack");
                    para.setAlignment(ParagraphAlignment.CENTER);
                } else {
                    //other rows
                    run.setText("row " + rowCt + ", col " + colCt);
                    para.setAlignment(ParagraphAlignment.LEFT);
                }
                colCt ++;
            }
            colCt = 0;
            rowCt ++;
        }
    }

}
