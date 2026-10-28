package com.vn.otofun.utils;


import android.util.Base64;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AES
{
    private static SecretKeySpec secretKey ;
    private static byte[] key ;

    private static String decryptedString;
    private static String encryptedString;


    public static void setKey(String myKey){
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
//            sha = MessageDigest.getInstance("SHA-1");
//            key = sha.digest(key);
            key = Arrays.copyOf(key, 32);
            secretKey = new SecretKeySpec(key, "AES");

//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static String getDecryptedString() {
        return decryptedString;
    }

    public static void setDecryptedString(String decryptedString) {
        AES.decryptedString = decryptedString;
    }

    public static String getEncryptedString() {
        return encryptedString;
    }

    public static void setEncryptedString(String encryptedString) {
        AES.encryptedString = encryptedString;
    }

    public static String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            int blockSize = 16; // cipher.getBlockSize();

            byte[] inputBytes = strToEncrypt.getBytes();
            int byteLength = inputBytes.length;
            if (byteLength % blockSize != 0) {
                byteLength = byteLength + (blockSize - (byteLength % blockSize));
            }

            byte[] paddedBytes = new byte[byteLength];

            System.arraycopy(inputBytes, 0, paddedBytes, 0, inputBytes.length);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] data = cipher.doFinal(paddedBytes);

            setEncryptedString(Base64.encodeToString(data, Base64.DEFAULT));

        }
        catch (Exception e) {
            System.out.println("Error while encrypting: "+e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            setDecryptedString(new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT))));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: "+e.toString());

        }
        return null;
    }

    public static String encryptWithDefPwd(String input) {
//        final String dp = "MYCDRWG42YMHCQZD";//FlurryHelper.FLURRY_TRACKING_ID.substring(0, 16);
        final String dp = "MYCDRWG42YMHCQZDMYCDRWG42YMHCQZD";//FlurryHelper.FLURRY_TRACKING_ID.substring(0, 16);
        AES.setKey(dp);
        AES.encrypt(input);
        final String enc = AES.getEncryptedString();
        return enc;
    }

}