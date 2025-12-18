package io.jingplayground;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class Set13 {
    public static int scoreText(String text) {
        String commonChars = "ETAOIN SHRDLU";
        int score = 0;
        for (char c : text.toUpperCase().toCharArray()) {
            if (commonChars.indexOf(c) != -1) {
                score++;
            }
        }
        return score;
    }

    static void main() {
        String hex = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
        byte[] cipher = HexFormat.of().parseHex(hex);

        int bestScore = Integer.MIN_VALUE;
        String bestPlaintext = "";
        char bestKey = 0;
        for (int key = 0; key < 256; key++) {
            byte[] decrypted = new byte[cipher.length];
            for (int i = 0; i < cipher.length; i++) {
                decrypted[i] = (byte) (cipher[i] ^ key);
            }
            String decryptedText = new String(decrypted, StandardCharsets.US_ASCII);
            int currentScore = scoreText(decryptedText);
            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestPlaintext = decryptedText;
                bestKey = (char) key;
            }
        }
        System.out.println("Best key: " + Integer.toHexString(bestKey));
        System.out.println("Decrypted text: " + bestPlaintext);
    }
}
