package io.jingplayground.tests;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.*;
import java.util.List;

public class CertificateValidateTest {

    static void main() throws Exception {
        System.setProperty("com.sun.security.enableAIAcaIssuers", "true"); // Enable Authority Information Access to download required middle CA certificates
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate serverCert;
        try (InputStream in = CertificateValidateTest.class
                .getClassLoader()
                .getResourceAsStream("_.google.com.hk.crt")) {
            assert in != null;
            if(cf.generateCertificate(in) instanceof X509Certificate x509Certificate) {
                serverCert = x509Certificate;
            } else {
                throw new UnsupportedOperationException();
            }
        }
        // load system trusted CA
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream in = new FileInputStream(
                System.getProperty("java.home") + "/lib/security/cacerts")) {
            trustStore.load(in, "changeit".toCharArray());
        }

        // build cert store
        CertStore certStore = CertStore.getInstance(
                "Collection",
                new CollectionCertStoreParameters(List.of(serverCert))
        );

        // build cert selector
        X509CertSelector selector = new X509CertSelector();
        selector.setCertificate(serverCert);

        // configure pkix build parameters
        PKIXBuilderParameters buildParams = new PKIXBuilderParameters(trustStore, selector);
        buildParams.addCertStore(certStore);
        buildParams.setRevocationEnabled(false); // will not pass if enabled

        // build cert chain
        CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");
        try {
            PKIXCertPathBuilderResult result =
                    (PKIXCertPathBuilderResult) builder.build(buildParams);

            System.out.println("Cert chain build successful");
            System.out.println("Anchor: " +
                    result.getTrustAnchor().getTrustedCert().getSubjectX500Principal());
            System.out.println("Chain：");
            for (Certificate c : result.getCertPath().getCertificates()) {
                X509Certificate xc = (X509Certificate) c;
                System.out.println(" - " + xc.getSubjectX500Principal());
            }
        } catch (CertPathBuilderException e) {
            e.printStackTrace();
            System.out.println("Cert chain failed : " + e.getMessage());
        }

        // check cert valid
        try {
            serverCert.checkValidity();
            System.out.println("Cert validity check successful");
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            e.printStackTrace();
            System.out.println("Cert not valid" + e.getMessage());
        }
    }
}
