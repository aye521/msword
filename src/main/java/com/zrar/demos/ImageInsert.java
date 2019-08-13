package com.zrar.demos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class ImageInsert {

    //Creating word document instance...
    static XWPFDocument document = new XWPFDocument();
    static XWPFParagraph p = document.createParagraph();
    static XWPFRun r = p.createRun();

    private static void addImagesToWordDocument(File imageFile, String textToDisplay)
            throws IOException, InvalidFormatException {
        // Read image file
        BufferedImage bimg1 = ImageIO.read(imageFile);
        int width = bimg1.getWidth();
        int height = bimg1.getHeight();
        // get image file name
        String imgFile = imageFile.getName();
        // get image format
        int imgFormat = getImageFormat(imgFile);
        // adding image and text parameters with the help of below function
        r.setText(textToDisplay);
        r.addBreak();
        r.addPicture(new FileInputStream(imageFile), imgFormat, imgFile, Units.toEMU(width), Units.toEMU(height));


    }
    /*
     * Checking the image file format...
     */
    private static int getImageFormat(String imgFileName) {
        int format;
        if (imgFileName.endsWith(".emf"))
            format = XWPFDocument.PICTURE_TYPE_EMF;
        else if (imgFileName.endsWith(".wmf"))
            format = XWPFDocument.PICTURE_TYPE_WMF;
        else if (imgFileName.endsWith(".pict"))
            format = XWPFDocument.PICTURE_TYPE_PICT;
        else if (imgFileName.endsWith(".jpeg") || imgFileName.endsWith(".jpg"))
            format = XWPFDocument.PICTURE_TYPE_JPEG;
        else if (imgFileName.endsWith(".png"))
            format = XWPFDocument.PICTURE_TYPE_PNG;
        else if (imgFileName.endsWith(".dib"))
            format = XWPFDocument.PICTURE_TYPE_DIB;
        else if (imgFileName.endsWith(".gif"))
            format = XWPFDocument.PICTURE_TYPE_GIF;
        else if (imgFileName.endsWith(".tiff"))
            format = XWPFDocument.PICTURE_TYPE_TIFF;
        else if (imgFileName.endsWith(".eps"))
            format = XWPFDocument.PICTURE_TYPE_EPS;
        else if (imgFileName.endsWith(".bmp"))
            format = XWPFDocument.PICTURE_TYPE_BMP;
        else if (imgFileName.endsWith(".wpg"))
            format = XWPFDocument.PICTURE_TYPE_WPG;
        else {
            return 0;
        }
        return format;
    }


    public static void main(String[] args) throws InvalidFormatException, IOException {

        File image = new File("./src/main/resources/pics/ip.png");
        //Passing imgae file lication and text/message in addImagesToWordDocument parameter.
        //This function will append content in word document.
        addImagesToWordDocument(image,"image one display");
        // writing content [image & text ] to word document...
        FileOutputStream out = new FileOutputStream("/home/echolee/ushares/sample-doc-file.docx");
        document.write(out);
        out.close();
        document.close();
    }

}
