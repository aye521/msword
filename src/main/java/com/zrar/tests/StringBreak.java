package com.zrar.tests;

import java.lang.reflect.Field;

public class StringBreak {
    private static Field fieldVal;
    static {
        try {
            fieldVal = String.class.getDeclaredField("value");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        final StringBreak obj = new StringBreak();
//        obj.warpString();
        obj.warpString2();
    }

    private void warpString() throws NoSuchFieldException, IllegalAccessException {
        final String s1 = "hello";
        final String s2 = "hallo";
        setValue(s1, s2.toCharArray());
        System.out.println(s1.equals(s2));
        System.out.println(s1);
    }

    String warpString2() {
        final String s1 = "Romeo, Romeo, wherefore art thou oh Romero?";
        final String s2 = "Stop this romance nonsense, or I'll be sick";
        try {
            System.out.println(s1);
            setValue(s1, s2.toCharArray());
            System.out.println(s1);
            System.out.println("after warp : " + s1);
//            System.out.println(s1.equals(s2));
//            System.out.println("Romeo, Romeo, wherefore art thou oh Romero?");
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return s1;
    }

    private static void setValue(String s, char[] chars) throws NoSuchFieldException, IllegalAccessException {
        fieldVal.setAccessible(true);
        fieldVal.set(s, chars);
    }

    private static char[] getValue(String s) throws IllegalAccessException {
        fieldVal.setAccessible(true);
        return (char[]) fieldVal.get(s);
    }
}
