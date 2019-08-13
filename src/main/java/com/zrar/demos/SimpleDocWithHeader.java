package com.zrar.demos;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import java.io.*;

public class SimpleDocWithHeader {
    public static void main(String[] args) throws IOException {
        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph p1 = doc.createParagraph();
            p1.setSpacingBetween(6, LineSpacingRule.AUTO);
//            p1.setVerticalAlignment(TextAlignment.BASELINE);
            XWPFRun r = p1.createRun();
            r.setText("text position : none ");
            r.setBold(true);
            r = p1.createRun();
            r.setText("text position : 50 ");
            System.out.println("run properties before: " + r.getCTR().getRPr() + " position: " + r.getTextPosition());
            r.setTextPosition(50);
            r.setBold(true);
            System.out.println("run properties : " + r.getCTR().getRPr() + " position: " + r.getTextPosition());
            r = p1.createRun();
            r.setText("text position: 10 ");
            r.setTextPosition(10);
            r.setItalic(true);
            r = p1.createRun();
            r.setText("text position: -10 ");
            r.setTextPosition(-10);
            r.setBold(true);
            r.addBreak();
            r = p1.createRun();
            r.setText("text position: -20 with break");
            r.setTextPosition(20);
            r.setBold(true);
//            r.addBreak();
            r.addCarriageReturn();
            r.setText("Text after carriageReturn, then new run");
            r = p1.createRun();
            r.setText("text position: -20 with break");
            r.setTextPosition(-20);
            r.setBold(true);
            r.addBreak();
            r.setText("tetxt after break then new run");
            final XWPFRun r2 = p1.createRun();
            r2.setText("Paragraph 1 : Third run , about subscript style");
            r2.setStrikeThrough(true);
            r2.setSubscript(VerticalAlign.SUBSCRIPT);
            //paragraph 2
            final XWPFParagraph p2 = doc.createParagraph();
            p2.setWordWrapped(true);
            p2.setPageBreak(true);
            p2.setAlignment(ParagraphAlignment.DISTRIBUTE);
            p2.setSpacingBetween(5, LineSpacingRule.AUTO);
            p2.setIndentationFirstLine(600);
            final XWPFRun r3 = p2.createRun();
            r3.setTextPosition(20);
            r3.setText("Paragraph 2 with PageBreak: first run, To be, or not to be: that is the question: "
                    + "Whether 'tis nobler in the mind to suffer "
                    + "The slings and arrows of outrageous fortune, "
                    + "Or to take arms against a sea of troubles, "
                    + "And by opposing end them? To die: to sleep; ");
            r3.addBreak(BreakType.PAGE);
            r3.setText("No more; and by a sleep to say we end "
                    + "The heart-ache and the thousand natural shocks "
                    + "That flesh is heir to, 'tis a consummation "
                    + "Devoutly to be wish'd. To die, to sleep; "
                    + "To sleep: perchance to dream: ay, there's the rub; "
                    + ".......");
            r3.setItalic(true);
//This would imply that this break shall be treated as a simple line break, and break the line after that word:

            XWPFRun r5 = p2.createRun();
            r5.setTextPosition(-10);
            r5.setText("For in that sleep of death what dreams may come(next:addCarriageReturn)");
            r5.addCarriageReturn();
            r5.setText("When we have shuffled off this mortal coil, "
                    + "Must give us pause: there's the respect "
                    + "That makes calamity of so long life;(next:addBreak)");
            r5.addBreak();
            r5.setText("For who would bear the whips and scorns of time, "
                    + "The oppressor's wrong, the proud man's contumely,(next:addBreak.All");

            r5.addBreak(BreakClear.ALL);
            r5.setText("The pangs of despised love, the law's delay, "
                    + "The insolence of office and the spurns " + ".......");

            // header
            CTP ctP = CTP.Factory.newInstance();
            CTText t = ctP.addNewR().addNewT();
            t.setStringValue("Simple Header with policy First");
            XWPFParagraph[] pars = new XWPFParagraph[1];
            p1 = new XWPFParagraph(ctP, doc);
            pars[0] = p1;

            XWPFHeaderFooterPolicy hfPolicy = doc.createHeaderFooterPolicy();
            hfPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, pars);
            //footer
            ctP = CTP.Factory.newInstance();
            t = ctP.addNewR().addNewT();
            t.setStringValue("Simple Footer with policy Even");
            pars[0] = new XWPFParagraph(ctP, doc);
            hfPolicy.createFooter(XWPFHeaderFooterPolicy.EVEN, pars);

            final File file = new File("/home/echolee/ushares/demo.docx");
            try (OutputStream os = new FileOutputStream(file)) {
                doc.write(os);
            }
        }
    }
}
