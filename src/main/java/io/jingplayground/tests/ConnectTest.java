package io.jingplayground.tests;

import java.net.HttpURLConnection;
import java.net.URI;

/**
 * -Dhttp.proxyHost=127.0.0.1
 * -Dhttp.proxyPort=59527
 * -Dhttps.proxyHost=127.0.0.1
 * -Dhttps.proxyPort=59527
 */
public class ConnectTest {
    static void main() throws Exception {
        URI uri = URI.create("https://www.google.com");
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.connect();
        System.out.println("Response: " + conn.getResponseCode());
    }
}
