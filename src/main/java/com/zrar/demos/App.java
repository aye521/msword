package com.zrar.demos;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 */
public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        final File demoDoc = new File("/home/echolee/ushares/demo.docx");
        try (FileOutputStream out = new FileOutputStream(demoDoc);
             final XWPFDocument doc = new XWPFDocument()) {
            createP(doc);
            createTbl(doc);
            doc.write(out);
            logger.info("create doc {} successfully", demoDoc.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        logger.info("extracted text {}", extractText(demoDoc));
    }

    private static void createTbl(XWPFDocument document) {
        XWPFTable table = document.createTable();
        //row one
        XWPFTableRow r1 = table.getRow(0);
        r1.getCell(0).setText("row one , column one");
        r1.addNewTableCell().setText("row one, column two");
        r1.addNewTableCell().setText("row one, column three");
        r1.addNewTableCell().setText("row one, column four");
        //row two
        XWPFTableRow r2 = table.createRow();
        r2.getCell(0).setText("row two, column one");
        r2.getCell(1).setText("row two , column two");
        r2.getCell(2).setText("row two , column three");
        r2.getCell(3).setText("row two , column four");
//        r2.addNewTableCell().setText("row two, column five");
        //row three
        XWPFTableRow r3 = table.createRow();
        r3.getCell(0).setText("row three, column one");
        r3.getCell(2).setText("row three, column three");
        r3.getCell(3).setText("row three, column four");
//        r3.addNewTableCell().setText("row three, extra column");
//        r3.addNewTableCell().setText("row three, extra column");
    }

    private static void createP(XWPFDocument doc) {
        //title
        final XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        title.setBorderBottom(Borders.BASIC_BLACK_DASHES);
        title.setBorderTop(Borders.DOUBLE_DIAMONDS);
        title.setBorderBetween(Borders.DOUBLE_WAVE);
        final XWPFRun titleRun = title.createRun();
        titleRun.setText("Paragraph one : 标题测试 A Title！");
        titleRun.setBold(true);
        titleRun.setColor("009933");
        titleRun.setFontSize(20);
        //subtitle
        XWPFParagraph subTitle = doc.createParagraph();
        subTitle.setAlignment(ParagraphAlignment.RIGHT);
        subTitle.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun subTitleRun = subTitle.createRun();
        subTitleRun.setText("Paragraph two : 子标题，A subTitle");
        subTitleRun.setColor("00cc44");
        subTitleRun.setFontSize(16);
        subTitleRun.setTextPosition(20);
        subTitleRun.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
        //insert an image,not working on poi version 3.10.final but ok with 3.17
        XWPFParagraph image = doc.createParagraph();
        image.setAlignment(ParagraphAlignment.CENTER);
        //set the distance between this image and the text below it;
        XWPFRun imageRun = image.createRun();
        imageRun.setText("Fig.1 A Natural Scene");
        imageRun.setBold(true);
        imageRun.setTextPosition(20);
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("pics/keyboard.jpg").toURI());
            logger.info("image path {}", path);
            // 516 x 295 pixels
            imageRun.addPicture(Files.newInputStream(path), XWPFDocument.PICTURE_TYPE_JPEG, path.getFileName().toString(),
                    Units.pixelToEMU(516), Units.pixelToEMU(295));
        } catch (URISyntaxException | IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        //insert a text
        XWPFParagraph para1 = doc.createParagraph();
        para1.setAlignment(ParagraphAlignment.LEFT);
        //BORDERS
        para1.setBorderBottom(Borders.DOUBLE);
        para1.setBorderTop(Borders.DOUBLE);
        para1.setBorderRight(Borders.DOUBLE);
        para1.setBorderLeft(Borders.DOUBLE);
        para1.setBorderBetween(Borders.SINGLE);
        XWPFRun para1Run = para1.createRun();
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("text.txt").toURI());
            String fileString = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            para1Run.setText(fileString);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static String extractText(File file) {
        String text = "";
        try(InputStream is = new FileInputStream(file)) {
            XWPFDocument doc = new XWPFDocument(is);
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            text = extractor.getText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;

    }

}
