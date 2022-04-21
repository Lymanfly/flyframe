package org.lyman.utils;

import org.apache.shiro.crypto.hash.*;

public class EncryptUtils {

    public static final int DEFAULT_HASH_ITERATIONS = 1024;

    public static String encrypt(String password, String salt) {
        return encrypt(password, salt, null, null);
    }

    public static String encrypt(String password, String salt, Integer hashIterations) {
        return encrypt(password, salt, hashIterations, null);
    }

    public static String encrypt(String password, String salt, Integer hashIterations, EncryptionAlgorithm algorithm) {
        if (password == null)
            return null;
        if (hashIterations == null || hashIterations < 0)
            hashIterations = DEFAULT_HASH_ITERATIONS;
        SimpleHash hash;
        switch (algorithm) {
            case MD5:
                hash = new Md5Hash(password, salt, hashIterations);
            case SHA1:
                hash = new Sha1Hash(password, salt, hashIterations);
            case SHA384:
                hash = new Sha384Hash(password, salt, hashIterations);
            case SHA512:
                hash = new Sha512Hash(password, salt, hashIterations);
            default:
                hash = new Sha256Hash(password, salt, hashIterations);
        }
        return hash.toHex();
    }

}
