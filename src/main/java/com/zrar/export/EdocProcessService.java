package com.zrar.export;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class EdocProcessService {
    private static Logger logger = LoggerFactory.getLogger(EdocProcessService.class);

    public static void main(String[] args) {

    }

    static void populateWord(XWPFDocument doc, List<String[]> data) {
        final List<XWPFParagraph> paragraphs = doc.getParagraphs();
        //modify title and subtitle
        populateTitles(paragraphs);
        final XWPFTable tbl = doc.getTables().get(0);
        final int templateRowPos = 2;
        final XWPFTableRow templateRow = tbl.getRow(templateRowPos);
        for (String[] arr : data) {
            XWPFTableRow newRow = cloneRow(tbl, templateRow);
            populateRow(arr, newRow);
            tbl.addRow(newRow, 3);
        }
        tbl.removeRow(templateRowPos);
//        logger.info("row cell size : {}", tbl.createRow().getTableCells().size());


    }

    private static void populateRow(String[] data, XWPFTableRow row) {
        for (int i = 0; i < data.length; i++) {
            final XWPFTableCell cell = row.getCell(i);
            final XWPFRun run = cell.getParagraphArray(0).getRuns().get(0);
            run.setText(data[i], 0);
        }

    }

    private static XWPFTableRow cloneRow(XWPFTable tbl, XWPFTableRow templateRow) {

        CTRow ctrow = null;
        try {
            ctrow = CTRow.Factory.parse(templateRow.getCtRow().newInputStream());
        } catch (XmlException | IOException e) {
            e.printStackTrace();
        }
        return new XWPFTableRow(ctrow, tbl);
    }

    private static void populateTitles(List<XWPFParagraph> paragraphs) {
        Date now = new Date();
        String titleDate = UtilityTools.formaDate(now, "yyyy年MM月");
        String statsDate = UtilityTools.getShortFormat(now);
        for (XWPFParagraph p : paragraphs) {
            final String text = p.getText().trim();
            if (text.length() > 0){
                final String titleHolder = "titleDate";
                final String statsHolder = "sdate";
                if (text.contains(titleHolder)){
                    WordService.replaceText(titleHolder, titleDate, p);
                } else if(text.contains(statsHolder)) {
                    WordService.replaceText(statsHolder, statsDate, p);
                }
                logger.info("paragraph text (after replaced) : {}", p.getText());
            }

        }
    }


    private static Map<String, String> array2map(String[] data) {
        String[] keys =  {"office", "rcvNum", "transNum", "dealNum", "dealRate", "doneNum", "doneRate",
                "undoneNum", "undoneRate", "suspendNum", "avgDays", "actualDays"};
        final HashMap<String, String> row = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            row.put(keys[i], nonEmpty(data[i]));
        }
        return row;
    }

    private static String nonEmpty(String val) {
        boolean isEmpty = (null == val) || ("".equals(val.trim()));
        return  isEmpty ? "--" : val;
    }

}
