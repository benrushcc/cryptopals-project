package io.jingplayground;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Set17 {
    static void main() throws Throwable {
        String key = "YELLOW SUBMARINE";
        byte[] cipherBytes = Set16.readAndDecode("7.txt");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] plainBytes = cipher.doFinal(cipherBytes);
        String plaintext = new String(plainBytes);
        System.out.println(plaintext);
    }
}
