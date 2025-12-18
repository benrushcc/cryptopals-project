package io.jingplayground;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class Set15 {
    public static byte[] multiByteXor(byte[] textBytes, byte[] keyBytes) {
        byte[] r = new byte[textBytes.length];

        for (int i = 0; i < textBytes.length; i++) {
            r[i] = (byte) (textBytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        return r;
    }

    static void main() {
        String plaintext = """
Burning 'em, if you ain't quick and nimble
I go crazy when I hear a cymbal""";
        String key = "ICE";

        byte[] cipherBytes = multiByteXor(plaintext.getBytes(StandardCharsets.US_ASCII), key.getBytes(StandardCharsets.US_ASCII));
        System.out.println(HexFormat.of().formatHex(cipherBytes)); // 0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f
    }
}
