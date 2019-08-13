package com.zrar.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

class Warper {
    private static Field stringValue;
    private static Logger logger = LoggerFactory.getLogger(Warper.class);
    static {
        // String has a private char [] called "value"
        // if it does not, find the char [] and assign it to value
        try {
            stringValue = String.class.getDeclaredField("value");
        } catch(NoSuchFieldException ex) {
            // safety net in case we are running on a VM with a
            // different name for the char array.
            Field[] all = String.class.getDeclaredFields();
            for (int i=0; stringValue == null && i<all.length; i++) {
                if (all[i].getType().equals(char[].class)) {
                    stringValue = all[i];
                }
            }
        }
        if (stringValue != null) {
            stringValue.setAccessible(true); // make field public
        }
    }

    public static void main(String[] args) {
        final Warper warper = new Warper();
        warper.warpString();
    }

    Warper() {
    }

    String [] warpString() {
        final String s1 = "Romeo, Romeo, wherefore art thou oh Romero?";
        final String s2 = "Stop this romance nonsense, or I'll be sick";
        try {
            stringValue.set(s1, s2.toCharArray());
            stringValue.set("hi there", "cheers !".toCharArray());
            ObjectAddress.printAddresses("Warper-s1 addr", s1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new String [] {s1, s2};
    }

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
