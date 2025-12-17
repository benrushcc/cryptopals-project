package io.jingplayground;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

public class Sec11 {
    static void main() {
        String hex = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        byte[] bytes = HexFormat.of().parseHex(hex);
        byte[] base64 = Base64.getEncoder().encode(bytes);
        String base64Str = new String(base64, StandardCharsets.US_ASCII);
        System.out.println(base64Str); // SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t
    }
}
