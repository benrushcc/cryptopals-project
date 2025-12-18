package io.jingplayground;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Set16 {
    public static byte[] readAndDecode(String fileName) throws IOException {
        try (InputStream stream = Set16.class.getClassLoader().getResourceAsStream(fileName)) {
            assert stream != null;
            byte[] raw = stream.readAllBytes();
            String clean = new String(raw, StandardCharsets.US_ASCII).replaceAll("\\s+", "");
            return Base64.getDecoder().decode(clean);
        }
    }

    public static int hammingDistance(byte[] a, byte[] b) {
        if (a.length != b.length) {
            throw new AssertionError();
        }
        int distance = 0;
        for (int i = 0; i < a.length; i++) {
            int xor = a[i] ^ b[i];
            distance += Integer.bitCount(xor);
        }
        return distance;
    }

    public static double normalizedDistance(byte[] data, int keysize) {
        int blocks = 40;
        int total = 0;
        int pairs = 0;
        for (int i = 0; i < blocks - 1; i++) {
            int start1 = i * keysize;
            int start2 = (i + 1) * keysize;
            if (start2 + keysize > data.length) break;
            byte[] block1 = Arrays.copyOfRange(data, start1, start1 + keysize);
            byte[] block2 = Arrays.copyOfRange(data, start2, start2 + keysize);
            total += hammingDistance(block1, block2);
            pairs++;
        }
        return (double) total / (pairs * keysize);
    }

    public static List<Integer> findLikelyKeySizes(byte[] data, int min, int max) {
        List<int[]> scores = new ArrayList<>();
        for (int keysize = min; keysize <= max; keysize++) {
            double score = normalizedDistance(data, keysize);
            scores.add(new int[]{keysize, (int) (score * 1e6)}); // for sorting
        }
        scores.sort(Comparator.comparingInt(a -> a[1]));
        List<Integer> best = new ArrayList<>();
        for (int i = 0; i < 3; i++) best.add(scores.get(i)[0]);
        return best;
    }

    public static byte[][] transpose(byte[] data, int keySize) {
        int numBlocks = (data.length + keySize - 1) / keySize;
        byte[][] transposed = new byte[keySize][];

        for (int i = 0; i < keySize; i++) {
            ByteArrayOutputStream col = new ByteArrayOutputStream();
            for (int j = 0; j < numBlocks; j++) {
                int index = j * keySize + i;
                if (index < data.length) col.write(data[index]);
            }
            transposed[i] = col.toByteArray();
        }
        return transposed;
    }

    public static byte findBestKey(byte[] block) {
        int bestScore = Integer.MIN_VALUE;
        byte bestKey = 0;

        for (int key = 0; key < 256; key++) {
            byte[] decoded = new byte[block.length];
            for (int i = 0; i < block.length; i++) decoded[i] = (byte) (block[i] ^ key);

            int score = Set13.scoreBytes(decoded);
            if (score > bestScore) {
                bestScore = score;
                bestKey = (byte) key;
            }
        }
        return bestKey;
    }

    static void main() throws IOException {
        String s1 = "this is a test";
        String s2 = "wokka wokka!!!";
        System.out.println(hammingDistance(s1.getBytes(), s2.getBytes())); // should print 37
        byte[] bytes = readAndDecode("6.txt");
        List<Integer> likelyKeySizes = findLikelyKeySizes(bytes, 2, 40);
        int keySize = likelyKeySizes.getFirst();
        System.out.println("key size: " + keySize);
        byte[][] blocks = transpose(bytes, keySize);
        byte[] key = new byte[keySize];
        for (int i = 0; i < keySize; i++) {
            key[i] = findBestKey(blocks[i]);
        }
        System.out.println("key :" + new String(key, StandardCharsets.US_ASCII));
        byte[] original = Set15.multiByteXor(bytes, key);
        System.out.println("original text :" + new String(original, StandardCharsets.US_ASCII));
    }
}
