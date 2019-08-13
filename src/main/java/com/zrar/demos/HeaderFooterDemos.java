package com.zrar.demos;

import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class HeaderFooterDemos {
    public static void main(String[] args) {
        final File demoDoc = new File("/home/echolee/ushares/demo.docx");
        try(final XWPFDocument doc = new XWPFDocument();
            final FileOutputStream fileOutputStream = new FileOutputStream(demoDoc)) {
            createHeaderFooter(doc);
            //write to file
            doc.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createHeaderFooter(XWPFDocument doc){
        // Create a header with a 1 row, 3 column table
        // changes made for issue 57366 allow a new header or footer
        // to be created empty. This is a change. You will have to add
        // either a paragraph or a table to the header or footer for
        // the document to be considered valid.
        final XWPFHeader hdr = doc.createHeader(HeaderFooterType.DEFAULT);
        final XWPFTable tbl = hdr.createTable(1, 3);
        //set the padding around text in the cells to 1/10th of an inch
        int pad = (int) (.1 * 1440);
        tbl.setCellMargins(pad, pad ,pad ,pad);
        //set table width to 6.5 inches in 1440th of a point
        tbl.setWidth((int) (6.5 * 1440));
        //can not yet set table or cell width properly, tables default to autofit layout, and this requires fixed layout
        final CTTbl ctTbl = tbl.getCTTbl();
        final CTTblPr ctTblPr = ctTbl.addNewTblPr();
        //CTTblLayoutType needs full version of ooxml-shcemas 1.3 or 1.4(java 8)
        final CTTblLayoutType layoutType = ctTblPr.addNewTblLayout();
        layoutType.setType(STTblLayoutType.FIXED);
        //now set up a grid for the table,cells will fit into grid
        //each cell width is 3120 in 1440th of an inch or 1/3rd of 6.5"
        final BigInteger width = new BigInteger("3120");
        final CTTblGrid tblGrid = ctTbl.addNewTblGrid();
        for (int i = 0; i < 3; i++) {
            tblGrid.addNewGridCol().setW(width);
        }
        //add paragraphs to the cell

        String[] texts = {"left", "cernter", "right"};
        final XWPFTableRow row = tbl.getRow(0);
        for (int i = 0; i < texts.length; i++) {
            row.getCell(i).getParagraphArray(0).createRun().setText("header cell : " + texts[i]);
        }
        //create a footer with paragraph
        final XWPFFooter ftr = doc.createFooter(HeaderFooterType.DEFAULT);
        final XWPFParagraph para = ftr.createParagraph();
        para.setAlignment(ParagraphAlignment.CENTER);
        para.createRun().setText("footer text");
    }
}
