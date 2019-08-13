package com.zrar.tests;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class MindWarp {
    private static Logger logger = LoggerFactory.getLogger(MindWarp.class);
    public static void main(String[] args) {
//        String s3 = "hi there";
//        String s = "Romeo, Romeo, wherefore art thou oh Romero?";
//        System.out.println("Romeo, Romeo, wherefore art thou oh Romero?");
//        System.out.println("hi there".equals("cheers !"));

        final String[] strArr = new Warper().warpString();
        ObjectAddress.printAddresses("s1 addr", strArr[0]);
        final String s1 = "Romeo, Romeo, wherefore art thou oh Romero?";
        ObjectAddress.printAddresses("s1 addr", s1);
        System.out.println("Romeo, Romeo, wherefore art thou oh Romero?");
//        logger.info("different class literal equals ?  {}", s1 == s);
    }
//    private static final String OH_ROMEO = "Romeo, Romeo, wherefore art thou oh Romero?";
//    private static final Warper warper = new Warper();

    private static void setValue(String s, char[] chars) {
        final Field fieldVal;
        try {
            fieldVal = String.class.getDeclaredField("value");
            fieldVal.setAccessible(true);
            fieldVal.set(s, chars);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static char[] getValue(String s) {
        final Field fieldVal;
        Object o = null;
        try {
            fieldVal = String.class.getDeclaredField("value");
            fieldVal.setAccessible(true);
            o = fieldVal.get(s);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return (char[]) o;
    }



}
