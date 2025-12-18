package io.jingplayground;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;
import java.util.List;

public class Set14 {
    static byte[] singleByteXOR(byte[] data, byte key) {
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            out[i] = (byte) (data[i] ^ key);
        }
        return out;
    }

    static void main() throws IOException, URISyntaxException {
        URL url = Set14.class.getClassLoader().getResource("4.txt");
        assert url != null;
        List<String> strings = Files.readAllLines(Paths.get(url.toURI()));

        int bestScore = 0;
        String bestPlaintext = null;
        String bestHex = null;
        int bestKey = -1;

        for (String hex : strings) {
            byte[] ciphertext = HexFormat.of().parseHex(hex.trim());
            for (int key = 0; key < 256; key++) {
                byte[] decrypted = singleByteXOR(ciphertext, (byte) key);
                String plaintext = new String(decrypted);
                int s = Set13.scoreText(plaintext);
                if (s > bestScore) {
                    bestScore = s;
                    bestPlaintext = plaintext;
                    bestHex = hex;
                    bestKey = key;
                }
            }
        }

        System.out.println("Best line: " + bestHex);
        System.out.println("Key: " + bestKey + " ('" + (char) bestKey + "')");
        System.out.println("Decrypted text: " + bestPlaintext);
    }
}
