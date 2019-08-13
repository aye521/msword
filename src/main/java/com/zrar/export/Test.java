package com.zrar.export;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    private static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        final File demoDoc = new File("./demo.docx");
        final InputStream template = Test.class.getResourceAsStream("/templates/edocProcess.docx");
        try (final FileOutputStream out = new FileOutputStream(demoDoc);
             final XWPFDocument doc = new XWPFDocument(template);) {
            String[] data1 = {"建议提案工作处", "238" , "6", "238", "100", "18", "100", "4", "6", "6", "3.7", "3.7"};
            String[] data2 = {"秘书一处", "18" , "", "18", "100", "18", "100", "", "", "", "3.7", "3.7"};
            String[] data3 = {"公报室", "9" , "1", "9", "100", "18", "100", "2", "3", "6", "3.7", "3.7"};
            String[] data4 = {"测试", "256" , "7", "45", "456", "56", "564", "8", "9", "45", "9.6", "9.3"};
            final List<String[]> datas = Arrays.asList(data1, data2, data3, data4);
            EdocProcessService.populateWord(doc, datas);
            doc.write(new FileOutputStream(demoDoc));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
