package com.summer.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyIncrementGeneratorUtil {
//    private MyIncrementGeneratorUtil() {
//    }
    
    /**
     * 得到32位唯一的UUID
     *
     * @return 唯一编号
     */
    public static Serializable uuid() {
        UUID uid = UUID.randomUUID();
        return uid.toString().replace("-", "");
    }
    
    /**
     * 根据长度得到唯一编号
     *
     * @param length 长度
     * @return 唯一编号
     */
    public static Serializable uuid(int length) {
        UUID uid = UUID.randomUUID();
//        String temp = uid.toString().replace("-", "");
//        if (length > 0 && length < temp.length()) {
//            temp = temp.substring(temp.length() - length);
//        }
        return uid;
    }
    
    /**
     * 根据长度得到随机字符串，一位字母一位数字
     *
     * @param length 长度
     * @return 字符串
     */
    public static String unique(int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                str.append(getRandom(97, 122));
            } else {
                str.append(getRandom(48, 57));
            }
        }
        return str.toString();
    }
    
    /**
     * 得到纯数字编号
     *
     * @param length 长度
     * @return
     */
    public static String number(int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0)
                str.append(getRandom(49, 57));
            else
                str.append(getRandom(48, 57));
        }
        return str.toString();
    }
    
    /**
     * 根据开始和结束大小得到单一字符
     *
     * @param begin 开始值
     * @param end   结束值
     * @return 单一字符
     */
    private static String getRandom(int begin, int end) {
        String str = "";
        Random rd = new Random();
        int number = 0;
        while (str.length() == 0) {
            number = rd.nextInt(end + 1);
            if (number >= begin && number <= end)
                str = String.valueOf((char) number);
        }
        return str;
    }
    
    public static String usingMath(int length) {
        String alphabetsInUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String alphabetsInLowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        // create a super set of all characters
        String allCharacters = alphabetsInLowerCase + alphabetsInUpperCase + numbers;
        // initialize a string to hold result
        
        String randomString = new String();
        // loop for 10 times
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * allCharacters.length());
            
            randomString = randomString + (allCharacters.charAt(randomIndex));

//            if (c >= 'a' && c <= 'z') {
//                result += (c + "").toUpperCase();
//            } else if (c >= 'A' && c <= 'Z') {
//                result += (c + "").toLowerCase();
//            } else {
//                result += c;
//            }
            if ((i + 1) % 4 == 0 && i != allCharacters.length() - 1) {
                randomString += "-";
                
//                result = result.substring(0, 8);
            }
            
//        for (int i = 0; i < length; i++) {
//            // generate a random number between 0 and length of all characters
//            int randomIndex = (int)(Math.random() * allCharacters.length());
//            // retrieve character at index and add it to result
//            randomString.append(allCharacters.charAt(randomIndex));
//        }
        }
        randomString = randomString.substring(0, randomString.length()-1);
        return randomString;
    }
    
//    public static void main(String[] args) throws IOException {
//        String a = usingMath(20);
//        System.out.println(a);
//    }
}


