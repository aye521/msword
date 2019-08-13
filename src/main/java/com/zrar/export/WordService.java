package com.zrar.export;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordService {

    public static void replaceText(String text, String replace, XWPFParagraph p) {
        for (XWPFRun run : p.getRuns()) {
            final int pos = 0;
            final String txt = run.getText(pos);
            if (null != txt && txt.contains(text)) {
                run.setText(txt.replace(text, replace), pos);
            }
        }
    }
}
