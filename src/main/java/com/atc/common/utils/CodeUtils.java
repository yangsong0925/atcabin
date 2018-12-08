package com.atc.common.utils;

public class CodeUtils {

    public static int CRC16_TR(int a, char b) {
        char i, c;
        a = a ^ b;
        for (i = 0; i < 8; i++) {
            c = 1;
            if ((0x0001 & a) == 0)
                c = 0;
            a = a >> 1;
            if (c == 1)
                a = a ^ 0xa001;
        }
        return (a);
    }

}
