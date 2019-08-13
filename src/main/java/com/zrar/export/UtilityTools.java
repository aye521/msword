package com.zrar.export;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilityTools {
    public static String DefaultFormat = "yyyy-MM-dd";

    public static String formaDate(Date date, String format) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getShortFormat(Date date) {
        return formaDate(date, DefaultFormat);
    }

    public static void main(String[] args) {
        System.out.println(formaDate(new Date(), "yyyy年"));
        System.out.println(formaDate(new Date(), "M月"));
    }
}
