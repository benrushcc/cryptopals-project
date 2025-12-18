package io.jingplayground;

import java.util.HexFormat;

public class Set15 {
    public static String encrypt(String plaintext, String key) {
        byte[] plaintextBytes = plaintext.getBytes();
        byte[] keyBytes = key.getBytes();
        byte[] cipherBytes = new byte[plaintextBytes.length];

        for (int i = 0; i < plaintextBytes.length; i++) {
            cipherBytes[i] = (byte) (plaintextBytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        return HexFormat.of().formatHex(cipherBytes);
    }

    static void main() {
        String plaintext = """
Burning 'em, if you ain't quick and nimble
I go crazy when I hear a cymbal""";
        String key = "ICE";

        String ciphertext = encrypt(plaintext, key);
        System.out.println(ciphertext); // 0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f
    }
}
