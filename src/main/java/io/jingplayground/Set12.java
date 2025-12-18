package io.jingplayground;

import java.util.HexFormat;

public class Set12 {
    static void main() {
        String a = "1c0111001f010100061a024b53535009181c";
        String b = "686974207468652062756c6c277320657965";
        byte[] a1 = HexFormat.of().parseHex(a);
        byte[] b1 = HexFormat.of().parseHex(b);
        if(a1.length != b1.length) {
            throw new AssertionError();
        }
        byte[] r = new byte[a1.length];
        for(int i = 0; i < a1.length; i++) {
            r[i] = (byte) (a1[i] ^ b1[i]);
        }
        String s = HexFormat.of().formatHex(r);
        System.out.println(s); // 746865206b696420646f6e277420706c6179
    }
}
