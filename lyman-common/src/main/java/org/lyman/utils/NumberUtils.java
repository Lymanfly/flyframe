package org.lyman.utils;

import org.apache.commons.codec.digest.Md5Crypt;

public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(Md5Crypt.md5Crypt("17601328421".getBytes()));
        }
    }

}
