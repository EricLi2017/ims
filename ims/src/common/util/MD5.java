package common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMd5String(String src) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md5.update(src.getBytes());
        return new BigInteger(1, md5.digest()).toString(16);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String src = "123456";
        String md5 = MD5.getMd5String(src);
        System.out.print(src + ":" + md5);
    }
}
