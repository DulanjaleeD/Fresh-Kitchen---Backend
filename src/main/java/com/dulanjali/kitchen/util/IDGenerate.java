package com.dulanjali.kitchen.util;

public class IDGenerate {
    public static String userId() {
        return "U-" + (int)(Math.random() * 9000 + 1000);
    }

}
