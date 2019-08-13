package com.zrar.tests;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandleDemo
{
    public static void main(String[] args) throws Throwable
    {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        invokeStatic(lookup);
        transformAuguments(lookup);
    }

    private static void invokeStatic(MethodHandles.Lookup lookup) throws Throwable {
        MethodHandle mh = lookup.findStatic(MethodHandleDemo.class, "hello",
                MethodType.methodType(void.class));
        mh.invokeExact();
    }

    private static void transformAuguments(MethodHandles.Lookup lookup) throws Throwable {
        MethodHandle mh = lookup.findStatic(Math.class, "pow",
                MethodType.methodType(double.class,
                        double.class,
                        double.class));
        mh = MethodHandles.insertArguments(mh, 1, 10);
        System.out.printf("2^10 = %d%n", (int) (double) mh.invoke(2.0));
    }

    static void hello()
    {
        System.out.println("hello");
    }
}
