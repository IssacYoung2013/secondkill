package com.issac.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "12aldjaas";

    public static String inputPassToFormPass(String inputPass) {
        String str = "" +
                salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" +
                salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }
    
    public static String inputPassToDbPass(String input,String saltDB) {
        String formPass = inputPassToFormPass(input);
        return formPassToDBPass(formPass,saltDB);
    }

    public static void main(String[] args) {
//        System.out.println(inputPassToFormPass("123456")); //
//        System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "12aldjaas"));12aldjaas //
        System.out.println(inputPassToDbPass("123456","12aldjaas"));

    }
}
